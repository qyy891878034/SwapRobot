package com.liuqi.business.controller.admin;


import com.liuqi.anno.admin.CurAdminId;
import com.liuqi.base.AdminValid;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.dto.ChainRequestDto;
import com.liuqi.business.dto.DetectContractDto;
import com.liuqi.business.service.BlockChainService;
import com.liuqi.redis.RedisRepository;
import com.liuqi.response.DataResult;
import com.liuqi.response.ReturnResponse;
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
@RequestMapping("/admin/detectRouter")
public class AdminDetectRouterController {

    @Autowired
    private RedisRepository redisRepository;
    //jsp基础路径
    private final static String JSP_BASE_PTH = "admin";
    //模块
    private final static String BASE_MODUEL = "detectRouter";
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
    public DataResult getList(DetectContractDto tdo,
                              @RequestParam(defaultValue = "1", required = false) int page,
                              @RequestParam(defaultValue = "20", required = false) int limit,
                              HttpServletRequest request) {
        List<DetectContractDto> chainList = new ArrayList<>();
        Map<Object, Object> chainMap = redisRepository.hmget(KeyConstant.KEY_DETECT_CHAIN_DATA);
        chainMap.forEach((k, v) -> {
            String chainName = k.toString();
            Map<Object, Object> contractMap = redisRepository.hmget(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + chainName);
            contractMap.forEach((m, n) -> chainList.add((DetectContractDto) n));
        });
        DataResult dataResult = new DataResult();
        dataResult.setData(chainList);
        dataResult.setCount(Long.valueOf(chainList.size()));
        return dataResult;
    }

    private List<String> getChainNameList() {
        List<String> chainList = new ArrayList<>();
        Map<Object, Object> chainMap = redisRepository.hmget(KeyConstant.KEY_DETECT_CHAIN_DATA);
        chainMap.forEach((k, v) -> chainList.add(k.toString()));
        return chainList;
    }

    @RequestMapping("/toAdd")
    public String toAdd(ModelMap modelMap, HttpServletRequest request,
                        HttpServletResponse response) {
        modelMap.put("chainList", getChainNameList());
        return JSP_BASE_PTH + "/" + BASE_MODUEL + "/" + BASE_MODUEL + "Add";
    }

    @RequestMapping("/add")
    @ResponseBody
    public ReturnResponse add(@CurAdminId Long adminId, @Validated(AdminValid.class) DetectContractDto t, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        t.setRouterContract(t.getRouterContract().toLowerCase());
        if (redisRepository.hHasKey(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + t.getChainName(), t.getRouterContract())) {
            return ReturnResponse.backFail("已有此合约");
        }
        redisRepository.hset(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + t.getChainName(), t.getRouterContract(), t);
        return ReturnResponse.backSuccess();
    }


    @RequestMapping("/toUpdate")
    public String toUpdate(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String chainName,
                           String routerContract) {
        DetectContractDto m = redisRepository.hGetModel(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + chainName, routerContract);
        modelMap.put("m", m);
        modelMap.put("chainList", getChainNameList());
        return JSP_BASE_PTH + "/" + BASE_MODUEL + "/" + BASE_MODUEL + "Update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public ReturnResponse update(@CurAdminId Long adminId, String originChainName, String originRouterContract,
                                 @Validated(AdminValid.class) DetectContractDto t, BindingResult bindingResult, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        DetectContractDto m = redisRepository.hGetModel(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + originChainName, originRouterContract);
        redisRepository.hdel(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + originChainName, originRouterContract);
        if (redisRepository.hHasKey(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + t.getChainName(), t.getRouterContract())) {
            return ReturnResponse.backFail("已有此合约");
        }
        redisRepository.hset(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + t.getChainName(), t.getRouterContract(), t);
        return ReturnResponse.backSuccess();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ReturnResponse delete(@CurAdminId Long adminId, String chainName, String routerContract,
                                 ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        redisRepository.hdel(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + chainName, routerContract);
        return ReturnResponse.backSuccess();
    }

}
