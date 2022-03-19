package com.liuqi.mq.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RechargeDto implements Serializable{

    private Long userId;
    private Long currencyId;

    public RechargeDto() {

    }

    public RechargeDto(Long userId, Long currencyId) {
        this.userId = userId;
        this.currencyId = currencyId;
    }
}
