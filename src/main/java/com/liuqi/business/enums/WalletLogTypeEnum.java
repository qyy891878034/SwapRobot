package com.liuqi.business.enums;

import com.liuqi.business.dto.SelectDto;

import java.util.ArrayList;
import java.util.List;

public enum WalletLogTypeEnum {
    SYS("系统", 1,true),
    TRANSFER("转账", 2,false),
    RECHARGE("充值", 3,true),
    EXTRACT("提现", 4,true),
    TRADE_BUY("交易买", 5,true),
    TRADE_SELL("交易卖", 6,true),
    FINANCING("融资融币", 7,false),
    CTC("ctc", 8,false),
    OTC("otc", 9,true),
    SUPERNODE("超级节点", 10,true),
    RELEASE("锁仓释放", 11,true),
    OUT_TRANSFER("外部转入", 12,true),
    CHARGE_AWARD("手续费分红", 13,true),
    LOCK_INPUT("转入锁仓", 14,true),
    OTC_INPUT("转入otc", 15,true),
    WAIT_RELEASE("待用释放", 16,true),
    EXTRACT_OTHER("提现PT", 17,true),
    POINT("点卡", 18,true);

    private String name;
    private Integer code;
    private boolean using;//是否使用

    WalletLogTypeEnum(String name, Integer code,boolean using) {
        this.name = name;
        this.code = code;
        this.using = using;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static String getName(Integer code) {
        for (WalletLogTypeEnum e : WalletLogTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return "";
    }

    public static List<SelectDto> getList() {
        List<SelectDto> list = new ArrayList<SelectDto>();
        for (WalletLogTypeEnum e : WalletLogTypeEnum.values()) {
            if(e.using) {
                list.add(new SelectDto(e.getCode(), e.getName()));
            }
        }
        return list;
    }
}
