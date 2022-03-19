package com.liuqi.mq;


import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.constant.MqConstant;
import com.liuqi.business.context.BlockChainContext;
import com.liuqi.business.dto.ChainRequestDto;
import com.liuqi.business.model.ClampRecordModelDto;
import com.liuqi.business.service.BlockChainService;
import com.liuqi.business.service.ClampRecordService;
import com.liuqi.business.service.DetectService;
import com.liuqi.mq.dto.EmailDto;
import com.liuqi.mq.dto.SmsDto;
import com.liuqi.third.email.AliEmailSender;
import com.liuqi.third.sms.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 消费者处理消息
 */
@Component
@Slf4j
public class Consumer {

    @Autowired
    private AliEmailSender aliEmailSender;
    @Autowired
    private SmsSender smsSender;
    @Autowired
    private ClampRecordService clampRecordService;
    @Autowired
    private DetectService detectService;

    /*****邮件***************************************************************************/
    @JmsListener(destination = MqConstant.MQ_DESTINATION_EMAIL)
    public void receiveEmail(String text) {
        EmailDto dto = JSONObject.parseObject(text, EmailDto.class);
        try {
            aliEmailSender.sendSimpleMail(dto.getEmail(), dto.getTitle(), dto.getMessage(), dto.getSign());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****短信***************************************************************************/
    @JmsListener(destination = MqConstant.MQ_DESTINATION_SMS)
    public void receiveSms(String text) {
        SmsDto dto = JSONObject.parseObject(text, SmsDto.class);
        try {
            smsSender.sendSms(dto.isChain(), dto.getPhone(), dto.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JmsListener(destination = MqConstant.MQ_INSERT_CLAMPED_DATA)
    public void receiveInsertClampedData(String text) {
        ClampRecordModelDto c = JSONObject.parseObject(text, ClampRecordModelDto.class);
        clampRecordService.insert(c);
    }

    @JmsListener(destination = MqConstant.MQ_RECONNECT_CHAIN_WS)
    public void receiveReconnectChainWs(String text) {
        ChainRequestDto c = JSONObject.parseObject(text, ChainRequestDto.class);
        detectService.startDetect(c);
    }


}
