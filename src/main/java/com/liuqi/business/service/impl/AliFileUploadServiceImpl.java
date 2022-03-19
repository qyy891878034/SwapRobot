package com.liuqi.business.service.impl;

import com.liuqi.aliyun.FileService;
import com.liuqi.business.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 阿里云OSS文件上传
 * @Date 11:40 2020/11/25
 */
@Service("ALI")
public class AliFileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileService fileService;

    @Override
    public String upload(MultipartFile file) {
        return fileService.uploadOss(file);
    }

}