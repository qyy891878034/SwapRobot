package com.liuqi.redis;

import com.liuqi.business.constant.KeyConstant;
import com.liuqi.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NumRepository {
    @Autowired
    private RedisRepository redisRepository;

    /**
     * 获取下一个编码
     * @return
     */
    public String getWorkCode() {
        String prefix = DateTimeUtils.currentDate("yyMMdd");
        String key = prefix + KeyConstant.KEY_WORK_NUM;
        Long num = redisRepository.incrOne(key);
        return prefix + num;
    }
}
