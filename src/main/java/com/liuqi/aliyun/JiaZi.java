package com.liuqi.aliyun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okio.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class JiaZi {

    private Set<String> set = new HashSet<>();

    public void s() {
        OkHttpClient mClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        String url = "wss://bsc.getblock.io/mainnet/?api_key=44fae5f3-2303-41c8-a0a4-865c8ae5e7c0";
        //构建一个连接请求对象
        Request request = new Request.Builder().get().url(url).build();
        //开始连接
        WebSocket websocket = mClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                System.out.println("onOpen");
                subscribePending(webSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                parseAndProcessMessage(text, webSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                System.out.println("onMessage2");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("onClosed");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
                super.onFailure(webSocket, throwable, response);
                System.out.println("onFailure" + throwable);
            }
        });
    }

    private void subscribePending(WebSocket webSocket) {
        webSocket.send("{\"jsonrpc\":\"2.0\", \"id\": 1, \"method\": \"eth_subscribe\", \"params\": [\"newPendingTransactions\"]}");
    }

    private void queryTxHash(WebSocket webSocket, String txHash) {
        webSocket.send("{\"jsonrpc\":\"2.0\", \"id\": \"getTransactionByHash\", \"method\": \"eth_getTransactionByHash\", \"params\": [\"" + txHash + "\"]}");
    }

    private void parseAndProcessMessage(String text, WebSocket webSocket) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        String method = jsonObject.getString("method");
        if (StringUtils.equalsIgnoreCase(method, "eth_subscription")) {
            String pendingHash = jsonObject.getJSONObject("params").getString("result");
            queryTxHash(webSocket, pendingHash);
        } else {
            String id = jsonObject.getString("id");
            if (StringUtils.equalsIgnoreCase(id, "getTransactionByHash")) {
                JSONObject result = jsonObject.getJSONObject("result");
                if (result == null || result.isEmpty()) {
                    return;
                }
                String to = result.getString("to");
                if (StringUtils.equalsIgnoreCase(to, "0x6dceb2e9a1b54fb7557f5df75da6c015030db656")) {
                    String hash = result.getString("hash");
                    String input = result.getString("input");
                    Long gasLimit = Long.valueOf(result.getString("gas").substring(2), 16);
                    BigInteger gasPrice = new BigInteger(result.getString("gasPrice").substring(2), 16);
                    System.out.println("hash=" + hash + ",input=" + input + ",gasLimit=" + gasLimit + ",gasPrice=" + gasPrice);
                    if (!StringUtils.equalsIgnoreCase(result.getString("from"), "0x1DF1353C05BD5c66457109e32D90A178aC9B20ed")) {
                        robotTransfer(webSocket, gasPrice, gasLimit);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new JiaZi().s();
    }

    private void robotTransfer(WebSocket webSocket, BigInteger originGasPrice, Long originGasLimit) {
        System.out.println("拦截时间" + System.currentTimeMillis());
        String privateKey = "1890b2564fb9ded83a3e308a45a9d704fb50a5aa658c3a188b2e4b0b15cf8e34";
        String contractAddress = "0x6dceb2e9a1b54fb7557f5df75da6c015030db656";
        try {
//            String url = "https://bsc-dataseed.binance.org";
            String url = "https://bsc.getblock.io/mainnet/?api_key=44fae5f3-2303-41c8-a0a4-865c8ae5e7c0";
            System.out.println("原交易燃气单价 - " + originGasPrice);
            BigInteger robotGasPrice = originGasPrice.multiply(BigInteger.valueOf(5));


            String params1 = addZero("0x8E4808A68C87c05DF1c169545ec0044f034BD886".substring(2), 64);
            String params2 = addZero(coinToParam(BigDecimal.ONE, 6), 64);

            String p = processRequest(privateKey,
                    contractAddress, 131L, "0x" + robotGasPrice.toString(16), originGasLimit * 2, "transfer(address,uint256)", params1 + params2);
            System.out.println("p=" + p);

            webSocket.send(p);
            System.out.println("拦截发送" + System.currentTimeMillis());
//            String rr = HttpUtil.createPost(url).body(p).timeout(3000).execute().body();
//            System.out.println("交易结果 = " + rr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String coinToParam(BigDecimal real, Integer decimal) {
        return real.multiply(BigDecimal.TEN.pow(decimal)).toBigInteger().toString(16);
    }

    private String processRequest(String privateKey, String contractAddress, Long nonce,
                                  String gasPrice, Long gasLimit, String method, String params) {
        Credentials credentials = Credentials.create(privateKey);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.valueOf(nonce),
                Numeric.decodeQuantity(gasPrice), BigInteger.valueOf(gasLimit),
                contractAddress, getInput(method, params));
        byte[] transactionSign = TransactionEncoder.signMessage(rawTransaction, 56L, credentials);
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

    private String getInput(String method, String params) {
        return "0x" + getMethodByte(method) + params;
    }

    public String getMethodByte(String method) {
        byte[] s = Hash.sha3(method.getBytes(StandardCharsets.UTF_8));
        String s1 = process(Integer.toHexString(s[0] & 0xFF));
        String s2 = process(Integer.toHexString(s[1] & 0xFF));
        String s3 = process(Integer.toHexString(s[2] & 0xFF));
        String s4 = process(Integer.toHexString(s[3] & 0xFF));
        return s1 + s2 + s3 + s4;
    }

    private String process(String s) {
        if (s.length() < 2) return "0" + s;
        return s;
    }

    private String addZero(String dt, int length) {
        StringBuilder builder = new StringBuilder();
        final int count = length;
        int zeroAmount = count - dt.length();
        for (int i = 0; i < zeroAmount; i++) {
            builder.append("0");
        }
        builder.append(dt);
        return builder.toString();
    }


}
