package com.liuqi.business.constant;

import com.liuqi.base.BaseConstant;

/**
 * @author tanyan
 * @create 2019-11=30
 * @description
 */
public class MqConstant {

    public final static String BASE = BaseConstant.BASE_PROJECT + ":";

    public static final String MQ_TRIGGER_SMART_CONTRACT1 = BASE + "mq:trigger:smart:contract1";
    public static final String MQ_TRIGGER_SMART_CONTRACT2 = BASE + "mq:trigger:smart:contract2";
    public static final String MQ_TRIGGER_SMART_CONTRACT3 = BASE + "mq:trigger:smart:contract3";
    public static final String MQ_TRIGGER_SMART_CONTRACT4 = BASE + "mq:trigger:smart:contract4";

    public final static String MQ_DESTINATION_EMAIL = BASE + "mq:email";//邮件
    public final static String MQ_DESTINATION_SMS = BASE + "mq:sms";//短信
    public final static String MQ_INSERT_CLAMPED_DATA = BASE + "mq:insert:clamped:data";// 余额查询
    public final static String MQ_RECONNECT_CHAIN_WS = BASE + "mq:reconnect:chain:ws";// 余额查询

    public final static String MQ_DESTINATION_TRANSACTION1 = BASE + "mq:transaction:1";// 余额查询
    public final static String MQ_DESTINATION_TRANSACTION2 = BASE + "mq:transaction:2";// 余额查询

    public final static String MQ_DESTINATION_NONCE = BASE + "mq:nonce";// 余额查询

}
