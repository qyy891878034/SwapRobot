package com.liuqi.tron;

import com.liuqi.tron.common.crypto.Sha256Sm3Hash;
import com.liuqi.tron.common.utils.Base58;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class WalletApi {

    public static byte[] decodeFromBase58Check(String addressBase58) {
        if (StringUtils.isEmpty(addressBase58)) {
            System.out.println("Warning: Address is empty !!");
            return null;
        }
        byte[] address = decode58Check(addressBase58);
        if (!addressValid(address)) {
            return null;
        }
        return address;
    }

    private static byte[] decode58Check(String input) {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
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
        }
        return null;
    }

    public static boolean addressValid(byte[] address) {
        if (ArrayUtils.isEmpty(address)) {
            System.out.println("Warning: Address is empty !!");
            return false;
        }
        if (address.length != 21) {
            System.out.println(
                    "Warning: Address length need "
                            + 21
                            + " but "
                            + address.length
                            + " !!");
            return false;
        }
        byte preFixbyte = address[0];
        if (preFixbyte != (byte) 0x41) {
            System.out.println(
                    "Warning: Address need prefix with "
                            + (byte) 0x41
                            + " but "
                            + preFixbyte
                            + " !!");
            return false;
        }
        // Other rule;
        return true;
    }


    public static void main(String[] args) {
        ECKey eCkey = ECKey.fromPrivate(ByteArray.fromHexString("6b2cd864a4ce8bb5acb33817f9dfe6a32f3e18179c200fdcb2a472be3a17109c"));
        byte[] addressBytes = eCkey.getAddress();
        byte[] hash0 = Sha256Sm3Hash.hash(addressBytes);
        byte[] hash1 = Sha256Sm3Hash.hash(hash0);
        byte[] inputCheck = new byte[addressBytes.length + 4];
        System.arraycopy(addressBytes, 0, inputCheck, 0, addressBytes.length);
        System.arraycopy(hash1, 0, inputCheck, addressBytes.length, 4);
        String s = Base58.encode(inputCheck);
        System.out.println(s);
    }
}
