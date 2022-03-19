package com.liuqi.business.enums;

import com.liuqi.business.dto.SelectDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 协议
 */
public enum ContractEnum {
    ERC20("ERC-20",60),
    TRC20("TRC-20", 195),
    OMNI("OMNI", 0);

    private String name;
    private Integer code;

    ContractEnum(String name, int code) {
        this.name = name;
        this.code = code;
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
        if (code != null) {
            for (ContractEnum e : ContractEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e.getName();
                }
            }
        }
        return "";
    }

    public static List<SelectDto> getList() {
        List<SelectDto> list = new ArrayList<SelectDto>();
        for (ContractEnum e : ContractEnum.values()) {
            list.add(new SelectDto(e.getCode(), e.getName()));
        }
        return list;
    }


//    public static Integer getProtocol(Integer udunCode) {
//        for (ProtocolEnum e : ProtocolEnum.values()) {
//            if (e.getUdunCode().compareTo(udunCode) == 0) {
//                return e.getCode();
//            }
//        }
//        return null;
//    }

    public static boolean hasCode(Integer code) {
        boolean hashCode = false;
        for (ContractEnum e : ContractEnum.values()) {
            if (e.getCode().equals(code)) {
                hashCode = true;
                break;
            }
        }
        return hashCode;
    }

}
