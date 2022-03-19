package com.liuqi.aliyun;


import com.aliyun.oss.OSSClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Component
public class OssFileUploadUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger("OssFileUploadUtils");

    @Value("${aliyunpic.endpoint}")
    private String endpoint;
    @Value("${aliyunpic.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyunpic.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyunpic.accessRequestUrl}")
    private String accessRequestUrl;

    /**
     * 文件上传
     *
     * @param bytes
     * @param bucketName
     * @param path
     */
    public String upload(byte[] bytes, String bucketName, String path, String suffix ) {
        OSSClient ossClient = null;
        String fileRequestUrl = "";
        try {
            String fileName = UUID.randomUUID().toString() + "." + suffix ;
            if (StringUtils.isNotBlank(path)) {
                path = path.concat("/").concat(fileName);
            }
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName, path, new ByteArrayInputStream(bytes));
            fileRequestUrl = accessRequestUrl.concat("/").concat(path);
        } catch (Exception e) {
            //
            LOGGER.error("OssFileUploadUtils upload image error....");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return fileRequestUrl;
    }


}

