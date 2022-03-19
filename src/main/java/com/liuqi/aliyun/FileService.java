/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.liuqi.aliyun;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Comparator;

/**
 * Service - 文件
 *
 * @author SHOP++ Team
 * @version 3.0
 */
@Service
public class FileService implements ServletContextAware {

    /**
     * 目标扩展名
     */
    private static final String DEST_EXTENSION = "jpg";

    /**
     * 目标文件类型
     */
    private static final String DEST_CONTENT_TYPE = "image/jpeg";

    /**
     * 阿里云prodcut bucket
     */
    public static final String PRODUCT_BUCKET = "luqiyun";

    public static final String PRODUCT_BUCKET_PATH = "image";
    public static final String FILE_BUCKET_PATH = "file";

    /**
     * servletContext
     */
    private ServletContext servletContext;

    @Autowired
    private OssFileUploadUtils ossFileUploadUtils;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    public String uploadOss(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            return ossFileUploadUtils.upload(multipartFile.getBytes(), PRODUCT_BUCKET, FILE_BUCKET_PATH, suffix);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class NameComparator implements Comparator<FileInfo> {
        public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
            return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getName(), fileInfos2.getName()).toComparison();
        }
    }

    private class SizeComparator implements Comparator<FileInfo> {
        public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
            return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getSize(), fileInfos2.getSize()).toComparison();
        }
    }

    private class TypeComparator implements Comparator<FileInfo> {
        public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
            return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(FilenameUtils.getExtension(fileInfos1.getName()), FilenameUtils.getExtension(fileInfos2.getName())).toComparison();
        }
    }

}