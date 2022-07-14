package com.liuqi.mq;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.base.BaseModel;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.constant.MqConstant;
import com.liuqi.business.context.ExchangeContext;
import com.liuqi.business.enums.ExchangeEnum;
import com.liuqi.business.enums.ProductTypeEnum;
import com.liuqi.business.model.CurrencyModel;
import com.liuqi.business.model.CurrencyModelDto;
import com.liuqi.business.model.ExchangeApiModelDto;
import com.liuqi.business.model.OrderRecordModel;
import com.liuqi.business.service.CurrencyService;
import com.liuqi.business.service.ExchangeApiService;
import com.liuqi.business.service.OrderRecordService;
import com.liuqi.business.service.TaskService;
import com.liuqi.jobtask.OrderSyncJob;
import com.liuqi.jobtask.WssReconnectJob;
import com.liuqi.mq.dto.EmailDto;
import com.liuqi.mq.dto.SmsDto;
import com.liuqi.redis.RedisRepository;
import com.liuqi.third.email.AliEmailSender;
import com.liuqi.third.sms.SmsSender;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.liuqi.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private AliEmailSender aliEmailSender;
    @Autowired
    private SmsSender smsSender;
    @Autowired
    private ExchangeApiService exchangeApiService;
    @Autowired
    private OrderRecordService orderRecordService;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private ExchangeContext exchangeContext;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CurrencyService currencyService;

    @JmsListener(destination = MqConstant.MQ_DESTINATION_EMAIL)
    public void receiveEmail(String text) {
        EmailDto dto = JSONObject.parseObject(text, EmailDto.class);
        try {
            aliEmailSender.sendSimpleMail(dto.getEmail(), dto.getTitle(), dto.getMessage(), dto.getSign());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public void receiveInsertClampedData(String text) {}

    @JmsListener(destination = MqConstant.MQ_INSERT_TRANSFER_RECORD)
    public void receiveInsertTransferRecord(String text) {}

    @JmsListener(destination = MqConstant.MQ_INSERT_LOOT_BUY)
    public void receiveInsertLootBuy(String text) {}

    @JmsListener(destination = MqConstant.MQ_INSERT_LOOT_SELL)
    public void receiveInsertLootSell(String text) {}

    @JmsListener(destination = MqConstant.MQ_PAIR_CREATE_QUERY)
    public void receivePairCreateQuery(String text) {
        ExchangeApiModelDto e = JSONObject.parseObject(text, ExchangeApiModelDto.class);
        String l = redisRepository.hGetString(KeyConstant.KEY_API_LAST_OID_MAP, e.getId().toString());
        log.info("同步已平仓订单，记录id={},上次订单号={}", e.getId(), l);
        exchangeContext.getExchangeService(ExchangeEnum.getName(e.getExchangeCode()).getIdentify())
                .syncHistorySwapOrder(e, l);
    }

    @JmsListener(destination = MqConstant.MQ_TRADE_ACTION_QUERY)
    public void receiveWarningMsg(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        Integer exchangeCode = jsonObject.getInteger("exchangeCode");
        String time = jsonObject.getString("time");
        Integer action = jsonObject.getInteger("action");
        String currencyName = jsonObject.getString("currencyName");
        String identifier = jsonObject.getString("identifier");
        this.exchangeApiService.processPlatformSwapOrder(exchangeCode, time, action, currencyName, identifier);
    }

    @JmsListener(destination = MqConstant.MQ_LOOT_BUY_HASH_QUERY)
    public void receiveLootBuyHashQuery(String text) {
        Long id = Long.valueOf(text);
        Map map = new HashMap();
        map.put("id", id);
        taskService.delete(OrderSyncJob.class.getName(), "OrderSyncJob-" + id);
        taskService.addDelayedJob(OrderSyncJob.class.getName(), "OrderSyncJob-" + id,
                "平仓订单查询-" + id, DateUtil.getDatePlusSeconds(new Date(), 20), map);
    }

    @JmsListener(destination = MqConstant.MQ_LOOT_SELL_CHECK_QUERY)
    public void receiveLootSellCheckQuery(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        Integer exchangeCode = jsonObject.getInteger("exchangeCode");
        if (exchangeCode.compareTo(0) <= 0) {
            currencyService.clear();
            return;
        }
        String currencyName = jsonObject.getString("currencyName");
        CurrencyModel c = new CurrencyModel();
        c.setExchangeCode(exchangeCode);
        c.setCurrencyName(currencyName);
        currencyService.insert(c);
    }

    @JmsListener(destination = MqConstant.MQ_LOOT_SELL_HASH_QUERY)
    public void receiveLootSellHashQuery(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        String currencyName = jsonObject.getString("currencyName");
        Long apiId = jsonObject.getLong("apiId");
        ExchangeApiModelDto e = exchangeApiService.getById(apiId);
        exchangeContext.getExchangeService(ExchangeEnum.getName(e.getExchangeCode()).getIdentify()).switchCross(e, currencyName);
    }

    @JmsListener(destination = MqConstant.MQ_LOOT_APPROVE_HASH_QUERY)
    public void receiveLootApproveHashQuery(String text) {
        orderRecordService.insert(JSONObject.parseObject(text, OrderRecordModel.class));
    }

    @JmsListener(destination = MqConstant.MQ_RECONNECT_CHAIN_WS)
    public void receiveReconnectChainWs(String text) {}

    @JmsListener(destination = MqConstant.MQ_QUERY_ADDRESS_NONCE)
    public void receiveQueryAddressNonce(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        String currencyName = jsonObject.getString("currencyName");
        String direction = jsonObject.getString("direction");
        Long apiId = jsonObject.getLong("apiId");
        Integer newLever = jsonObject.getInteger("lever");
        Integer oldLever = redisRepository.hGetInteger(KeyConstant.KEY_API_CURRENCY_LEVER_MAP + apiId, currencyName + direction);
        if (oldLever.compareTo(newLever) == 0) return;
        redisRepository.hset(KeyConstant.KEY_API_CURRENCY_LEVER_MAP + apiId, currencyName + direction, newLever);
    }

    @JmsListener(destination = MqConstant.MQ_INSERT_DETECT_ADDRESS)
    public void receiveInsertDetectAddress(String text) {
        Integer exchangeCode = Integer.valueOf(text);
        Map map = new HashMap();
        map.put("exchangeCode", exchangeCode);
        taskService.delete(WssReconnectJob.class.getName(), "WssReconnectJob-" + exchangeCode);
        taskService.addDelayedJob(WssReconnectJob.class.getName(), "WssReconnectJob-" + exchangeCode,
                "WSS延迟重连-" + exchangeCode, DateUtil.getDatePlusSeconds(new Date(), 10), map);
    }

    @JmsListener(destination = MqConstant.MQ_FRESH_BUY_MIN)
    public void receiveFreshBuyMin(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        Long exchangeApiId = jsonObject.getLong("exchangeApiId");
        String currencyName = jsonObject.getString("currencyName");
        String orderId = jsonObject.getString("orderId");
        BigDecimal money = jsonObject.getBigDecimal("money");
        exchangeApiService.syncSpotHoldingOpenPrice(exchangeApiId, currencyName, orderId, money);
    }

    @JmsListener(destination = MqConstant.MQ_TRIGGER_SMART_CONTRACT1)
    public void receiveNewPendingTx1(String text) {
        processNewPendingTx(text);
    }

    @JmsListener(destination = MqConstant.MQ_TRIGGER_SMART_CONTRACT2)
    public void receiveNewPendingTx2(String text) {
        processNewPendingTx(text);
    }

    @JmsListener(destination = MqConstant.MQ_TRIGGER_SMART_CONTRACT3)
    public void receiveNewPendingTx3(String text) {
        processNewPendingTx(text);
    }

    @JmsListener(destination = MqConstant.MQ_TRIGGER_SMART_CONTRACT4)
    public void receiveNewPendingTx4(String text) {
        processNewPendingTx(text);
    }

    public void processNewPendingTx(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        Integer exchangeCode = jsonObject.getInteger("exchangeCode");
        String currencyName = jsonObject.getString("currencyName");
        BigDecimal price = jsonObject.getBigDecimal("price");
        BigDecimal money = jsonObject.getBigDecimal("money");
        redisRepository.hset(KeyConstant.KEY_SWAP_PRICE_MAP + exchangeCode, currencyName, price);
        redisRepository.hset(KeyConstant.KEY_SWAP_MONEY_MAP + exchangeCode + ":" + currencyName, String.valueOf(DateUtil.getDayOfWeek()), money);
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println(week_index);
    }
}
