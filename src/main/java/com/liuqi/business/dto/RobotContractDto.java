package com.liuqi.business.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RobotContractDto {

    private String routerContract;

    private String coinContract;

    private String stableContract;

}
