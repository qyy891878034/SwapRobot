package com.liuqi.business.context;

import com.liuqi.business.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileUploadContext {

    private static final String TYPE_FDFS = "FDFS";// FastDFS文件上传
    private static final String TYPE_ALI = "ALI";// 阿里云OSS文件上传

    public static final String TYPE_DEFAULT = TYPE_ALI;// 默认文件上传类型

    /**
     * 使用线程安全的ConcurrentHashMap存储所有实现FileUploadService接口的Bean
     * key:beanName
     * value：实现FileUploadService接口Bean
     */
    private final Map<String, FileUploadService> strategyMap = new ConcurrentHashMap<>();

    /**
     * 注入所有实现了FileUploadService接口的Bean
     *
     * @param strategyMap
     */
    @Autowired
    public FileUploadContext(Map<String, FileUploadService> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v) -> this.strategyMap.put(k, v));
    }

    public FileUploadService getFileUploadService(String type) {
        return strategyMap.get(type);
    }

}
