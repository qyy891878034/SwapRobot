package com.liuqi.utils;

/**
 *  根据IP地址获取详细的地域信息
 *  @project:personGocheck
 *  @class:AddressUtils.java
 *  @author:heguanhua E-mail:37809893@qq.com
 *  @date：Nov 14, 2012 6:38:25 PM
 */
public class UdunSignUtils {

    private static final String API_KEY = "";// API-KEY

    private static final String BASE_URL = "https://hk04-hk-node.uduncloud.com";// 节点地址

    public static final String CREATE_ADDRESS_URL = BASE_URL + "/mch/address/create";

    public static final String WITH_DRAW = BASE_URL + "/mch/withdraw";

    public static final String MERCHANT_ID = "";// 商户号

    public static String getSign(String body, long timestamp, long nonce) {
        String sign = MD5Util.MD5Encode(body + API_KEY + nonce + timestamp);
        return sign;
    }

    // 测试
    public static void main(String[] args) {
    }
}
