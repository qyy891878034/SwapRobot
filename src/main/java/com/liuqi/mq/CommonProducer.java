package com.liuqi.mq;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.dto.ChainRequestDto;
import com.liuqi.business.model.ClampRecordModelDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.Topic;
import java.math.BigDecimal;

/**
 * 合约单  生产者
 */
@Service("commonProducer")
@Slf4j
public class CommonProducer {

    @Autowired
    private JmsTemplate jmsTemplate;
    // 队列
    @Resource(name = "queueTriggerSmartContract1")
    private Destination queueTriggerSmartContract1;
    @Resource(name = "queueTriggerSmartContract2")
    private Destination queueTriggerSmartContract2;
    @Resource(name = "queueTriggerSmartContract3")
    private Destination queueTriggerSmartContract3;
    @Resource(name = "queueTriggerSmartContract4")
    private Destination queueTriggerSmartContract4;

    @Resource(name = "queueInsertClampedData")
    private Destination queueInsertClampedData;
    @Resource(name = "queueReconnectChainWs")
    private Destination queueReconnectChainWs;

    /** 处理委托单数据 **/
    public void sendTrusteeOrderMsg(){
        jmsTemplate.convertAndSend(queueInsertClampedData, "");
    }

    public void sendInsertClampedData(ClampRecordModelDto d){
        jmsTemplate.convertAndSend(queueInsertClampedData, JSONObject.toJSONString(d));
    }

    public void sendReconnectChainWs(ChainRequestDto d){
        jmsTemplate.convertAndSend(queueReconnectChainWs, JSONObject.toJSONString(d));
    }

}
