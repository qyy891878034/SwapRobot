package com.liuqi.business.async;

import com.liuqi.business.service.UserAdminLoginService;
import com.liuqi.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 注意事项：
 如下方式会使@Async失效
 一、异步方法使用static修饰
 二、异步类没有使用@Component注解（或其他注解）导致spring无法扫描到异步类
 三、异步方法不能与调用异步方法的方法在同一个类中
 四、类中需要使用@Autowired或@Resource等注解自动注入，不能自己手动new对象
 五、如果使用SpringBoot框架必须在启动类中增加@EnableAsync注解
 六、在Async 方法上标注@Transactional是没用的。 在Async 方法调用的方法上标注@Transactional 有效。
 */
@Component("asyncTask")
@Async("threadPoolTaskExecutor")
public class AsyncTask {
    @Autowired
    private UserAdminLoginService userAdminLoginService;

    public void addAdminLoginLog(HttpServletRequest request,String loginName,String remark){
        if(StringUtils.isEmpty(remark)){
            remark="-";
        }
        String ip= IpUtils.getIpAddr(request);
        String city=IpUtils.getCity(ip);
        userAdminLoginService.addLog(loginName,ip,city,remark);
    }
}
