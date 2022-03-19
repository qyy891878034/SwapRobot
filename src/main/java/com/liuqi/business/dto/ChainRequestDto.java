package com.liuqi.business.dto;

import lombok.Data;

@Data
public class ChainRequestDto {

    private Long chainId;

    private String chainName;

    private String url;

    private String privateKey;

}
