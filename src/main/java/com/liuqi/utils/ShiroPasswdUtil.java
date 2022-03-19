package com.liuqi.utils;

import com.liuqi.base.BaseConstant;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.Arrays;

/**
 * @author tanyan
 * @create 2019-11=29
 * @description
 */
public class ShiroPasswdUtil {

    public static String getUserPwd(String orginPwd) {
        String slat = BaseConstant.TYPE_USER;
        return getBasePwd(slat, orginPwd);
    }

    public static String getSysPwd(String orginPwd) {
        String slat = BaseConstant.TYPE_SYS + BaseConstant.BASE_PROJECT;
        return getBasePwd(slat, orginPwd);
    }

    public static String getAdminPwd(String orginPwd) {
        String slat = BaseConstant.TYPE_ADMIN + BaseConstant.BASE_PROJECT;
        return getBasePwd(slat, orginPwd);
    }

    public static String getBasePwd(String slat, String orginPwd) {
        String hashAlgorithmName = "MD5";//加密方式
        int hashIterations = BaseConstant.PWD_COUNT;//加密次
        return new SimpleHash(hashAlgorithmName, orginPwd, slat, hashIterations).toString();
    }

    public static void main(String[] args) {
        System.out.println(getAdminPwd("111111"));
    }

    /**
     * 收到上链事件
     */
    public void uploadProAuth(){
        Web3j web3j = Web3j.build(new HttpService("http://localhost:8545"));
    }
}
