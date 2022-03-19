package com.liuqi.business.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tanyan
 * @create 2020-02=09
 * @description
 */
@Data
public class UdunRechargeCallbackDto implements Serializable {

    private String address;

    private String amount;

    private String blockHigh;

    private String coinType;

    private String decimals;

    private String fee;

    private String mainCoinType;

    private Integer status;

    private String tradeId;

    private Integer tradeType;

    private String txId;

    private String businessId;

    private String memo;

}
