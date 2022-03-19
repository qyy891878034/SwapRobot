package com.liuqi.mq;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.mq.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;

/**
 * 生产者 发送一个消息到消息队列
 */
@Service("emailProducer")
public class EmailProducer {

    @Autowired
    private JmsTemplate jmsTemplate ;
    @Resource(name = "queueEmail")
    private Destination queueEmail;

    /**
     * 发布消息
     * @param dto   是否取消
     */
    public void sendMessage(EmailDto dto){
        jmsTemplate.convertAndSend(queueEmail,JSONObject.toJSONString(dto));
    }
}
