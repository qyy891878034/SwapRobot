package com.liuqi.business.listener;

import com.liuqi.business.context.ExchangeContext;
import com.liuqi.business.enums.ExchangeEnum;
import com.liuqi.business.model.CurrencyModel;
import com.liuqi.business.model.CurrencyModelDto;
import com.liuqi.business.service.CurrencyService;
import com.liuqi.redis.RedisRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartListener implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(com.liuqi.business.listener.AppStartListener.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeContext exchangeContext;

    @Autowired
    private RedisRepository redisRepository;

    public void run(ApplicationArguments args) throws Exception {
        log.info("程序启动");
        if (this.redisRepository.hasKey("app:start"))
            return;
        this.redisRepository.set("app:start", Integer.valueOf(1), 30L, TimeUnit.SECONDS);
        List<CurrencyModelDto> l = currencyService.queryListByDto(null, false);
        Map<Integer, List<CurrencyModelDto>> m = l.stream().collect(Collectors.groupingBy(CurrencyModel::getExchangeCode));
//        m.forEach((k, v) -> exchangeContext.getExchangeService(ExchangeEnum.getName(k).getIdentify()).initWebsocket(v));
    }
}
