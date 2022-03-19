package com.liuqi.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendTransactionResultDto implements Serializable {

    private boolean success;

    private String errorMsg;

    private String hash;

    private String toAddress;// 接收地址

    private String fromAddress;// 发送地址


}
