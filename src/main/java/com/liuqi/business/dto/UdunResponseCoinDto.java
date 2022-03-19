package com.liuqi.business.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UdunResponseCoinDto implements Serializable{

    private Long currencyId;

    private Integer protocol;//开盘价
}
