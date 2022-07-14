package com.liuqi.third.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信发送
 */
@Component
@Slf4j
public class SmsSender {


    /**
     * 发送短信
     * @param isChain
     * @param phone
     * @param message
     */
    public void sendSms(boolean isChain, String phone, String message) {
    }
}
