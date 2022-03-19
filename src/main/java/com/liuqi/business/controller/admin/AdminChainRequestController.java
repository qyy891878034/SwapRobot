package com.liuqi.business.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.liuqi.anno.admin.CurAdminId;
import com.liuqi.base.AdminValid;
import com.liuqi.base.BaseAdminController;
import com.liuqi.base.BaseService;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.dto.ChainRequestDto;
import com.liuqi.business.model.FwtAirModel;
import com.liuqi.business.model.FwtAirModelDto;
import com.liuqi.business.model.LoggerModelDto;
import com.liuqi.business.service.BlockChainService;
import com.liuqi.business.service.FwtAirService;
import com.liuqi.redis.RedisRepository;
import com.liuqi.response.DataResult;
import com.liuqi.response.ReturnResponse;
import com.liuqi.utils.InstanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/chainRequest")
public class AdminChainRequestController {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private BlockChainService blockChainService;
    //jsp基础路径
    private final static String JSP_BASE_PTH = "admin";
    //模块
    private final static String BASE_MODUEL = "chainRequest";
    //默认为""表示可以使用add和update。  重写add或update时，原有方法禁止使用 NOT_OPERATE="add,update"

    /*******待修改  排序  导出**********************************************************************************************************/

    /*******自己代码**********************************************************************************************************/

    @RequestMapping("/toList")
    public String toList(ModelMap modelMap, HttpServletRequest request,
                         HttpServletResponse response) {
        return JSP_BASE_PTH + "/" + BASE_MODUEL + "/" + BASE_MODUEL + "List";
    }

    @RequestMapping("/queryList")
    @ResponseBody
    public DataResult getList(ChainRequestDto tdo,
                              @RequestParam(defaultValue = "1", required = false) int page,
                              @RequestParam(defaultValue = "20", required = false) int limit,
                              HttpServletRequest request) {
        Map<Object, Object> map = redisRepository.hmget(KeyConstant.KEY_DETECT_CHAIN_DATA);
        List<ChainRequestDto> list = new ArrayList<>();
        map.forEach((k, v) -> list.add((ChainRequestDto) v));
        DataResult dataResult = new DataResult();
        dataResult.setData(list);
        dataResult.setCount(Long.valueOf(list.size()));
        return dataResult;
    }


    @RequestMapping("/toAdd")
    public String toAdd(ModelMap modelMap, HttpServletRequest request,
                        HttpServletResponse response) {
        return JSP_BASE_PTH + "/" + BASE_MODUEL + "/" + BASE_MODUEL + "Add";
    }

    @RequestMapping("/add")
    @ResponseBody
    public ReturnResponse add(@CurAdminId Long adminId, @Validated(AdminValid.class) ChainRequestDto t, BindingResult bindingResult, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        redisRepository.hset(KeyConstant.KEY_DETECT_CHAIN_DATA, t.getChainName(), t);
        return ReturnResponse.backSuccess();
    }


    @RequestMapping("/toUpdate")
    public String toUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
                           @RequestParam("chainName") String chainName) {
        ChainRequestDto m = redisRepository.hGetModel(KeyConstant.KEY_DETECT_CHAIN_DATA, chainName);
        modelMap.put("m", m);
        return JSP_BASE_PTH + "/" + BASE_MODUEL + "/" + BASE_MODUEL + "Update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public ReturnResponse update(@CurAdminId Long adminId, @Validated(AdminValid.class) ChainRequestDto t, BindingResult bindingResult, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        redisRepository.hset(KeyConstant.KEY_DETECT_CHAIN_DATA, t.getChainName(), t);
        return ReturnResponse.backSuccess();
    }

    @RequestMapping("/refreshNonce")
    @ResponseBody
    public ReturnResponse refreshNonce(@CurAdminId Long adminId, String chainName, HttpServletRequest request, HttpServletResponse response) {
        return blockChainService.refreshNonce(chainName);
    }
}
