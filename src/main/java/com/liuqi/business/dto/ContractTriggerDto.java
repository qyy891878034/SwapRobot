package com.liuqi.business.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractTriggerDto {

    private String method;

    private String[] params;

}
