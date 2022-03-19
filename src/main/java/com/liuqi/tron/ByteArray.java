package com.liuqi.tron;

import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class ByteArray {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public ByteArray() {
    }

    public static String toHexString(byte[] data) {
        return data == null ? "" : Hex.toHexString(data);
    }

    public static byte[] fromHexString(String data) {
        if (data == null) {
            return EMPTY_BYTE_ARRAY;
        } else {
            if (data.startsWith("0x")) {
                data = data.substring(2);
            }

            if (data.length() % 2 == 1) {
                data = "0" + data;
            }

            return Hex.decode(data);
        }
    }

    public static long toLong(byte[] b) {
        return b != null && b.length != 0 ? (new BigInteger(1, b)).longValue() : 0L;
    }

    public static byte[] fromString(String str) {
        return str == null ? null : str.getBytes();
    }

    public static String toStr(byte[] byteArray) {
        return byteArray == null ? null : new String(byteArray);
    }

    public static byte[] fromLong(long val) {
        return ByteBuffer.allocate(8).putLong(val).array();
    }

    public static byte[] subArray(byte[] input, int start, int end) {
        byte[] result = new byte[end - start];
        System.arraycopy(input, start, result, 0, end - start);
        return result;
    }

}
