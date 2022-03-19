package com.liuqi.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LockRuleDto implements Serializable {

    //数量
    private Integer quantity;

    //释放
    private Integer release;
}
