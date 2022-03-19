package com.liuqi.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExtractDto implements Serializable {

    private String pickUid;
    private String txHash;
    private String toAddress;
    private String pickValue;
    /**
     * 100等待提现。101等待查询。0pending，-1失败  1成功
     */
    private String transactionState;
    private String time;

}
