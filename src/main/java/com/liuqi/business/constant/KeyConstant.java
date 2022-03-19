package com.liuqi.business.constant;


import cn.hutool.core.util.HexUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.dto.QueryBalanceDto;
import com.liuqi.tron.AddressHelper;
import com.liuqi.utils.MathUtil;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class KeyConstant {

    /****缓存key*****************************************************************/
    public final static String KEY_TOKEN = "key:base:token:";
    public final static String KEY_CONFIG_NAME = "key:config:name:";//缓存配置文件key
    public final static String KEY_USER_AUTH = "key:user:auth:";//用户验证

    public final static String KEY_SMS_CONFIG = "key:sms:config:";//短信配置


    public final static String KEY_SLIDE = "key:slide";//轮播图


    // 监测链ID+链名称+WSS地址+夹子私钥
    public static final String KEY_DETECT_CHAIN_DATA = "key:detect:chain:data";
    // 监控的合约地址[HRC20/ERC20/BEP20.....]
    public static final String KEY_DETECT_CONTRACT_ADDRESS = "key:detect:contract:address:";
    // 发交易的Nonce
    public static final String KEY_DETECT_ADDRESS_NONCE = "key:detect:address:nonce";
    // 已投过的链 - 地址 - 合约
    public static final String KEY_CLAMPED_CHAIN_ADDRESS = "key:clamped:chain:address:";


    public static final String KEY_CTC_PRICE = "key:ctc:price:";
    public static final String KEY_CTC_NUM = "key:ctc:num:";
    public static final String KEY_OTC_NUM = "key:otc:num:";
    public static final String KEY_WORK_NUM = "key:work:num:";


    //撮合交易
    public static final String KEY_ALL_PRICE = "key:all:price:";

    public static final String KEY_ZB_PRICE = "key:zb:price";//中币价格


    public final static String KEY_WORKTYPE = "key:worktype";//工单类型
    public final static String KEY_WORKTYPE_Id = "key:worktype:id:";//工单类型
    public final static String KEY_ZONE = "key:zone";//区号

    public final static String KEY_LOGIN_ERROR = "key:login:error:";
    public final static String KEY_VERSION_CONFIG = "key:version:config:";//版本


    public final static String KEY_BLOCK = "key:block:";
    public final static String KEY_BLOCK_ERROR = "key:block:error:";
    public final static String KEY_RECHARGE = "key:recharge:";
    public final static String KEY_RE_CONFIG = "key:config:re";

    public static final String KEY_RECHARGE_SEARCH_STOP = "key:search:stop";

    public static final String KEY_ADMIN_AUTH_ERROR = "key:admin:auth";

    public static final String KEY_AIR_DROP_PRI = "key:air:drop:";// 空投总地址私钥
    public static final String KEY_ETH_NONCE = "key:eth:address:nonce:";// ETH总地的nonce数
    public static final String KEY_FIL_NONCE = "key:fil:address:nonce:";// FIL地址的nonce数
    public static final String KEY_GCT_NONCE = "key:gct:address:nonce:";// GCT总地的nonce数


    public static final String KEY_A_P = "key:user:ap:";// FIL地址的nonce数


    public static final String KEY_COLLECT_TOTAL_ADDRESS = "key:collect:total:address:";// 汇总总地址


    public static final String KEY_EXTRACT_PRI = "key:extract:private:key:";// 提币总地址私钥[分币种]

    public static final String KEY_NODE_ADDRESS = "key:node:address:";// 节点地址

    public static final String KEY_CONTRACT_DECIMAL = "key:contract:decimal:";// 合约地址的精度

    public static final String KEY_PASSPHRASE_ID = "key:passphrase:id:";// 助记词记录

    public static final String KEY_ETH_GAS_LIMIT = "key:eth:gas:limit";// ETH燃气数量 - ETH交易使用

    public static final String KEY_FIL_GAS_FEEGAP = "key:fil:gas:feegap";// FIL用户选择支付的总手续费率[要大于premium]210000[attoFIL]
    public static final String KEY_FIL_GAS_PREMIUM = "key:fil:gas:premium";// FIL用户选择支付给矿工的手续费率205000[attoFIL]
    public static final String KEY_FIL_GAS_LIMIT = "key:fil:gas:limit";// FIL完成这笔交易真实消耗的Gas量2300000

    public static final String KEY_PRI_IP_PORT = "key:pri:ip:port";// 请求coinbase2的内网ip和port,ext http://127.0.0.2:8082

    public static final String KEY_GCT_GAS_LIMIT = "key:gct:gas:limit";// GCT燃气数量 - GCT交易使用

    private static String generateGetGasData1() {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_gasPrice");
        postJson.put("id", 1);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    private static String generateCallData(String privateKey, String contractAddress, String data, Long nonce,
                                           String gasPrice, Long gasLimit) {
        org.web3j.crypto.Credentials credentials = Credentials.create(privateKey);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.valueOf(nonce),
                Numeric.decodeQuantity(gasPrice), BigInteger.valueOf(gasLimit),
                contractAddress, data);
        byte[] transactionSign = TransactionEncoder.signMessage(rawTransaction, 97, credentials);
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

    public static void m4444444444444ain(String[] args) {
        System.out.println(BigInteger.valueOf(10).pow(33).toString(16));
    }

    public static void main(String[] args) {
//        String privateKey = "1890b2564fb9ded83a3e308a45a9d704fb50a5aa658c3a188b2e4b0b15cf8e34";
        String privateKey = "0x1890b2564fb9ded83a3e308a45a9d704fb50a5aa658c3a188b2e4b0b15cf8e34";
//        String privateKey = "0x34bcfd81efeb44c90aba505eaab4339bcc7d64da6bc26d099af4085f97064c65";
//        String contractAddress = "0x74d4e25f4419acebefedb190afe7a899be0effa8";// Mainnet
//        String contractAddress = "0x8600929b0b77a83b6ec8f6ac94046ee41a190b20";// Testnet
        String contractAddress = "0x7bc93fa1f5daa6cf484b21daf5e59835f442f2af";

//        String url = "https://bsc-dataseed.binance.org";// Mainnet
        String url = "https://data-seed-prebsc-1-s1.binance.org:8545";
//        String url = "https://http-mainnet-node.huobichain.com";// Mainnet

        Long nonce = 18L;
//        String url = "https://data-seed-prebsc-1-s1.binance.org:8545";// Testnet

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(150, TimeUnit.SECONDS)//设置连接超时时间
                .build();


        try {
            RequestBody stringBody1 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                    generateGetGasData1());
            okhttp3.Request request1 = new okhttp3.Request.Builder().
                    url(url).post(stringBody1).build();
            Response response1 = mOkHttpClient.newCall(request1).execute();
            String r = response1.body().string();
            System.out.println("|gas price - " + r);
            String gasPriceStr = JSONObject.parseObject(r).getString("result");

            BigDecimal gasPrice = new BigDecimal(Numeric.decodeQuantity(gasPriceStr));
            if (gasPrice.compareTo(BigDecimal.valueOf(100000)) < 0) {
                return;
            }
            BigDecimal maxGas = BigDecimal.valueOf(0.005).multiply(BigDecimal.TEN.pow(18));
            long gasLimit = maxGas.divide(gasPrice, 1, BigDecimal.ROUND_DOWN).longValue();

            System.out.println("GasPrice = " + gasPrice + ",GAS Limit = " + gasLimit);
            if (gasLimit > 5000000) gasLimit = 5000000;

            String data = "0xa9059cbb";
            data += "0000000000000000000000002c97612adaf4a2da300ae4579f5ffdf47d3016f2";
            data += "000000000000000000000000000000000000314dc6448d9338c15b0a00000000";

            RequestBody stringBody2 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                    generateCallData(privateKey, contractAddress, data, nonce, gasPriceStr, gasLimit));
            okhttp3.Request request2 = new okhttp3.Request.Builder().
                    url(url).post(stringBody2).build();
            OkHttpClient mOkHttpClient2 = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(150, TimeUnit.SECONDS)//设置连接超时时间
                    .build();
            Response response2 = mOkHttpClient2.newCall(request2).execute();
            System.out.println(response2.isSuccessful());

            String rr = response2.body().string();
            System.out.println(rr);
            System.out.println(JSONObject.toJSONString(response2));
            JSONObject result = JSONObject.parseObject(rr);

            if (result.containsKey("error")) {
                String msg = result.getJSONObject("error").getString("message");
            } else {
                String hash = result.getString("result");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void masddsfsin(String[] args) {
//        System.out.println(new BigInteger("26cdd83f3e9160", 16));
//        try {
//            String balanceData = HttpUtil.createPost("https://http-mainnet-node.huobichain.com").
//                    body(generateGetBalanecPostData("0x1df1353c05bd5c66457109e32d90a178ac9b20ed",
//                            "0x0x19d054836192200c71eec12bc9f255b1fae8ee80")).execute().body();
//            System.out.println(balanceData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(150, TimeUnit.SECONDS)//设置连接超时时间
                .build();

//        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
//        RequestBody stringBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
//                generateGetBalanecPostData("0x55d398326f99059ff775485246999027b3197955"));

        RequestBody stringBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                generateGetBalanecPostData("0x74d4e25f4419acebefedb190afe7a899be0effa8"));
        //0x8600929B0b77A83b6Ec8f6aC94046eE41a190B20

//        RequestBody stringBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
//                generateGetBalanecPostData("0xcb1984386e2b7ac67c515a963dd7ac59864fd270"));



        okhttp3.Request request = new okhttp3.Request.Builder().
                url("https://bsc-dataseed.binance.org").post(stringBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            System.out.println(jsonObject.toJSONString());
            String ss = jsonObject.getString("result");
//            BigDecimal d = new BigDecimal(new BigInteger(ss.substring(2), 16)).divide(BigDecimal.TEN.pow(18));
            System.out.println(new BigInteger(ss.substring(2), 16));
            System.out.println(new BigInteger("28c850b61f6178000", 16));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mainsdfsfd(String[] args) throws Exception {
//        System.out.println(AddressHelper.addressHexToBase58("417a7f9c10bb670e0795b4bb94f24092217c9574bb"));
        System.out.println(AddressHelper.toHexString("TMvj6pTguUnXoJ9jKmVBp8dHS4yE8XSuBU"));


//        System.out.println(get("transferFrom(address,address,uint256)"));
//        System.out.println(get("setApprovalForAll(address,bool)"));
//        System.out.println(get("publish(uint256,uint256)"));
//        System.out.println(get("cancel(uint256)"));
        System.out.println(get("extract(uint256)"));

//        System.out.println(Long.valueOf("f240", 16));
    }

    private static String get(String method) {
        byte[] s = Hash.sha3(method.getBytes(StandardCharsets.UTF_8));
        String s1 = s(Integer.toHexString(s[0] & 0xFF));
        String s2 = s(Integer.toHexString(s[1] & 0xFF));
        String s3 = s(Integer.toHexString(s[2] & 0xFF));
        String s4 = s(Integer.toHexString(s[3] & 0xFF));
        return "0x" + s1 + s2 + s3 + s4;
    }

    private static String s(String s) {
        if (s.length() < 2) return "0" + s;
        return s;
    }

    public static void main23424(String[] args) {
        int d = 2;
        BigDecimal s = BigDecimal.valueOf(2.080721);
        BigDecimal day = s.divide(BigDecimal.valueOf(d), 18, BigDecimal.ROUND_DOWN);
        BigDecimal yearTotal = BigDecimal.ONE.add(day).pow(d).setScale(5, BigDecimal.ROUND_DOWN);
        System.out.println(yearTotal.subtract(BigDecimal.ONE));
    }

    public static void main1(String[] args) {
        int blockHeight = 0;
        BigInteger b = new BigInteger("19f376abacd4cc000", 16);
        BigDecimal bb = new BigDecimal(b);
        System.out.println(bb.divide(BigDecimal.TEN.pow(18), 18, BigDecimal.ROUND_DOWN));


//        JSONObject params = new JSONObject();
//        params.put("jsonrpc", "2.0");
//        params.put("method", "eth_getTransactionReceipt");
//        JSONArray array = new JSONArray();
//        array.add("0xb090ce31f8166cad02c817d2b2f0f7c82958a190544cc533b129f443c991efcc");
//        params.put("params", array);
//        params.put("id", "1");
//        String data = HttpUtil.createPost("https://bsc-dataseed.binance.org").body(params.toJSONString()).execute().body();
//        System.out.println(data);
    }

    private static String generateGetBalanecPostData(String contractAddress) {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        body.put("to", contractAddress);
        String data = "0xd91bbc94";
        data += "0000000000000000000000001df1353c05bd5c66457109e32d90a178ac9b20ed";
//        data += "0000000000000000000000005cded5308ca099eb2ee58ef86882ab731f8a4459";
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

}
