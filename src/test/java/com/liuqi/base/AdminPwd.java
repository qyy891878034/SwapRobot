package com.liuqi.base;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author tanyan
 * @create 2019-11=29
 * @description
 */
public class AdminPwd {
    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";//加密方式
        Object crdentials = "111111";//密码原值
        Object salt = BaseConstant.TYPE_SYS+BaseConstant.BASE_PROJECT;//盐值
        int hashIterations = 67;//加密次
        Object result = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
        System.out.println(result);
    }
}
