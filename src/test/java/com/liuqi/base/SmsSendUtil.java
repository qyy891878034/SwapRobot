package com.liuqi.base;

import cn.hutool.http.HttpRequest;
import com.liuqi.utils.HttpsUtils;
import org.redisson.misc.Hash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tanyan
 * @create 2019-12=14
 * @description
 */
public class SmsSendUtil {
    public static void main(String[] args) {
        Map<String,Object> params=new HashMap<>();
        params.put("CorpID","CDJS009250");
        params.put("Pwd","zm0513@");
        params.put("Mobile","18674006013");
        params.put("Content","尊敬的TEC用户，您的验证码123456，该验证码10分钟内有效。【TEC】");
        params.put("Cell","");
        params.put("SendTime","");
        HttpRequest request=HttpRequest.post("https://sdk2.028lk.com/sdk2/BatchSend2.aspx");
        String str=request.form(params).timeout(3000).execute().body();
        System.out.println(str);
    }
}
