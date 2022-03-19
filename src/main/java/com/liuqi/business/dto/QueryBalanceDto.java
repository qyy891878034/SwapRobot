package com.liuqi.business.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class QueryBalanceDto {

    private boolean success;

    private BigDecimal balance;
}
