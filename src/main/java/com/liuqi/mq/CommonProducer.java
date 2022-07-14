package com.liuqi.mq;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.model.ExchangeApiModelDto;
import com.liuqi.business.model.OrderRecordModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("commonProducer")
public class CommonProducer {
    private static final Logger log = LoggerFactory.getLogger(CommonProducer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

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

    @Resource(name = "queueInsertTransferRecord")
    private Destination queueInsertTransferRecord;

    @Resource(name = "queueReconnectChainWs")
    private Destination queueReconnectChainWs;

    @Resource(name = "queueQueryAddressNonce")
    private Destination queueQueryAddressNonce;

    @Resource(name = "queueInsertDetectCurrency")
    private Destination queueInsertDetectCurrency;

    @Resource(name = "queueFreshBuyMin")
    private Destination queueFreshBuyMin;

    @Resource(name = "queueInsertLootBuy")
    private Destination queueInsertLootBuy;

    @Resource(name = "queueInsertLootSell")
    private Destination queueInsertLootSell;

    @Resource(name = "queuePairCreateQuery")
    private Destination queuePairCreateQuery;

    @Resource(name = "queueTradeActionQuery")
    private Destination queueTradeActionQuery;

    @Resource(name = "queueLootBuyHashQuery")
    private Destination queueLootBuyHashQuery;

    @Resource(name = "queueLootSellCheckQuery")
    private Destination queueLootSellCheckQuery;

    @Resource(name = "queueLootSellHashQuery")
    private Destination queueLootSellHashQuery;

    @Resource(name = "queueLootApproveHashQuery")
    private Destination queueLootApproveHashQuery;

    public void sendPriceChangeMsg(Integer exchangeCode, String currencyName, BigDecimal price, BigDecimal money) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("exchangeCode", exchangeCode);
        jsonObject.put("currencyName", currencyName);
        jsonObject.put("price", price);
        jsonObject.put("money", money);
        long i = (currencyName.hashCode() / 4);
        if (i == 0) {
            this.jmsTemplate.convertAndSend(this.queueTriggerSmartContract1, jsonObject.toJSONString());
        } else if (i == 1) {
            this.jmsTemplate.convertAndSend(this.queueTriggerSmartContract2, jsonObject.toJSONString());
        } else if (i == 2) {
            this.jmsTemplate.convertAndSend(this.queueTriggerSmartContract3, jsonObject.toJSONString());
        } else {
            this.jmsTemplate.convertAndSend(this.queueTriggerSmartContract4, jsonObject.toJSONString());
        }
    }

    public void sendWssReconnect(Integer exchangeCode) {
        this.jmsTemplate.convertAndSend(this.queueInsertDetectCurrency, exchangeCode.toString());
    }

    public void sendSyncSpotInfo(Long exchangeApiId, String currencyName, String orderId, BigDecimal money) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("exchangeApiId", exchangeApiId);
        jsonObject.put("currencyName", currencyName);
        jsonObject.put("orderId", orderId);
        jsonObject.put("money", money);
        this.jmsTemplate.convertAndSend(this.queueFreshBuyMin, jsonObject.toJSONString());
    }

    public void sendSyncSwapOrder(ExchangeApiModelDto e) {
        this.jmsTemplate.convertAndSend(this.queuePairCreateQuery, JSONObject.toJSONString(e));
    }

    public void sendWarningMsg(Integer exchangeCode, String time, Integer action, String currencyName, String identifier) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("exchangeCode", exchangeCode);
        jsonObject.put("time", time);
        jsonObject.put("action", action);
        jsonObject.put("currencyName", currencyName);
        jsonObject.put("identifier", identifier);
        this.jmsTemplate.convertAndSend(this.queueTradeActionQuery, jsonObject.toJSONString());
    }

    public void sendCloseAllPosition(Long id) {
        this.jmsTemplate.convertAndSend(this.queueLootBuyHashQuery, id.toString());
    }

    public void sendInsertOrderRecord(OrderRecordModel o) {
        this.jmsTemplate.convertAndSend(this.queueLootApproveHashQuery, JSONObject.toJSONString(o));
    }

    public void sendClearOrInsertCurrency(Integer exchangeCode, String currencyName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("exchangeCode", exchangeCode);
        jsonObject.put("currencyName", currencyName);
        this.jmsTemplate.convertAndSend(this.queueLootSellCheckQuery, jsonObject.toJSONString());
    }

    public void sendModeToCrossMsg(Long apiId, String currencyName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currencyName", currencyName);
        jsonObject.put("apiId", apiId);
        this.jmsTemplate.convertAndSend(this.queueLootSellHashQuery, jsonObject.toJSONString());
    }

    public void sendModifyLeverMsg(Long apiId, Integer lever, String currencyName, String direction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("apiId", apiId);
        jsonObject.put("lever", lever);
        jsonObject.put("currencyName", currencyName);
        jsonObject.put("direction", direction);
        this.jmsTemplate.convertAndSend(this.queueQueryAddressNonce, jsonObject.toJSONString());
    }

}
