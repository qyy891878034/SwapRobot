package com.liuqi.base;

import com.liuqi.spring.SpringUtil;
import com.liuqi.token.RedisTokenManager;

import javax.servlet.http.HttpServletRequest;

public class LoginUserTokenHelper {


    private static RedisTokenManager redisTokenManager;


    public static RedisTokenManager getRedisTokenManager() {
        if(redisTokenManager==null){
            redisTokenManager= (RedisTokenManager) SpringUtil.getBean("redisTokenManager");
        }
        return redisTokenManager;
    }

    public static String getToken(HttpServletRequest request){
        return request.getHeader(BaseConstant.TOKEN_NAME);
    }

}
