package com.liuqi.business.constant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.tron.AddressHelper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

public class KeyConstant {
    public static final String PLATFORM_IDENTIFIER = "b28af2e91953e07ebca71f927bc55896";

    public static final String KEY_TOKEN = "key:base:token:";

    public static final String KEY_CONFIG_NAME = "key:config:name:";

    public static final String KEY_USER_AUTH = "key:user:auth:";

    public static final String KEY_OPERATE_CONFIG_API = "key:operate:config:api:";

    public static final String KEY_FRESH_PRICE_FLAG = "key:fresh:price:flag:";

    public static final String KEY_OPEN_POSITION_WINDOW = "key:open:position:window:";

    public static final String KEY_MAX_PROFIT_WINDOW = "key:max:profit:window:";

    public static final String KEY_ADD_ORDER_WINDOW = "key:add:order:window:";

    public static final String LONG = "-LONG";
    public static final String SHORT = "-SHORT";

    public static final String OPEN = "OPEN";
    public static final String CLOSE = "CLOSE";

    public static final String KEY_API_LAST_OID_MAP = "key:api:last:oid:map";

    public static final String KEY_API_CURRENCY_LEVER_MAP = "key:api:currency:lever:map:";

    public static final String KEY_EXCHANGE_RANK_LIST = "key:exchange:rank:list:";

    public static final String KEY_API_LIST_CODE = "key:api:list:code:";

    public static final String KEY_SWAP_ROUTERS = "key:swap:routers:";

    public static final String KEY_API_ORDER_TIME_MAP = "key:api:order:time:map";

    public static final String KEY_API_HOLDING_MAP = "key:api:holding:map:";

    public static final String KEY_SWAP_PRICE_MAP = "key:swap:price:map:";
    public static final String KEY_SWAP_MONEY_MAP = "key:swap:money:map:";

    public static final String KEY_ALL_PRICE = "key:all:price:";

    public static final String KEY_ZB_PRICE = "key:zb:price";

    public static final String KEY_API_SPOT_MAP = "key:api:spot:map:";

    public static final String KEY_OPERATE_CONFIG_CODE = "key:operate:config:map:exchange:";

    public static final String KEY_API_ID = "key:api:id:";

    public static final String KEY_EXCHANGE_CURRENCY = "key:exchange:currency:";

    public static final String KEY_LOGIN_ERROR = "key:login:error:";

    public static final String KEY_CURRENCY_CONFIG = "key:currency:config:";

    public static final String KEY_SWAP_PROCESS_FLAG_MAP = "key:swap:process:flag:map";

    public static final String KEY_RECHARGE = "key:recharge:";

    public static final String KEY_RE_CONFIG = "key:config:re";

    public static final String KEY_RECHARGE_SEARCH_STOP = "key:search:stop";

    public static final String KEY_ADMIN_AUTH_ERROR = "key:admin:auth";

    public static final String KEY_QUERYING_PRICE_FLAG = "key:querying:price:map";

    public static final String KEY_TRANSFER_CONFIG_ADDRESS = "key:transfer:config:ca:";

    public static final String KEY_LOCKED_SUB_CONTRACT = "key:locked:sub:contract:";

    public static final String KEY_TRANSFER_CURRENCY_CONTRACT = "key:transfer:currency:contract:";

    public static final String KEY_TRANSFER_CURRENCY_ID = "key:transfer:currency:id:";

    public static final String KEY_TRANSFER_CURRENCY_FROM_MAP = "key:transfer:currency:from:map";

    public static final String KEY_CHAIN_ID_LOOT_CURRENCY_A_B = "key:chain:id:loot:currency:a:b:";

    public static final String KEY_LOOT_CURRENCY_ID = "key:loot:currency:id:";

    public static final String KEY_LOOT_CHECKING_FLAG = "key:loot:checking:flag:";

    public static final String KEY_LOOT_SWAP_TRADE_CREATED = "key:loot:swap:trade:created:";

    public static final String KEY_CONTRACT_DECIMAL = "key:contract:decimal:";

    public static final String KEY_PASSPHRASE_ID = "key:passphrase:id:";

    public static final String KEY_LOOT_BUY_ORDER_PROCESSING_MAP = "key:loot:buy:order:processing:map";

    public static final String KEY_FIL_GAS_FEEGAP = "key:fil:gas:feegap";

    public static final String KEY_FIL_GAS_PREMIUM = "key:fil:gas:premium";

    public static final String KEY_FIL_GAS_LIMIT = "key:fil:gas:limit";

    public static final String KEY_TRANSFER_ADDRESS_MAP = "key:transfer:address:map";

    public static final String KEY_GCT_GAS_LIMIT = "key:gct:gas:limit";

    private static String generateGetGasData1() {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_gasPrice");
        postJson.put("id", Integer.valueOf(1));
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    private static String generateCallData(String privateKey, String contractAddress, String data, Long nonce, String gasPrice, Long gasLimit) {
        Credentials credentials = Credentials.create(privateKey);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.valueOf(nonce.longValue()),
                Numeric.decodeQuantity(gasPrice), BigInteger.valueOf(gasLimit.longValue()), contractAddress, data);
        byte[] transactionSign = TransactionEncoder.signMessage(rawTransaction, 97L, credentials);
        String transactionData = Numeric.toHexString(transactionSign);
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(transactionData);
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_sendRawTransaction");
        postJson.put("id", Integer.valueOf(1));
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    public static void m4444444444444ain(String[] args) {
        System.out.println(BigInteger.valueOf(10L).pow(33).toString(16));
    }

    public static void main(String[] args) {
        String privateKey = "0x1890b2564fb9ded83a3e308a45a9d704fb50a5aa658c3a188b2e4b0b15cf8e34";
        String contractAddress = "0x7bc93fa1f5daa6cf484b21daf5e59835f442f2af";
        String url = "https://data-seed-prebsc-1-s1.binance.org:8545";
        Long nonce = Long.valueOf(18L);
        OkHttpClient mOkHttpClient = (new OkHttpClient.Builder()).readTimeout(3L, TimeUnit.SECONDS).writeTimeout(3L, TimeUnit.SECONDS).connectTimeout(150L, TimeUnit.SECONDS).build();
        try {
            RequestBody stringBody1 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                    generateGetGasData1());
            Request request1 = (new Request.Builder()).url(url).post(stringBody1).build();
            Response response1 = mOkHttpClient.newCall(request1).execute();
            String r = response1.body().string();
            System.out.println("|gas price - " + r);
            String gasPriceStr = JSONObject.parseObject(r).getString("result");
            BigDecimal gasPrice = new BigDecimal(Numeric.decodeQuantity(gasPriceStr));
            if (gasPrice.compareTo(BigDecimal.valueOf(100000L)) < 0)
                return;
            BigDecimal maxGas = BigDecimal.valueOf(0.005D).multiply(BigDecimal.TEN.pow(18));
            long gasLimit = maxGas.divide(gasPrice, 1, 1).longValue();
            System.out.println("GasPrice = " + gasPrice + ",GAS Limit = " + gasLimit);
            if (gasLimit > 5000000L)
                gasLimit = 5000000L;
            String data = "0xa9059cbb";
            data = data + "0000000000000000000000002c97612adaf4a2da300ae4579f5ffdf47d3016f2";
            data = data + "000000000000000000000000000000000000314dc6448d9338c15b0a00000000";
            RequestBody stringBody2 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                    generateCallData(privateKey, contractAddress, data, nonce, gasPriceStr, Long.valueOf(gasLimit)));
            Request request2 = (new Request.Builder()).url(url).post(stringBody2).build();
            OkHttpClient mOkHttpClient2 = (new OkHttpClient.Builder()).readTimeout(3L, TimeUnit.SECONDS).writeTimeout(3L, TimeUnit.SECONDS).connectTimeout(150L, TimeUnit.SECONDS).build();
            Response response2 = mOkHttpClient2.newCall(request2).execute();
            System.out.println(response2.isSuccessful());
            String rr = response2.body().string();
            System.out.println(rr);
            System.out.println(JSONObject.toJSONString(response2));
            JSONObject result = JSONObject.parseObject(rr);
            if (result.containsKey("error")) {
                String str = result.getJSONObject("error").getString("message");
            } else {
                String str = result.getString("result");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void masddsfsin(String[] args) {
        OkHttpClient mOkHttpClient = (new OkHttpClient.Builder()).readTimeout(3L, TimeUnit.SECONDS).writeTimeout(3L, TimeUnit.SECONDS).connectTimeout(150L, TimeUnit.SECONDS).build();
        RequestBody stringBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                generateGetBalanecPostData("0x74d4e25f4419acebefedb190afe7a899be0effa8"));
        Request request = (new Request.Builder()).url("https://bsc-dataseed.binance.org").post(stringBody).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            System.out.println(jsonObject.toJSONString());
            String ss = jsonObject.getString("result");
            System.out.println(new BigInteger(ss.substring(2), 16));
            System.out.println(new BigInteger("28c850b61f6178000", 16));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mainsdfsfd(String[] args) throws Exception {
        System.out.println(AddressHelper.toHexString("TMvj6pTguUnXoJ9jKmVBp8dHS4yE8XSuBU"));
        System.out.println(get("extract(uint256)"));
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
        if (s.length() < 2)
            return "0" + s;
        return s;
    }

    public static void main23424(String[] args) {
        int d = 2;
        BigDecimal s = BigDecimal.valueOf(2.080721D);
        BigDecimal day = s.divide(BigDecimal.valueOf(d), 18, 1);
        BigDecimal yearTotal = BigDecimal.ONE.add(day).pow(d).setScale(5, 1);
        System.out.println(yearTotal.subtract(BigDecimal.ONE));
    }

    public static void main1(String[] args) {
        int blockHeight = 0;
        BigInteger b = new BigInteger("19f376abacd4cc000", 16);
        BigDecimal bb = new BigDecimal(b);
        System.out.println(bb.divide(BigDecimal.TEN.pow(18), 18, 1));
    }

    private static String generateGetBalanecPostData(String contractAddress) {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject body = new JSONObject();
        body.put("to", contractAddress);
        String data = "0xd91bbc94";
        data = data + "0000000000000000000000001df1353c05bd5c66457109e32d90a178ac9b20ed";
        body.put("data", data);
        array.add(body);
        array.add("latest");
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_call");
        postJson.put("id", Integer.valueOf(1));
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }
}
