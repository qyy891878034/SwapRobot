package com.liuqi.business.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.constant.ContractAddressConstant;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.constant.MethodConstant;
import com.liuqi.business.dto.ChainRequestDto;
import com.liuqi.business.dto.ContractTriggerDto;
import com.liuqi.business.dto.TriggerSmartContractDto;
import com.liuqi.business.model.ClampRecordModelDto;
import com.liuqi.business.service.BlockChainService;
import com.liuqi.business.service.DetectService;
import com.liuqi.mq.CommonProducer;
import com.liuqi.redis.RedisRepository;
import com.liuqi.utils.BlockChainUtil;
import com.liuqi.utils.MD5Util;
import com.liuqi.utils.MathUtil;
import jdk.nashorn.internal.ir.Block;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DetectServiceImpl implements DetectService {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private BlockChainService blockChainService;
    @Autowired
    private CommonProducer commonProducer;

    private final int CODE_ERROR = 4999;

    // 链名称[ETH/BSC/HECO... - websocket]
    private Map<String, WebSocket> chainWsMap = new HashMap<>();

    // 夹的交易信息和MD5
    private Map<String, ClampRecordModelDto> clampMap = new HashMap<>();
    // 夹了的交易哈希
    private Set<String> clampHashSet = new HashSet<>();
    // 待转账数据
    private Map<String, TriggerSmartContractDto> waitTransferMap = new HashMap<>();

    @Override
    public void startDetect(ChainRequestDto dto) {
        if (chainWsMap.containsKey(dto.getChainName())) return;
        if (redisRepository.hasKey(KeyConstant.KEY_START_APP)) {
            return;
        }
        redisRepository.set(KeyConstant.KEY_START_APP, 1, 30L, TimeUnit.SECONDS);
        log.info("启动链监测" + dto.getChainName());
        OkHttpClient mClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        //构建一个连接请求对象
        Request request = new Request.Builder().get().url("wss://" + dto.getUrl()).build();
        //开始连接
        mClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                log.info("onOpen" + dto.getChainName());
                addWs(dto.getChainName(), webSocket);
                subscribePending(webSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                parseAndProcessMessage(text, webSocket, dto);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                log.info("onClosed");
                removeWs(dto, code);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                StackTraceElement[] s = throwable.getStackTrace();
                for (int i = 0; i < s.length; i++) {
                    log.info("onFailure" + s[i].toString());
                }
                removeWs(dto, CODE_ERROR);
            }
        });
    }

    private void addWs(String chainName, WebSocket webSocket) {
        chainWsMap.put(chainName, webSocket);
    }

    private void removeWs(ChainRequestDto dto, int code) {
        WebSocket webSocket = chainWsMap.remove(dto.getChainName());
        if (code == CODE_ERROR) {
            if (webSocket == null) return;
            webSocket.close(CODE_ERROR, "fail");
        }
        // TODO 丢消息队列出去，重连WS
        commonProducer.sendReconnectChainWs(dto);
    }

    private void subscribePending(WebSocket webSocket) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("id", "subscribe");
        jsonObject.put("method", "eth_subscribe");
        JSONArray params = new JSONArray();
        params.add("newPendingTransactions");
        jsonObject.put("params", params);
        webSocket.send(jsonObject.toJSONString());
    }

    private void queryTxHash(WebSocket webSocket, String txHash) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("id", "getTransactionByHash");
        jsonObject.put("method", "eth_getTransactionByHash");
        JSONArray params = new JSONArray();
        params.add(txHash);
        jsonObject.put("params", params);
        webSocket.send(jsonObject.toJSONString());
    }

    private void parseAndProcessMessage(String text, WebSocket webSocket, ChainRequestDto dto) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        String method = jsonObject.getString("method");
        if (StringUtils.equalsIgnoreCase(method, "eth_subscription")) {
            processNewPendingTx(jsonObject, webSocket);
            return;
        }
        String id = jsonObject.getString("id");
        if (StringUtils.equalsIgnoreCase(id, "getTransactionByHash")) {
            processQueryTxResult(jsonObject, webSocket, dto);
            return;
        }
        if (id.startsWith(BlockChainServiceImpl.MY_TRIGGER_ACTION)) {
            // 代码触发合约
            processClamp(id.substring(BlockChainServiceImpl.MY_TRIGGER_ACTION.length()), jsonObject);
            return;
        }
        if (id.startsWith(BlockChainServiceImpl.MY_CONSTANT_ACTION)) {
            // 代码触发合约
            processWetherSend(dto.getChainName(), id.substring(BlockChainServiceImpl.MY_CONSTANT_ACTION.length()),
                    jsonObject, webSocket);
            return;
        }
    }

    private void processNewPendingTx(JSONObject jsonObject, WebSocket webSocket) {
        String pendingHash = jsonObject.getJSONObject("params").getString("result");
        queryTxHash(webSocket, pendingHash);
    }


    private void processQueryTxResult(JSONObject jsonObject, WebSocket webSocket, ChainRequestDto dto) {
        JSONObject result = jsonObject.getJSONObject("result");
        if (result == null) return;
        String hash = result.getString("hash");
        if (clampHashSet.remove(hash)) return;
        if (result == null || result.isEmpty()) return;
        String contractAddress = result.getString("to");
        if (StringUtils.isEmpty(contractAddress)) return;
        contractAddress = contractAddress.toLowerCase();
        // 非本系统监测的路由合约，跳过
        if (!redisRepository.hHasKey(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + dto.getChainName(), contractAddress)) return;
        String input = result.getString("input");
        ContractTriggerDto contractTriggerDto = blockChainService.parseInput(input);
        String receiveAddress = BlockChainUtil.getAddressFromParam(contractTriggerDto.getParams()[0]).toLowerCase();
//        log.info("合约swap信息={}", result.toJSONString());
        // 非买卖信息
        if (!StringUtils.equalsAnyIgnoreCase(contractTriggerDto.getMethod(), MethodConstant.PANCAKE_SWAP_BUY1, MethodConstant.PANCAKE_SWAP_BUY2)) return;

        Integer pathOffset = Integer.valueOf(contractTriggerDto.getParams()[2], 16) / 32;// path[] offset
        Integer pathLength = Integer.valueOf(contractTriggerDto.getParams()[pathOffset], 16);// path[] length
        String buyCoinContractAddress = BlockChainUtil.getAddressFromParam(contractTriggerDto.getParams()[pathOffset + pathLength]);// path[]最后一条是买入的币
        String payCoinContractAddress = BlockChainUtil.getAddressFromParam(contractTriggerDto.getParams()[pathOffset + 1]);// 支付的币种
        String stableCoins = redisRepository.hGetString(KeyConstant.KEY_ROUTER_2_ROBOT_CONTRACT_MAP + contractAddress, buyCoinContractAddress);
        // 不是此路由监测的买入币种合约 + 看此买入币的支持的稳定币合约
        if (StringUtils.isEmpty(stableCoins) || !stableCoins.contains(payCoinContractAddress)) return;

        BigInteger gasLimit = new BigInteger(result.getString("gas").substring(2), 16);
        BigInteger gasPrice = new BigInteger(result.getString("gasPrice").substring(2), 16);
        BigInteger i1 = new BigInteger(contractTriggerDto.getParams()[0], 16);// amountOut - 买入的币的精准数量
        BigInteger i2 = new BigInteger(contractTriggerDto.getParams()[1], 16);// amountInMax - 卖出的币的最大数量
        log.info("交易哈希={},以{}购买{}", hash, payCoinContractAddress, buyCoinContractAddress);
        // USDT或者BUSD买
        log.info("gasLimit={},gasPrice={},", gasLimit, gasPrice);
        if (StringUtils.equalsIgnoreCase(contractTriggerDto.getMethod(), MethodConstant.PANCAKE_SWAP_BUY1)) {
            log.info("最多只支付{}", i2);
            contractTriggerDto.getParams()[0] = BlockChainUtil.addZero(i1.divide(BigInteger.TEN).toString(16), 64);
            contractTriggerDto.getParams()[1] = BlockChainUtil.addZero(i2.divide(BigInteger.TEN).toString(16), 64);
        } else {
            log.info("支付固定数量{}", i1);
            contractTriggerDto.getParams()[0] = BlockChainUtil.addZero(i2.divide(BigInteger.TEN).toString(16), 64);
            contractTriggerDto.getParams()[1] = BlockChainUtil.addZero(i2.divide(BigInteger.TEN).toString(16), 64);
        }

//        String params1 = contractTriggerDto.getParams()[0];
//        String params2 = BlockChainUtil.addZero(BlockChainUtil.coinToParam(BigDecimal.valueOf(2), 16), 64);
//        // 待请求数据先暂存起来
//        TriggerSmartContractDto t = TriggerSmartContractDto.builder().chainId(dto.getChainId()).gasLimit(gasLimit.multiply(BigInteger.valueOf(2)))
//                .gasPrice(gasPrice.multiply(BigInteger.valueOf(5))).privateKey(dto.getPrivateKey()).contractAddress(contractAddress)
//                .method(MethodConstant.TRANSFER).params(params1 + params2).build();
//        String dataHash = MD5Util.MD5Encode(JSONObject.toJSONString(t));
//        waitTransferMap.put(dataHash, t);
        // 查下收款方有没有上级
        String s = blockChainService.getTriggerSmartContractData(dto.getChainId(), gasLimit, gasPrice.multiply(BigInteger.valueOf(2)), dto.getPrivateKey(), contractAddress,
                MethodConstant.PANCAKE_SWAP_BUY1, contractTriggerDto.getParams()[0], hash);
//        webSocket.send(s);
//        ClampRecordModelDto c = new ClampRecordModelDto();
//        c.setChainName(dto.getChainName());
//        c.setAddress(receiveAddress);
//        c.setContractAddress(t.getContractAddress());
//        clampMap.put(dataHash, c);
//
//        String d = blockChainService.getTriggerSmartContractData(t.getChainId(), t.getGasLimit(),
//                t.getGasPrice(), t.getPrivateKey(), t.getContractAddress(), t.getMethod(),
//                t.getParams(), dataHash);
//
//        recordHasClamp(dto.getChainName(), receiveAddress, t.getContractAddress());
//        webSocket.send(d);
//        log.info("夹子完成={}", receiveAddress);
//        log.info("查寻地址={}有无上级信息", receiveAddress);
    }

    private void processWetherSend(String chainName, String dataHash, JSONObject jsonObject, WebSocket webSocket) {
        TriggerSmartContractDto t = waitTransferMap.get(dataHash);
        String receiveAddress = BlockChainUtil.getAddressFromParam(t.getParams().substring(0, 64));

        log.info("查询有无上级结果={}", jsonObject.toJSONString());
        String data = jsonObject.getString("result");
        if (!StringUtils.equalsIgnoreCase(data, "0x0000000000000000000000000000000000000000000000000000000000000000")) {
            waitTransferMap.remove(dataHash);
            recordHasClamp(chainName, receiveAddress, t.getContractAddress());
            return;
        }

        ClampRecordModelDto c = new ClampRecordModelDto();
        c.setChainName(chainName);
        c.setAddress(receiveAddress);
        c.setContractAddress(t.getContractAddress());
        clampMap.put(dataHash, c);

        String d = blockChainService.getTriggerSmartContractData(t.getChainId(), t.getGasLimit(),
                t.getGasPrice(), t.getPrivateKey(), t.getContractAddress(), t.getMethod(),
                t.getParams(), dataHash);

        recordHasClamp(chainName, receiveAddress, t.getContractAddress());
        webSocket.send(d);

        log.info("夹子完成={}", receiveAddress);
    }

    private void recordHasClamp(String chainName, String receiveAddress, String contractAddress) {
        redisRepository.set(getClampRecordKey(chainName, receiveAddress, contractAddress), 1);
    }

    private String getClampRecordKey(String chainName, String receiveAddress, String contractAddress) {
        return KeyConstant.KEY_CLAMPED_CHAIN_ADDRESS + chainName + ":" + receiveAddress + ":" +
                contractAddress;
    }

    private void processClamp(String tHash, JSONObject jsonObject) {
        ClampRecordModelDto c = clampMap.get(tHash);
        String hash = jsonObject.getString("result");
        if (StringUtils.isEmpty(hash)) return;
        clampHashSet.add(hash);
        if (c == null) return;
        c.setHash(hash);
        commonProducer.sendInsertClampedData(c);
    }

}
