package com.liuqi.business.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.dto.ChainRequestDto;
import com.liuqi.business.service.DetectService;
import com.liuqi.redis.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AppStartListener implements ApplicationRunner {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private DetectService detectService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<Object, Object> map = redisRepository.hmget(KeyConstant.KEY_DETECT_CHAIN_DATA);
        map.forEach((k, v) -> {
            ChainRequestDto c = (ChainRequestDto) v;
            detectService.startDetect(c);
        });
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(new BigInteger("7ccf433be4a64ba9f", 16))
                .divide(BigDecimal.TEN.pow(18)));
    }


}
