package com.liuqi.business.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.base.BaseFrontController;
import com.liuqi.business.service.CoinFilterService;
import com.liuqi.business.service.ExchangeApiService;
import com.liuqi.response.ReturnResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "通用配置")
@RestController
@Slf4j
@RequestMapping({"/api/v1"})
public class FrontCommonrController extends BaseFrontController {

    @Autowired
    private ExchangeApiService exchangeApiService;
    @Autowired
    private CoinFilterService coinFilterService;

    @ApiOperation("平台策略下单")
    @PostMapping({"/platform/order"})
    public ReturnResponse platFormOrder(@RequestBody String data) {
       log.info("TradingView{}", data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        exchangeApiService.placePlatformSwapOrder(jsonObject.getInteger("exchangeCode"),jsonObject.getString("time"),jsonObject
                .getInteger("action"),jsonObject.getString("currencyName"),jsonObject.getString("identifier"));
        return ReturnResponse.backSuccess();
    }

    @ApiOperation("自主策略下单")
    @PostMapping({"/manual/order"})
    public ReturnResponse manualOrder(@RequestBody String data){
        log.info("TradingView{}", data);
        JSONObject jsonObject=JSONObject.parseObject(data);
        exchangeApiService.placeManualSwapOrder(jsonObject.getInteger("exchangeCode"),jsonObject.getString("time"),
                jsonObject.getInteger("action"),jsonObject.getString("currencyName"),jsonObject.getString("identifier"));
        return ReturnResponse.backSuccess();
    }

    @ApiOperation("sdfsdfs")
    @GetMapping({"/sync/ss"})
    public ReturnResponse sfsdfsf() {
        coinFilterService.ss();
        return ReturnResponse.backSuccess();
    }

}
