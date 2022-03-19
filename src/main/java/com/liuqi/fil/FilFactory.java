package com.liuqi.fil;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.constant.PublicNodeConstant;
import com.liuqi.redis.RedisRepository;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilFactory {

    private static final BigDecimal FIL_UNIT = BigDecimal.TEN.pow(18);

    //获取nonce
    private final static String NONCE = "Filecoin.MpoolGetNonce";
    //获取余额
    private final static String BALANCE = "Filecoin.WalletBalance";
    //获取交易费
    private final static String VALID_ADDRESS = "Filecoin.WalletValidateAddress";
    //广播
    private final static String PUSH = "Filecoin.MpoolPush";
    //当前区块高度
    private final static String BLOCK_HEIGHT = "Filecoin.ChainHead";
    //计算CAP
    private final static String GAS_ESTIMATE_FEECAP = "Filecoin.GasEstimateFeeCap";
    //计算Premium
    private final static String GAS_ESTIMATE_GASPREMIUM = "Filecoin.GasEstimateGasPremium";
    //计算gaslimit
    private final static String GAS_ESTIMATE_GAS_LIMIT = "Filecoin.GasEstimateGasLimit";

    @Autowired
    private RedisRepository redisRepository;

    private String getNodeAddress() {
        Integer index = redisRepository.getInteger("fil_node_index");
        String address = PublicNodeConstant.FIL_NODE_ADDRESS[index % PublicNodeConstant.FIL_NODE_ADDRESS.length];
        index++;
        if (index.compareTo(PublicNodeConstant.FIL_NODE_ADDRESS.length) >= 0) {
            index = 0;
        }
        redisRepository.set("fil_node_index", index);
        return address;
    }

    public String getBalance(String address) {
        String[] addr = {address};
        return getBody(BALANCE, addr);
    }

    public String checkAddress(String address) {
        String[] addr = {address};
        String body = getBody(VALID_ADDRESS, addr);
        if (body != null) {
            JSONObject jsonBody = JSONObject.parseObject(body);
            return jsonBody != null ? jsonBody.getString("result") : null;
        }
        return null;
    }

    public Long getNonce(String address) {
        String[] addr = {address};
        String body = getBody(NONCE, addr);
        if (body != null) {
            JSONObject jsonBody = JSONObject.parseObject(body);
            if (jsonBody != null) {
                return jsonBody.getLong("result");
            }
        }
        return 0L;
    }

    public Integer getBlockHeight() {
        List<JSONObject> jsonObjects = new ArrayList<>();
        //System.out.println("height=");
        String body = getBody(BLOCK_HEIGHT, jsonObjects);
        JSONObject jsonBody = JSONObject.parseObject(body);
        if (jsonBody != null) {
            String result = jsonBody.getString("result");
            if (result != null) {
                JSONObject object = JSONObject.parseObject(result);
                if (object != null) {
                    int height = Integer.parseInt(object.getString("Height"));
                    //System.out.println("height=" + height);
                    return height;
                }
            }
        }
        return 0;
    }

    public String getBlockCid() {
        List<JSONObject> jsonObjects = new ArrayList<>();
        String body = getBody(BLOCK_HEIGHT, jsonObjects);
        JSONObject jsonBody = JSONObject.parseObject(body);
        if (jsonBody != null) {
            String result = jsonBody.getString("result");
            if (result != null) {
                JSONObject object = JSONObject.parseObject(result);
                if (object != null) {
                    String cids = object.getString("Cids");
                    JSONArray jsonArray = JSONArray.parseArray(cids);
                    if (jsonArray != null) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if (jsonObject != null) {
                            return jsonObject.getString("/");
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getGasEstimateGasLimit(String gaslimit) {
        JSONObject jsonlimit = JSONObject.parseObject(gaslimit);
        List<Object> info = new ArrayList<>();
        info.add(jsonlimit);
        info.add(null);
        String body = getBody(info, GAS_ESTIMATE_GAS_LIMIT);
        if (body != null) {
            JSONObject jsonBody = JSONObject.parseObject(body);
            if (jsonBody != null) {
                String result = jsonBody.getString("result");
                if (result != null) {
                    return result;
                }
            }
        }
        return "0";
    }

    public String getGasEstimateGasPremium(String address, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("/", message);
        List<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        List<Object> info = new ArrayList<>();
        info.add(null);
        info.add(address);
        info.add(null);
        info.add(jsonObjects);
        String body = getBody(info, GAS_ESTIMATE_GASPREMIUM);
        if (body != null) {
            JSONObject jsonBody = JSONObject.parseObject(body);
            if (jsonBody != null) {
                String result = jsonBody.getString("result");
                if (result != null) {
                    return result;
                }
            }
        }
        return "0";
//        List<Object> info = new ArrayList<>();
//        info.add(5);
//        info.add(address);
//        info.add(maxGasLimit);
//        String body = getBody(info, GAS_ESTIMATE_GASPREMIUM);
//        if (body != null) {
//            JSONObject jsonBody = JSONObject.parseObject(body);
//            if (jsonBody != null) {
//                String result = jsonBody.getString("result");
//                if (result != null) {
//                    return result;
//                }
//            }
//        }
//        return "0";
    }

    public String getGasEstimateFeeCap(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        List<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        jsonObjects.add(null);
        jsonObjects.add(null);
        String body = getBody(GAS_ESTIMATE_FEECAP, jsonObjects);
        if (body != null) {
            JSONObject jsonBody = JSONObject.parseObject(body);
            if (jsonBody != null) {
                String result = jsonBody.getString("result");
                if (result != null) {
                    return result;
                }
            }
        }
        return "0";
    }

    public String push(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        List<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        return getBody(PUSH, jsonObjects);
    }

    private String getBody(List<Object> object, String method) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("method", method);
        jsonObject.put("params", object);
        jsonObject.put("id", 1);
        System.out.println("method = " + method + "|params = " + jsonObject.toJSONString());
        String result = HttpUtil.createPost(getNodeAddress())
                .body(jsonObject.toJSONString()).timeout(3000)
                .execute().body();
        System.out.println("method = " + method + "|getBody3 = " + result);
        return result;
//        HttpRequest httpRequest = HttpRequest.post(rpcConstant.getFIL_RPC_URL()).headerMap(getHeaderMap(), true);
        //httpRequest.setHttpProxy("127.0.0.1",7890);
//        return httpRequest.body(json).execute().body();
    }

    private String getBody(String method, List<JSONObject> object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("method", method);
        jsonObject.put("params", object);
        jsonObject.put("id", 1);
        System.out.println("method = " + method + "|params = " + jsonObject.toJSONString());
        String result = HttpUtil.createPost(getNodeAddress())
                .body(jsonObject.toJSONString()).timeout(5000)
                .execute().body();
        System.out.println("method = " + method + "|getBody1 = " + result);
        return result;
//        HttpRequest httpRequest = HttpRequest.post(rpcConstant.getFIL_RPC_URL()).headerMap(getHeaderMap(), true);
//        return httpRequest.body(json).execute().body();
    }

    private String getBody(String method, String[] params) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("method", method);
        jsonObject.put("params", params);
        jsonObject.put("id", 1);
        String result = HttpUtil.createPost(getNodeAddress()).body(jsonObject.toJSONString()).timeout(3000).execute().body();
//        String result = HttpUtil.createPost("https://1u3jyXk91eiWGLGZezqqvun2MPA:ddd0bf4c4d83d6284c1b40fe7cf84365@filecoin.infura.io")
//                .body(jsonObject.toJSONString())
//                .basicAuth("1u3jyXk91eiWGLGZezqqvun2MPA", "ddd0bf4c4d83d6284c1b40fe7cf84365")
//                .timeout(2000).execute().body();
        System.out.println("method = " + method + "|getBody2 = " + result);
        return result;
//        HttpRequest httpRequest = HttpRequest.post(rpcConstant.getFIL_RPC_URL()).headerMap(getHeaderMap(), true);
        //httpRequest.setHttpProxy("127.0.0.1",7890);
//        return httpRequest.body(json).execute().body();
    }

    public Map getHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("charset", "UTF-8");
//        headerMap.put("Authorization", "Bearer " + rpcConstant.getFIL_RPC_PASSWORD());
        return headerMap;
    }

    public String getGasEstimateFeeCap(String addrFrom, String addrTo, int nonce, String value, String cids) {
        JSONObject cid = new JSONObject();
        JSONObject message = new JSONObject();

        cid.put("/", cids);

        message.put("To", addrTo);
        message.put("From", addrFrom);
        message.put("Nonce", nonce);
        message.put("Value", value);
        message.put("GasLimit", 0);
        message.put("GasFeeCap", "0");
        message.put("GasPremium", "0");
        message.put("Method", 0);
        message.put("Params", "");
        message.put("CID", cid);

        return message.toJSONString();
    }
}
