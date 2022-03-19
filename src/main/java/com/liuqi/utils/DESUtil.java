package com.liuqi.utils;

import com.alibaba.fastjson.JSONObject;
import org.web3j.crypto.Credentials;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DESUtil {

    public static final String COMMON_PWD1 = "LiQi";
    public static final String COMMON_PWD2 = "&^%*";

    /**
     * 数据加密，算法（DES）
     *
     * @param data
     *            要进行加密的数据
     * @return 加密后的数据
     */
    public static String encryptBasedDes(String data, String pwd) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(pwd.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密，并把字节数组编码成字符串
            encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
//            log.error("加密错误，错误信息：", e);  加密后UpSbNP/AeM5AfGnWB21HZQ== UpSbNP/AeM5AfGnWB21HZQ==
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * 解密
     * @param cryptData
     * @return
     */
    public static String decryptBasedDes(String cryptData, String pwd) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(pwd.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 把字符串解码为字节数组，并解密
            decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
        } catch (Exception e) {
//            log.error("解密错误，错误信息：", e);
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

    public static void main(String[] args) {
//        JSONObject j = new JSONObject();
//        j.put("dfdsf1", "555");
//        j.put("dfdsf2", 1);
//        j.put("dfdsf3", 1);
//        j.put("dfdsf4", 1);
//        j.put("dfdsf5", 1);
//        j.put("dfdsf6", 1);
//        j.put("dfdsf7", 1);
//        j.put("dfdsf8", 1);
//        j.put("dfdsf9", 1);
//        j.put("dfdsf9444", 1);



        // DES数据加密
//        String s1=encryptBasedDes(str);
        String s1=encryptBasedDes("0x658ad8fc932d13c62dfc090b510f731eba0cab6b5a80d64451a5cded623f70f4",
                COMMON_PWD1 + "222222");
//
        System.out.println(Credentials.create("0x658ad8fc932d13c62dfc090b510f731eba0cab6b5a80d64451a5cded623f70f4").getAddress());

        // DES数据解密
//        String s2=decryptBasedDes("2TLW6Bx+LiGWIohlYOTiVRMbbCK76BBbP3iF2ZKypPQOXLUbR0WI0jhwkEvMVU4PDDs6i1OlzKuj" +
//                "mvUBidmmGS6NzOm0jdqV", COMMON_PWD1 + "222222");
//
//        System.err.println("解密后"+s2);
    }

}
