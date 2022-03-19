package com.liuqi.fil;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.HexUtil;
import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.UnsignedInteger;
import com.alibaba.fastjson.JSONObject;
import org.bitcoinj.crypto.ChildNumber;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import ove.crypto.digest.Blake2b;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

public class FilecoinSign {

    public static byte[] CID_PREFIX = new byte[]{0x01, 0x71, (byte) 0xa0, (byte) 0xe4, 0x02, 0x20};

    public static final ChildNumber FIL_HARDENED = new ChildNumber(461, true);

    public static SignData transaction(String fromAddress, String toAddress, Long nonce, String value, Long gasLimit,
                                       String gasFeeCap, String gasPremium) {
        //构建交易结构体
        byte[] from = getByte(fromAddress);
        byte[] to = getByte(toAddress);
        SignData signData = new SignData();
        signData.setVersion(new UnsignedInteger(0));
        signData.setTo(new ByteString(to));
        signData.setFrom(new ByteString(from));
        signData.setNonce(new UnsignedInteger(nonce));
        ByteString valueByteString;
        if (new BigInteger(value).toByteArray()[0] != 0) {
            byte[] byte1 = new byte[new BigInteger(value).toByteArray().length + 1];
            byte1[0] = 0;
            System.arraycopy(new BigInteger(value).toByteArray(), 0, byte1, 1, new BigInteger(value).toByteArray().length);
            valueByteString = new ByteString(byte1);
        } else {
            valueByteString = new ByteString(new BigInteger(value).toByteArray());
        }

        signData.setValue(valueByteString);
        signData.setGasLimit(new UnsignedInteger(gasLimit));

        ByteString gasFeeCapString;
        if (new BigInteger(gasFeeCap).toByteArray()[0] != 0) {
            byte[] byte2 = new byte[new BigInteger(gasFeeCap).toByteArray().length + 1];
            byte2[0] = 0;
            System.arraycopy(new BigInteger(gasFeeCap).toByteArray(), 0, byte2, 1
                    , new BigInteger(gasFeeCap).toByteArray().length);
            gasFeeCapString = new ByteString(byte2);
        } else {
            gasFeeCapString = new ByteString(new BigInteger(gasFeeCap).toByteArray());
        }
        signData.setGasFeeCap(gasFeeCapString);

        ByteString gasGasPremium;
        if (new BigInteger(gasPremium).toByteArray()[0] != 0) {
            byte[] byte2 = new byte[new BigInteger(gasPremium).toByteArray().length + 1];
            byte2[0] = 0;
            System.arraycopy(new BigInteger(gasPremium).toByteArray(), 0, byte2, 1
                    , new BigInteger(gasPremium).toByteArray().length);
            gasGasPremium = new ByteString(byte2);
        } else {
            gasGasPremium = new ByteString(new BigInteger(gasPremium).toByteArray());
        }

        signData.setGasPremium(gasGasPremium);

        signData.setMethodNum(new UnsignedInteger(0));
        signData.setParams(new ByteString(new byte[0]));
        return signData;
    }

    public static String signTransaction(String fromAddress, String toAddress, Long nonce, String value, Long gasLimit,
                                         String gasFeeCap, String gasPremium, String privateKey) {
        SignData signData = transaction(fromAddress, toAddress, nonce, value, gasLimit, gasFeeCap, gasPremium);
        System.out.println("signData = " + JSONObject.toJSONString(signData));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new CborEncoder(baos).encode(new CborBuilder()
                    .addArray()
                    .add(signData.getVersion())
                    .add(signData.getTo())
                    .add(signData.getFrom())
                    .add(signData.getNonce())
                    .add(signData.getValue())
                    .add(signData.getGasLimit())
                    .add(signData.getGasFeeCap())
                    .add(signData.getGasPremium())
                    .add(signData.getMethodNum())
                    .add(new ByteString(new byte[]{}))
                    .end()
                    .build());
            byte[] encodedBytes = baos.toByteArray();
            byte[] cidHashBytes = getCidHash(encodedBytes);
            return sign(cidHashBytes, privateKey);
        } catch (CborException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getCidHash(byte[] message) {
        byte[] messageByte = Blake2b.Digest.newInstance(32).digest(message);
        int xlen = CID_PREFIX.length;
        int ylen = messageByte.length;
        byte[] result = new byte[xlen + ylen];

        System.arraycopy(CID_PREFIX, 0, result, 0, xlen);
        System.arraycopy(messageByte, 0, result, xlen, ylen);

        byte[] prefixByte = Blake2b.Digest.newInstance(32).digest(result);
        String prefixByteHex = Numeric.toHexString(prefixByte).substring(2);
        System.out.println("prefixByteHex=" + prefixByteHex);
        return prefixByte;
    }

    public static String sign(byte[] cidHash, String privateKey) {
        ECKeyPair ecKeyPair = ECKeyPair.create(HexUtil.decodeHex(privateKey));
        Sign.SignatureData signatureData = Sign.signMessage(cidHash, ecKeyPair, false);
        byte[] sig = getSignature(signatureData);
        String base64 = Base64Encoder.encode(sig);
        return base64;
    }

    private static byte[] getSignature(Sign.SignatureData signatureData) {
        byte[] sig = new byte[65];
        System.arraycopy(signatureData.getR(), 0, sig, 0, 32);
        System.arraycopy(signatureData.getS(), 0, sig, 32, 32);
        sig[64] = (byte) ((signatureData.getV()[0] & 0xFF) - 27);
        return sig;
    }

    public static byte[] getByte(String addressStr) {
        String str = addressStr.substring(2);
        byte[] bytes12 = new byte[21];
        bytes12[0] = 1;
        System.arraycopy(Base32.decode(str), 0, bytes12, 1, 20);
        return bytes12;
    }

}
