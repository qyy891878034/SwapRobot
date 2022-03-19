package com.liuqi.business.controller.admin;


import com.liuqi.anno.admin.CurAdminId;
import com.liuqi.base.AdminValid;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.dto.DetectContractDto;
import com.liuqi.business.dto.RobotContractDto;
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
@RequestMapping("/admin/robotContract")
public class AdminRobotContractController {

    @Autowired
    private RedisRepository redisRepository;
    //jsp基础路径
    private final static String JSP_BASE_PTH = "admin";
    //模块
    private final static String BASE_MODUEL = "robotContract";
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
        List<RobotContractDto> coinList = new ArrayList<>();
        Map<Object, Object> chainMap = redisRepository.hmget(KeyConstant.KEY_DETECT_CHAIN_DATA);
        chainMap.forEach((k, v) -> {
            String chainName = k.toString();
            Map<Object, Object> routerMap = redisRepository.hmget(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + chainName);
            routerMap.forEach((m, n) -> {
                Map<Object, Object> coinMap = redisRepository.hmget(KeyConstant.KEY_ROUTER_2_ROBOT_CONTRACT_MAP + m.toString());
                coinMap.forEach((x, y) -> {
                    coinList.add(RobotContractDto.builder().routerContract(m.toString()).coinContract(x.toString()).stableContract(y.toString()).build());
                });
            });
        });
        DataResult dataResult = new DataResult();
        dataResult.setData(coinList);
        dataResult.setCount(Long.valueOf(coinList.size()));
        return dataResult;
    }

    private List<String> getRoutersList() {
        List<String> routerContracts = new ArrayList<>();
        Map<Object, Object> chainMap = redisRepository.hmget(KeyConstant.KEY_DETECT_CHAIN_DATA);
        chainMap.forEach((k, v) -> {
            String chainName = k.toString();
            Map<Object, Object> routerMap = redisRepository.hmget(KeyConstant.KEY_DETECT_CONTRACT_ADDRESS + chainName);
            routerMap.forEach((m, n) -> {
                routerContracts.add(m.toString());
            });
        });
        return routerContracts;
    }

    @RequestMapping("/toAdd")
    public String toAdd(ModelMap modelMap, HttpServletRequest request,
                        HttpServletResponse response) {
        modelMap.put("routerContracts", getRoutersList());
        return JSP_BASE_PTH + "/" + BASE_MODUEL + "/" + BASE_MODUEL + "Add";
    }

    @RequestMapping("/add")
    @ResponseBody
    public ReturnResponse add(@CurAdminId Long adminId, String routerContract, String coinContract, String stableContract, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        routerContract = routerContract.toLowerCase();
        coinContract = coinContract.toLowerCase();
        stableContract = stableContract.toLowerCase();
        if (redisRepository.hHasKey(KeyConstant.KEY_ROUTER_2_ROBOT_CONTRACT_MAP + routerContract, coinContract)) {
            return ReturnResponse.backFail("已有此币合约");
        }
        redisRepository.hset(KeyConstant.KEY_ROUTER_2_ROBOT_CONTRACT_MAP + routerContract, coinContract, stableContract);
        return ReturnResponse.backSuccess();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ReturnResponse delete(@CurAdminId Long adminId, String coinContract, String routerContract,
                                 ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        redisRepository.hdel(KeyConstant.KEY_ROUTER_2_ROBOT_CONTRACT_MAP + routerContract, coinContract);
        return ReturnResponse.backSuccess();
    }

}
