package com.liuqi.tron;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.liuqi.tron.common.crypto.Sha256Sm3Hash;
import com.liuqi.tron.common.utils.Base58;
import com.liuqi.tron.core.config.Parameter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 地址创建器
 *
 * @author: sunlight
 * @date: 2020/7/24 16:37
 */
public class AddressHelper {

    public static void main2(String[] args) throws Exception {
//        System.out.println(Long.parseLong("60364380", 16));
//        System.out.println(Long.parseLong("b5e19e", 16));
//        System.out.println("a9059cbb0000000000000000000000004f0af218b19973c1f8b6367ab86287128b5855b100000000000000000000000000000000000000000000000000000000002dc6c0".length());
//        System.out.println(addressHexToBase58("41d538bae59d07577f4b159fed50a421d7d45305b9"));
//        BigInteger b = new BigInteger("00000000000000000000000000000000000000000000000000000000002dc6c0", 16);
//        System.out.println(b);

//        JSONObject params = new JSONObject();
//        params.put("num", 27926428);
//        String blockData = HttpUtil.createPost( "http://47.57.240.228:8090/wallet/getblockbynum").body(params.toJSONString()).execute().body();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
//        jsonObject.put("method", "eth_blockNumber");
        jsonObject.put("method", "eth_getBlockByNumber");
        jsonObject.put("id", "1");
        JSONArray p = new JSONArray();
        p.add("0x72EDA7");
        p.add(true);
        jsonObject.put("params", p);
        String blockData = HttpUtil.createPost("http://47.57.242.21:8545").body(jsonObject.toJSONString()).timeout(3000).execute().body();
        System.out.println(blockData);
    }

    public static void main(String[] args) throws Exception {
//        String s = "a9059cbb0000000000000000000000004f0af218b19973c1f8b6367ab86287128b5855b100000000000000000000000000000000000000000000000000000000002dc6c0";
//        System.out.println(Long.parseLong("72eda7", 16));
//        System.out.println(addressHexToBase58("41" + s.substring(32, 72)));

        System.out.println(toHexString("TX8mKRD7CCvxh98rg7MdCGjtjSuHw9LSZg"));
//        System.out.println(addressHexToBase58("0000000000000000000000000000000000000000000000000000000000000002"));


//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("jsonrpc", "2.0");
//        jsonObject.put("method", "eth_getTransactionReceipt");
//        jsonObject.put("id", "1");
//        JSONArray p = new JSONArray();
//        p.add("0xa7991a7ceed9f3c900720384dda3116146f42745604d5e076820bc37625f32f6");
//        jsonObject.put("params", p);
//        String blockData = HttpUtil.createPost("http://47.57.242.21:8545").body(jsonObject.toJSONString()).execute().body();
//        System.out.println(blockData);
    }

    public static String privateKeyToBase58Address(String privateKey) {
        ECKey eCkey = ECKey.fromPrivate(ByteArray.fromHexString(privateKey));
        byte[] addressBytes = eCkey.getAddress();
        return addressBytesEncode58Check(addressBytes);
    }

    public static String toHexString(String base58Address) throws Exception {
        return ByteArray.toHexString(decodeFromBase58Check(base58Address));
    }

    public static ByteString toByteString(String base58Address) throws Exception {
        byte[] addressBytes = AddressHelper.decodeFromBase58Check(base58Address);
        return ByteString.copyFrom(addressBytes);
    }

    /**
     * 对地址执行Base58编码
     *
     * @param addressBytes 地址字节数组
     * @return 地址
     */
    public static String addressBytesEncode58Check(byte[] addressBytes) {
        byte[] hash0 = Sha256Sm3Hash.hash(addressBytes);
        byte[] hash1 = Sha256Sm3Hash.hash(hash0);
        byte[] inputCheck = new byte[addressBytes.length + 4];
        System.arraycopy(addressBytes, 0, inputCheck, 0, addressBytes.length);
        System.arraycopy(hash1, 0, inputCheck, addressBytes.length, 4);
        return Base58.encode(inputCheck);
    }

    /**
     * 地址Base58解码成字节数组
     *
     * @param addressBase58 地址Base58格式
     * @return 字节数组
     */
    public static byte[] decodeFromBase58Check(String addressBase58) throws Exception {
        byte[] address = decode58Check(addressBase58);
        if (addressValid(address)) {
            return address;
        } else {
            throw new Exception("Invalid address");
        }
    }

    public static String addressHexToBase58(String addressHex) {
        byte[] toBytes = ByteArray.fromHexString(addressHex);
        return addressBytesEncode58Check(toBytes);
    }

    /**
     * Base58解码
     *
     * @param input Base58字符串
     * @return 字节数组
     */
    private static byte[] decode58Check(String input) throws Exception {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            throw new Exception("invalid input");
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Sha256Sm3Hash.hash(decodeData);
        byte[] hash1 = Sha256Sm3Hash.hash(hash0);
        if (hash1[0] == decodeCheck[decodeData.length]
                && hash1[1] == decodeCheck[decodeData.length + 1]
                && hash1[2] == decodeCheck[decodeData.length + 2]
                && hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        } else {
            throw new Exception("invalid input");
        }
    }

    /**
     * 地址校验
     *
     * @param address 地址Base58 解码后的字节数组
     * @return 校验结果
     */
    private static boolean addressValid(byte[] address) {
        if (address.length != Parameter.CommonConstant.ADDRESS_SIZE) {
            return false;
        }
        byte prefixByte = address[0];
        return prefixByte == Parameter.CommonConstant.ADD_PRE_FIX_BYTE_MAINNET;
    }

}
