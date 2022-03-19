package com.liuqi.business.dto.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liuqi.business.enums.BuySellEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReleaseStatDto implements Serializable {


    private BigDecimal quantity;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date send_date;

    private Long currency_id;

    private String currencyName;

}
