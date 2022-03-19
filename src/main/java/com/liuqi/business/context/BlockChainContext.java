package com.liuqi.business.context;

import com.liuqi.business.service.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BlockChainContext {

    /**
     * 使用线程安全的ConcurrentHashMap存储所有实现FileUploadService接口的Bean
     * key:beanName
     * value：实现FileUploadService接口Bean
     */
    private final Map<String, BlockChainService> strategyMap = new ConcurrentHashMap<>();

    /**
     * 注入所有实现了FileUploadService接口的Bean
     *
     * @param strategyMap
     */
    @Autowired
    public BlockChainContext(Map<String, BlockChainService> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v) -> this.strategyMap.put(k, v));
    }

    public BlockChainService getBlockChainService(String type) {
        return strategyMap.get(type);
    }

}
