package com.liuqi.business.enums;

import com.liuqi.business.dto.SelectDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 协议
 */
public enum ProtocolEnum {
    ETH("ETH",60),
    BTC("BTC", 0),
    EOS("EOS", 194),
    ETC("ETC", 61),
    LTC("LTC", 2),
    XRP("XRP", 144),
    TRX("TRX", 195),
    FIL("FIL", 461),
    GCT("GCT", 21),
    NONE("无用", -1);

    private String name;
    private Integer code;

    ProtocolEnum(String name, int code) {
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
            for (ProtocolEnum e : ProtocolEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e.getName();
                }
            }
        }
        return "";
    }

    public static List<SelectDto> getList() {
        List<SelectDto> list = new ArrayList<SelectDto>();
        for (ProtocolEnum e : ProtocolEnum.values()) {
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
        for (ProtocolEnum e : ProtocolEnum.values()) {
            if (e.getCode().equals(code)) {
                hashCode = true;
                break;
            }
        }
        return hashCode;
    }

}
