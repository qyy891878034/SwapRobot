package com.liuqi.third.email;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 阿里邮件
 */
@Component
@Slf4j
public class AliEmailSender {

    public static final String ACCESSKEY="LTAIcEGDd7mVQJNd";
    public static final String ACCESSSECRET="XYEXcVi3Sk6YIacHuBdevvHmzzrwny";

    public void sendSimpleMail(String to,String subject,String content,String sign) {
    }
}
