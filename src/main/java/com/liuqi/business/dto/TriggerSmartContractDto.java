package com.liuqi.business.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Builder
public class TriggerSmartContractDto implements Serializable {

    private Long chainId;

    private BigInteger gasLimit;

    private BigInteger gasPrice;

    private String privateKey;

    private String contractAddress;

    private String method;

    private String params;

}
