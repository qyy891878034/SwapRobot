package com.liuqi.business.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    String upload(MultipartFile file);

}
