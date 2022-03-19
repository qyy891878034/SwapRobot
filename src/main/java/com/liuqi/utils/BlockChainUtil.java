package com.liuqi.utils;

import com.liuqi.business.constant.MethodConstant;
import org.web3j.crypto.Hash;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class BlockChainUtil {

    public static String coinToParam(BigDecimal real, Integer decimal) {
        return real.multiply(BigDecimal.TEN.pow(decimal)).toBigInteger().toString(16);
    }

    public static String getAddressFromParam(String param) {
        return "0x" + param.substring(24);
    }

    public static String getMethodByte(String method) {
        byte[] s = Hash.sha3(method.getBytes(StandardCharsets.UTF_8));
        String s1 = process(Integer.toHexString(s[0] & 0xFF));
        String s2 = process(Integer.toHexString(s[1] & 0xFF));
        String s3 = process(Integer.toHexString(s[2] & 0xFF));
        String s4 = process(Integer.toHexString(s[3] & 0xFF));
        return s1 + s2 + s3 + s4;
    }

    private static String process(String s) {
        if (s.length() < 2) return "0" + s;
        return s;
    }

    public static String addZero(String dt, int length) {
        StringBuilder builder = new StringBuilder();
        final int count = length;
        int zeroAmount = count - dt.length();
        for (int i = 0; i < zeroAmount; i++) {
            builder.append("0");
        }
        builder.append(dt);
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(Integer.valueOf("00000000000000000000000000000000000000000000000000000000000000a0", 16));
    }
}
