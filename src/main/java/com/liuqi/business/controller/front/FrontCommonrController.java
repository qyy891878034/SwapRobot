package com.liuqi.business.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.base.BaseFrontController;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.redis.RedisRepository;
import com.liuqi.response.ReturnResponse;
import com.liuqi.utils.DESUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "通用配置")
@RestController
@RequestMapping("/api/v11/common")//（前台）用户控制层
public class FrontCommonrController extends BaseFrontController {

    @Autowired
    private RedisRepository redisRepository;

    @ApiOperation(value = "FIL设置GAS")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "data", value = "加密后的数据", paramType = "query")
    })
    @PostMapping("/set/fil/gas")
    public ReturnResponse setFilGas(String data) {
        String d = DESUtil.decryptBasedDes(data, DESUtil.COMMON_PWD1 + DESUtil.COMMON_PWD2);
        JSONObject jsonObject = JSONObject.parseObject(d);
        redisRepository.set(KeyConstant.KEY_FIL_GAS_LIMIT, jsonObject.getLong("gasLimit"));// 2300000
        redisRepository.set(KeyConstant.KEY_FIL_GAS_FEEGAP, jsonObject.getLong("gasFeecap"));// 210000
        redisRepository.set(KeyConstant.KEY_FIL_GAS_PREMIUM, jsonObject.getLong("gasPremium"));// 205000
        return ReturnResponse.backSuccess();
    }

    @ApiOperation(value = "FIL获取GAS")
    @GetMapping("/get/fil/gas")
    public ReturnResponse getFilGas() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gasLimit", redisRepository.getLong(KeyConstant.KEY_FIL_GAS_LIMIT));
        jsonObject.put("gasFeecap", redisRepository.getString(KeyConstant.KEY_FIL_GAS_FEEGAP));
        jsonObject.put("gasPremium", redisRepository.getString(KeyConstant.KEY_FIL_GAS_PREMIUM));
        return ReturnResponse.backSuccess(jsonObject);
    }

}
