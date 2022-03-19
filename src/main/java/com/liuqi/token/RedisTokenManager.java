package com.liuqi.token;

import com.liuqi.base.BaseConstant;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.redis.RedisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {


    @Autowired
    private RedisRepository redisRepository;

    public String getBaseKey(){
        return KeyConstant.KEY_TOKEN+"_";
    }

    @Override
    public void refreshUserToken(String token) {
        String key= getBaseKey()+token;
        if (redisRepository.hasKey(key)) {
            redisRepository.expire(key, BaseConstant.TOKEN_SESSION_TIME, TimeUnit.MINUTES);
        }
    }

    /**
     * 设置值到session中
     * @param token
     * @param paramKey
     * @param paramValue
     * @return
     */
    public boolean setAttribute(String token, String paramKey,Object paramValue){
        String key= getBaseKey()+token;
        return redisRepository.hset(key, paramKey, paramValue, BaseConstant.TOKEN_SESSION_TIME, TimeUnit.MINUTES);
    }

    /**
     * 在session中获取值
     * @param token
     * @param paramKey
     * @return
     */
    public Object getAttribute(String token, String paramKey){
        String key= getBaseKey()+token;
        if (redisRepository.hasKey(key)) {
            return redisRepository.hget(key, paramKey);
        }
        return null;
    }

}
