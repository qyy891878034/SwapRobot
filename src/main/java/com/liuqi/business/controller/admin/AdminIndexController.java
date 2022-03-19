package com.liuqi.business.controller.admin;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.liuqi.base.BaseController;
import com.liuqi.base.LoginAdminUserHelper;
import com.liuqi.business.dto.*;
import com.liuqi.business.dto.stat.WaitExtractStatDto;
import com.liuqi.business.mapper.StatMapper;
import com.liuqi.business.model.RoleModel;
import com.liuqi.business.model.UserAdminModel;
import com.liuqi.business.service.*;
import com.liuqi.response.ReturnResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminIndexController extends BaseController {

    @Autowired
    private UserAdminService userAdminService;
    @Autowired
    private ConfigService configService;
    @RequestMapping("/toLogin")
    public String toLogin(ModelMap modelMap) {
        String projectName=configService.getProjectName();
        modelMap.put("projectName",projectName);
        return "admin/login";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request, ModelMap modelMap) {
        String projectName=configService.getProjectName();
        RoleModel role = LoginAdminUserHelper.getRole();
        UserAdminModel user = LoginAdminUserHelper.getAdmin();
        modelMap.put("roleName", role.getName());
        modelMap.put("adminName", user.getName());
        modelMap.put("projectName",projectName);
        return "admin/index";
    }
    /**
     * 获取菜单
     * @return
     */
    @PostMapping(value = "/getMenu")
    @ResponseBody
    public ReturnResponse getMenu(HttpServletRequest request) {
        String menus=request.getSession().getAttribute("adminUserMenu").toString();
        return ReturnResponse.backSuccess(JSONArray.parseArray(menus));
    }

    /**
     * 管理员登录
     *
     * @param name
     * @param pwd
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ReturnResponse login(@RequestParam("name") String name,
                                @RequestParam("pwd") String pwd,
                                @RequestParam(value = "code",defaultValue = "0") Long code,
                                HttpServletRequest request) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)) {
            return ReturnResponse.builder().code(ReturnResponse.RETURN_FAIL).msg("用户名或者密码不能为空").build();
        }
        try {
            //管理员登录
            userAdminService.login(name, pwd,code, request);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResponse.builder().code(ReturnResponse.RETURN_FAIL).msg(e.getMessage()).build();
        }
        return ReturnResponse.builder().code(ReturnResponse.RETURN_OK).msg("登录成功").build();
    }
    /**
     * 登出
     *
     * @param request
     * @return

     @RequestMapping(value = "/logout")
     public String logout(HttpServletRequest request) {
     SecurityUtils.getSubject().logout();
     System.out.println("------1");
     System.out.println("------12");
     System.out.println("------13");
     return "admin/toLogin";
     }*/

//    /**
//     * 统计
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "/statistics")
//    public String statistics(ModelMap modelMap, HttpServletRequest request) {
//        //统计用户总数
//        int total = userService.getTotal();
//        modelMap.put("total", total);
//
//        List<TaskInfo> taskList = taskService.cronList();
//        modelMap.put("taskCount", taskList.size());
//        modelMap.put("taskRun", taskList.stream().filter(t -> (t.getJobStatusStr().equals("正常") || t.getJobStatusStr().equals("运行中"))).count());
//        modelMap.put("taskError", taskList.stream().filter(t -> (t.getJobStatusStr().equals("异常"))).count());
//        modelMap.put("taskPause", taskList.stream().filter(t -> (t.getJobStatusStr().equals("暂停"))).count());
//
//
//        /***********************************************************************************************************/
//        Date startDate = DateUtil.beginOfDay(new Date());
//        //今日充值
//        List<CurrencyCountDto> toDayRechargeList = rechargeService.queryCountByDate(startDate, null);
//        //所有充值
//        List<CurrencyCountDto> rechargelist = rechargeService.queryCountByDate(null, null);
//        modelMap.put("toDayRechargeList", toDayRechargeList);
//        modelMap.put("rechargelist", rechargelist);
//        //今日提现
//        List<CurrencyCountDto> toDayExtractList = extractService.queryCountByDate(startDate, null);
//        //所有提现
//        List<CurrencyCountDto> extractList = extractService.queryCountByDate(null, null);
//        modelMap.put("toDayExtractList", toDayExtractList);
//        modelMap.put("extractList", extractList);
//
//        //获取待提现数据
//        List<WaitExtractStatDto> waitList = statMapper.waitExtractStat();
//        waitList.stream().forEach(dto -> {
//            CurrencyModel model = currencyService.getById(dto.getCurrencyId());
//            dto.setCurrencyName(model != null ? model.getName() : "");
//        });
//        modelMap.put("waitList", waitList);
//
//        return "admin/statistics";
//    }
//
//    @Autowired
//    private GenerateService generateService;
//    /**
//     * 数据库导出
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "/export")
//    public String export(ModelMap modelMap, HttpServletRequest request) {
//        List<ExportDto> exportList=new ArrayList<>();
//        List<TableInfo> list=generateService.getAllTableName();
//        if(list!=null && list.size()>0){
//            for (TableInfo table : list) {
//                List<ColumnInfo> columnList =generateService.listTableColumn(table.getTableName());
//                exportList.add(new ExportDto(table.getTableName(),table.getTableComment(),columnList));
//            }
//        }
//        modelMap.put("list",exportList);
//        return "admin/export";
//    }

}
