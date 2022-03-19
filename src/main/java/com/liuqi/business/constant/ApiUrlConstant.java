package com.liuqi.business.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: vmc
 * @description: 外部接口地址
 * @author: chenxi
 * @create: 2019-12-25 14:16
 **/
@Component
public class ApiUrlConstant {

    public static String UDUN_CALL_URL;    //U盾回到地址

    @Value("${udun.callback}")
    public void setUdunCallUrl(String udunCallUrl) {
        UDUN_CALL_URL = udunCallUrl;
    }

}
