package com.liuqi.business.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DetectContractDto implements Serializable {

    private String chainName;// 监控的链

    private String routerContract;// 监控的Swap路由地址

}
