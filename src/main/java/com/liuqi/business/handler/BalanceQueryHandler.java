package com.liuqi.business.handler;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.context.BlockChainContext;
import com.liuqi.business.enums.ContractEnum;
import com.liuqi.business.enums.ProtocolEnum;
import com.liuqi.redis.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
public class BalanceQueryHandler {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private BlockChainContext blockChainContext;

}
