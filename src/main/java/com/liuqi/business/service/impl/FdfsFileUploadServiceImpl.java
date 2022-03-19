package com.liuqi.business.service.impl;

import com.liuqi.business.service.FileUploadService;
import com.liuqi.fastdfs.FastDFSClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description FastFDS文件上传
 * @Date 11:40 2020/11/25
 */
@Service("FDFS")
public class FdfsFileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FastDFSClientUtils fastDFSClientUtils;

    @Override
    public String upload(MultipartFile file) {
        return fastDFSClientUtils.uploadFile(file);
    }

}
