package com.liuqi.business.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.constant.ConfigConstant;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.constant.PublicNodeConstant;
import com.liuqi.business.dto.ChainRequestDto;
import com.liuqi.business.dto.ContractTriggerDto;
import com.liuqi.business.dto.QueryBalanceDto;
import com.liuqi.business.dto.SendTransactionResultDto;
import com.liuqi.business.service.BlockChainService;
import com.liuqi.business.service.ConfigService;
import com.liuqi.mq.TransactionProducer;
import com.liuqi.redis.RedisRepository;
import com.liuqi.response.ReturnResponse;
import com.liuqi.utils.BlockChainUtil;
import com.liuqi.utils.DESUtil;
import com.liuqi.utils.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Service()
@Slf4j
public class BlockChainServiceImpl implements BlockChainService {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private TransactionProducer transactionProducer;
    @Autowired
    private ConfigService configService;

    public static final String MY_TRIGGER_ACTION = "MyTriggerAction_";
    public static final String MY_CONSTANT_ACTION = "MyConstantAction_";


    @Override
    public String getTriggerSmartContractData(Long chainId, BigInteger gasLimit, BigInteger gasPrice, String privateKey,
                                              String contractAddress, String method, String params, String tHash) {
        log.info("拦截时间" + System.currentTimeMillis());
        String triggerAddress = Credentials.create(privateKey).getAddress();
        Long nonce = redisRepository.hincreone(KeyConstant.KEY_DETECT_ADDRESS_NONCE, triggerAddress);
//        Long nonce = 4L;
        String data = processRequest(chainId, privateKey,
                contractAddress, nonce, gasPrice, gasLimit, method, params, tHash);
        return data;
    }

    private String processRequest(Long chainId, String privateKey, String contractAddress, Long nonce,
                                  BigInteger gasPrice, BigInteger gasLimit, String method, String params, String tHash) {
        Credentials credentials = Credentials.create(privateKey);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.valueOf(nonce), gasPrice, gasLimit, contractAddress, getInput(method, params));
        byte[] transactionSign = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        String transactionData = Numeric.toHexString(transactionSign);

        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(transactionData);
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_sendRawTransaction");
        postJson.put("id", MY_TRIGGER_ACTION + tHash);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    private String getInput(String method, String params) {
        return "0x" + method + params;
    }

    @Override
    public ContractTriggerDto parseInput(String input) {
        input = input.substring(2);
        String method = input.substring(0, 8);
        String[] params = parseParams(input);
        return ContractTriggerDto.builder().method(method).params(params).build();
    }

    protected String[] parseParams(String data) {
        if (data == null || data.length() <= 8) return null;
        String params = data.substring(8);
        int paramCount = params.length() / 64;
        String[] param = new String[paramCount];
        for (int i = 0; i < paramCount; i++) {
            param[i] = params.substring(i * 64, i * 64 + 64);
        }
        return param;
    }

    @Override
    public String getTriggerConstantContractData(String address, String contractAddress, String method, String params,
                                                 String dataHash) {
        return getConstantData(address, contractAddress, method, params, dataHash);
    }

    private String getConstantData(String address, String contractAddress, String method, String params, String dataHash) {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        body.put("from", address);
        body.put("to", contractAddress);
        String data = "0x" + BlockChainUtil.getMethodByte(method) + params;
        body.put("data", data);
        array.add(body);
        array.add("latest");
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_call");
        postJson.put("id", MY_CONSTANT_ACTION + dataHash);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    @Override
    public ReturnResponse refreshNonce(String chainName) {
        ChainRequestDto dto = redisRepository.hGetModel(KeyConstant.KEY_DETECT_CHAIN_DATA, chainName);
        JSONObject postJson = new JSONObject();
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_getTransactionCount");
        postJson.put("id", "getTransactionCount");
        JSONArray array = new JSONArray();
        String address = Credentials.create(dto.getPrivateKey()).getAddress().toLowerCase();
        array.add(address);
        array.add("pending");
        postJson.put("params", array);
        String postData = postJson.toString();
        String s = HttpUtil.createPost("https://" + dto.getUrl()).body(postData).timeout(3000).execute().body();
        JSONObject r = JSONObject.parseObject(s);
        redisRepository.hset(KeyConstant.KEY_DETECT_ADDRESS_NONCE, address, Long.valueOf(r.getString("result").substring(2), 16) - 1);
        return ReturnResponse.backSuccess();
    }

    @Override
    public String[] getNewAddress(List<String> mnemonicCode, Integer index) {
        return null;
    }

    private BigDecimal getGasPriceRate() {
        String v = configService.queryValueByName(ConfigConstant.CONFIG_GAS_PRICE_RATE + "eth");
        return StringUtils.isEmpty(v) ? BigDecimal.ONE : new BigDecimal(v);
    }

    @Override
    public SendTransactionResultDto doTransaction(JSONObject jsonObject) {
        String address = jsonObject.getString("address");
        String privateKey = jsonObject.getString("privateKey");
        Long nonce = jsonObject.getLong("nonce");
        String contractAddress = jsonObject.getString("contractAddress");
        BigDecimal quantity = jsonObject.getBigDecimal("quantity");
        Integer decimal = jsonObject.getInteger("decimal");

        jsonObject.remove("privateKey");
        log.info("doTransaction = {}", jsonObject.toJSONString());

        SendTransactionResultDto s = new SendTransactionResultDto();
        s.setFromAddress(Credentials.create(privateKey).getAddress());
        s.setToAddress(address);
        try {
            String u1 = getNodeAddress();
            String r = HttpUtil.createPost(u1).body(generateGetGasData()).timeout(3000).execute().body();
            log.info(u1 + "|gas price - " + r);
            String gasPriceStr = JSONObject.parseObject(r).getString("result");

            BigDecimal g = new BigDecimal(new BigInteger(gasPriceStr.substring(2), 16));
            g = MathUtil.mul(g, getGasPriceRate());
            gasPriceStr = "0x" + Long.toHexString(g.longValue());

            BigDecimal gasPrice = new BigDecimal(Numeric.decodeQuantity(gasPriceStr));
            if (gasPrice.compareTo(BigDecimal.valueOf(100000)) < 0) {
                s.setSuccess(false);
                s.setErrorMsg("gas price is invalid");
                return s;
            }
            BigDecimal maxGas = BigDecimal.valueOf(0.001).multiply(BigDecimal.TEN.pow(18));
            long gasLimit = maxGas.divide(gasPrice, 1, BigDecimal.ROUND_DOWN).longValue();

            log.info("GasPrice = {}, GAS Limit = {}", gasPrice, gasLimit);
            if (gasLimit > 500000) gasLimit = 500000;

            String rr = HttpUtil.createPost(getNodeAddress()).body(generateSendCoinData(privateKey,
                    contractAddress, address, nonce, gasPriceStr, quantity, gasLimit, decimal)).timeout(3000).execute().body();

            JSONObject result = JSONObject.parseObject(rr);

            if (result.containsKey("error")) {
                String msg = result.getJSONObject("error").getString("message");
                s.setSuccess(false);
                s.setErrorMsg(msg);
            } else {
                String hash = result.getString("result");
                s.setSuccess(true);
                s.setHash(hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
            s.setSuccess(false);
            s.setErrorMsg(e.getLocalizedMessage());
        }
        return s;
    }

    private String getNodeAddress() {
        Integer index = redisRepository.getInteger("eth_node_index");
        String address = PublicNodeConstant.ETH_NODE_ADDRESS[index % PublicNodeConstant.ETH_NODE_ADDRESS.length];
        index++;
        if (index.compareTo(PublicNodeConstant.ETH_NODE_ADDRESS.length) >= 0) {
            index = 0;
        }
        redisRepository.set("eth_node_index", index);
        return address;
    }

    @Override
    public boolean processTxFail(String errorMsg, String params) {
        if (StringUtils.isEmpty(errorMsg)) return false;
        if (errorMsg.contains("gas too low")) {// ETH gas太低，把Gaslimit调大了再试
            return false;
        } else if (errorMsg.contains("latest version")) {// GETH版本问题，切换到下一个节点尝试
            log.info("GETH版本过低，重试");
            transactionProducer.sendTransaction(errorMsg.hashCode(), DESUtil.encryptBasedDes(params,
                    DESUtil.COMMON_PWD1 + DESUtil.COMMON_PWD2));
            return true;
        } else if (errorMsg.contains("gas price is invalid")) {// gas price过低
            log.info("GAS PRICE过低，重试");
            transactionProducer.sendTransaction(errorMsg.hashCode(), DESUtil.encryptBasedDes(params,
                    DESUtil.COMMON_PWD1 + DESUtil.COMMON_PWD2));
            return true;
        }
        return false;
    }

    @Override
    public void processBeforeTx(JSONObject params) {
        String privateKey = params.getString("privateKey");
        Long nonce = redisRepository.getLong(KeyConstant.KEY_ETH_NONCE + Credentials.create(privateKey).getAddress());
        params.put("nonce", nonce);
    }

    @Override
    public void processAfterTx(SendTransactionResultDto s, JSONObject params) {
        if (!s.isSuccess()) return;
        redisRepository.set(KeyConstant.KEY_ETH_NONCE + s.getFromAddress(),
                params.getLong("nonce") + 1);
    }

    @Override
    public void processAddressNonce(String airdropAddress, String extractAddress) {

    }

    @Override
    public QueryBalanceDto getBalance(JSONObject jsonObject) {
        Integer decimal = jsonObject.getInteger("decimal");
        String address = jsonObject.getString("address");
        String contractAddress = jsonObject.getString("contractAddress");
        try {
            String balanceData = HttpUtil.createPost(getNodeAddress()).
                    body(generateGetBalanecPostData(address, contractAddress)).timeout(3000).execute().body();
            JSONObject r = JSONObject.parseObject(balanceData);
            String balanceStr = r.getString("result");
            return QueryBalanceDto.builder().success(true).balance(decodeERC20Balance(balanceStr, decimal)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return QueryBalanceDto.builder().success(false).build();
        }
    }

    @Override
    public String getAddressFromPrivateKey(String privateKey) {
        return null;
    }

    @Override
    public String processPrivateKey(String privateKey) {
        if (!privateKey.startsWith("0x")) privateKey = "0x" + privateKey;
        return privateKey;
    }

    public BigDecimal decodeERC20Balance(String input, Integer decimal) {
        BigInteger b = new BigInteger(input.substring(2), 16);
        BigDecimal ba = new BigDecimal(b);
        BigDecimal r = ba.divide(BigDecimal.valueOf(10).pow(decimal), decimal, RoundingMode.DOWN);
        return r.stripTrailingZeros();
    }

    private String generateGetBalanecPostData(String address, String contractAddress) {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        body.put("from", address);
        body.put("to", contractAddress);
        String s1 = "000000000000000000000000";
        String data = "0x70a08231" + s1 + address.substring(2);
        body.put("data", data);
        array.add(body);
        array.add("latest");
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_call");
        postJson.put("id", 1);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    private String generateSendCoinData(String privateKey, String contractAddress, String toAddress, Long nonce,
                                        String gasPrice, BigDecimal value, Long gasLimit, int decimal) {
        Credentials credentials = Credentials.create(privateKey);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.valueOf(nonce),
                Numeric.decodeQuantity(gasPrice), BigInteger.valueOf(gasLimit),
                contractAddress, getTransferERC20Data(toAddress, value, decimal));
        byte[] transactionSign = TransactionEncoder.signMessage(rawTransaction, 1, credentials);
        String transactionData = Numeric.toHexString(transactionSign);

        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(transactionData);
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_sendRawTransaction");
        postJson.put("id", 1);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    private String getTransferERC20Data(String toAddress, BigDecimal value, int decimal) {
        String methodSign = "0xa9059cbb";
        String s1 = "000000000000000000000000";
        BigDecimal s2 = new BigDecimal(10);
        BigDecimal transferERC20Count = s2.pow(decimal).multiply(value);
        String da = transferERC20Count.toBigInteger().toString(16);
        int sl = da.length();
        for (int i = 0; i < (64 - sl); i++) {
            da = "0" + da;
        }
        String data = methodSign + s1 + toAddress.substring(2) + da;
        return data;
    }

    private String generateGetGasData() {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_gasPrice");
        postJson.put("id", 1);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    public static void main(String[] args) {
        String rr = HttpUtil.createPost("https://bsc.getblock.io/mainnet/?api_key=44fae5f3-2303-41c8-a0a4-865c8ae5e7c0").body(new BlockChainServiceImpl().getTriggerSmartContractData(56L, BigInteger.valueOf(25000), BigInteger.TEN.pow(9).multiply(BigInteger.valueOf(5)),
                "1b4f6153d1b2323964885bd7f11df1f96c182650acf2c5940a91f999354da735", "0x55d398326f99059ff775485246999027b3197955",
                "095ea7b3", "00000000000000000000000010ed43c718714eb63d5aa57b78b54704e256024e00000000000000000000000000000000000000000000001b1ae4d6e2ef500000", "sdfsdf")).timeout(3000).execute().body();
        System.out.println(rr);
    }

    public static void maisdfsfn(String[] args) {
        String address = "0x16d211f3be093d4b484054364471d68dd02a6d82";
        String contractAddress = "0x6d5b44925e1118b6caba47148ca78a8f8b69bbb2";
        String param1 = "000000000000000000000000000000000000000000000000000000000000008A";
//        String param1 = "00000000000000000000000016d211f3be093d4b484054364471d68dd02a6d82";
//        String param2 = "0000000000000000000000000000000000000000000000000000000000000040";
//        String param3 = "0000000000000000000000000000000000000000000000000000000000000002";
//        String param5 = "0000000000000000000000000000000000000000000000000000000000000000";
//        String param6 = "0000000000000000000000000000000000000000000000000000000000000000";


        String p = new BlockChainServiceImpl().getConstantData(address, contractAddress,
//                "getStaticUnitProfitByHeight(uint256)",
                "getDynamicUnitProfitByHeight(uint256)",
                param1, "sdfsfd");
//        System.out.println(p);

        String rr = HttpUtil.createPost("https://bsc.getblock.io/testnet/?api_key=44fae5f3-2303-41c8-a0a4-865c8ae5e7c0").
//        String rr = HttpUtil.createPost("https://data-seed-prebsc-1-s1.binance.org:8545").
//        String rr = HttpUtil.createPost("https://bsc-dataseed.binance.org").
        body(p).timeout(3000).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(rr);
        String result = jsonObject.getString("result");
        System.out.println("卡牌检测结果=" + new BigDecimal(new BigInteger(result.substring(2), 16)).divide(BigDecimal.TEN.pow(18)));

//        System.out.println(BlockChainUtil.getMethodByte("activeRecord(uint256,address[])"));

    }

    // 总数据条数得到总页数
    private int calculateTotalPages(int totalCount) {
        int totalPages = totalCount / 20;
        if (totalCount % 20 > 0) totalPages++;
        return totalPages;
    }

    private int getCountByPageNum(int pageNum, int totalPages, int totalCount) {
        int countPerPage = 20;
        if (pageNum >= totalPages) {
            countPerPage = totalCount - (pageNum - 1) * 20;
        }
        return countPerPage;
    }

}