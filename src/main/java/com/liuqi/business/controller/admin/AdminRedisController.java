package com.liuqi.business.controller.admin;


import com.liuqi.base.BaseController;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.redis.RedisRepository;
import com.liuqi.response.ReturnResponse;
import com.liuqi.third.zb.SearchPrice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/redis")
public class AdminRedisController extends BaseController {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private SearchPrice searchPrice;

    @RequestMapping("/toList")
    public String toList(ModelMap modelMap) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(KeyConstant.KEY_USER_AUTH, "验证码,例如：" + KeyConstant.KEY_USER_AUTH + "用户登录名");
        map.put(KeyConstant.KEY_ALL_PRICE, "所有价格");
        map.put(KeyConstant.KEY_CTC_NUM, "ctc编号");
        map.put(KeyConstant.KEY_OTC_NUM, "otc编号");
        map.put(KeyConstant.KEY_WORK_NUM, "工单编号");
        modelMap.put("map", map);
        return "admin/redis/redisList";
    }

    /**
     * 获取key值
     *
     * @param key
     * @return
     */
    @PostMapping(value = "/getKey")
    @ResponseBody
    public ReturnResponse getKey(@RequestParam("key") String key, HttpServletRequest request) {
        if (StringUtils.isEmpty(key)) {
            return ReturnResponse.builder().code(ReturnResponse.RETURN_FAIL).msg("参数异常").build();
        }
        String value = redisRepository.getString(key);
        return ReturnResponse.backSuccess(value);
    }

    /**
     * 清除单个key
     *
     * @param key
     * @return
     */
    @PostMapping(value = "/cleanKey")
    @ResponseBody
    public ReturnResponse cleanKey(@RequestParam("key") String key, HttpServletRequest request) {
        if (StringUtils.isEmpty(key)) {
            return ReturnResponse.builder().code(ReturnResponse.RETURN_FAIL).msg("参数异常").build();
        }
        redisRepository.del(key);
        return ReturnResponse.backSuccess();
    }


}
