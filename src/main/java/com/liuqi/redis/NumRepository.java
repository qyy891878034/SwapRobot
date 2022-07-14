package com.liuqi.redis;

import com.liuqi.business.constant.KeyConstant;
import com.liuqi.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NumRepository {

    @Autowired
    private RedisRepository redisRepository;

}
