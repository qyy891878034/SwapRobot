package com.liuqi.mq;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Map;

/**
 * 以太坊交易队列，因为要维护nonce，所以要采用队列
 */
@Service("transactionProducer")
public class TransactionProducer {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "queueTransaction1")
    private Destination queueTransaction1;
    @Resource(name = "queueTransaction2")
    private Destination queueTransaction2;
    @Resource(name = "queueQueryNonce")
    private Destination queueQueryNonce;

    public void sendTransaction(int hashCode, String data) {
        int i = hashCode % 2;
        jmsTemplate.convertAndSend(i == 0 ? queueTransaction1 : queueTransaction2, data);
    }

    public void sendNonceQuery(String serviceType, String airdropAddress, String extractAddress) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceType", serviceType);
        jsonObject.put("airdropAddress", airdropAddress);
        jsonObject.put("extractAddress", extractAddress);
        jmsTemplate.convertAndSend(queueQueryNonce, jsonObject.toJSONString());
    }
}
