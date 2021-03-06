package com.liuqi.business.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.base.BaseConstant;
import com.liuqi.base.BaseMapper;
import com.liuqi.base.BaseServiceImpl;
import com.liuqi.business.async.AsyncTask;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.dto.MenuDto;
import com.liuqi.business.enums.YesNoEnum;
import com.liuqi.business.mapper.UserAdminMapper;
import com.liuqi.business.model.RoleModel;
import com.liuqi.business.model.UserAdminModel;
import com.liuqi.business.model.UserAdminModelDto;
import com.liuqi.business.service.MenuService;
import com.liuqi.business.service.RoleService;
import com.liuqi.business.service.UserAdminService;
import com.liuqi.exception.BusinessException;
import com.liuqi.redis.RedisRepository;
import com.liuqi.shiro.MyLoginToken;
import com.liuqi.third.google.GoogleAuthService;
import com.liuqi.utils.ShiroPasswdUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class UserAdminServiceImpl extends BaseServiceImpl<UserAdminModel, UserAdminModelDto> implements UserAdminService {

	@Autowired
	private UserAdminMapper userAdminMapper;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RedisRepository redisRepository;
	@Autowired
	private GoogleAuthService googleAuthService;
	@Autowired
	@Lazy
	private AsyncTask asyncTask;
	@Override
	public BaseMapper<UserAdminModel, UserAdminModelDto> getBaseMapper() {
		return this.userAdminMapper;
	}

	@Override
	public void beforeAddCheck(UserAdminModel userAdminModel) {
		super.beforeAddCheck(userAdminModel);
		userAdminModel.setPwd(ShiroPasswdUtil.getAdminPwd(userAdminModel.getPwd()));
	}

	@Override
	@Transactional
	public void modifyPwd(Long adminId, String newPwd) {
		UserAdminModel admin=this.getById(adminId);
		admin.setPwd(ShiroPasswdUtil.getAdminPwd(newPwd));
		this.update(admin);
	}

    @Override
	@Transactional
    public void modifyPwdByOld(Long adminId, String old, String pwd) {
		UserAdminModel admin=this.getById(adminId);
		if(!admin.getPwd().equals(ShiroPasswdUtil.getAdminPwd(old))){
			throw new BusinessException("??????????????????");
		}
		admin.setPwd(ShiroPasswdUtil.getAdminPwd(pwd));
		this.update(admin);
    }

    @Override
	public void login(String name, String pwd,Long code,HttpServletRequest request) {
		String loginErrorKey = KeyConstant.KEY_LOGIN_ERROR +BaseConstant.TYPE_ADMIN+ name;
		Integer times = redisRepository.getInteger(loginErrorKey);
		if (times >= 3) {
			if(times==3) {
				asyncTask.addAdminLoginLog(request, name, "???????????????3?????????10??????");
			}
			throw new BusinessException("????????????3?????????10???????????????");
		}

		MyLoginToken token = new MyLoginToken(name, pwd, BaseConstant.TYPE_ADMIN);
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			UserAdminModelDto admin = (UserAdminModelDto)subject.getPrincipal();
			if(YesNoEnum.YES.getCode().equals( admin.getAuth())){
				boolean verify=googleAuthService.verify(admin.getSecret(),code);
				if(!verify){
					asyncTask.addAdminLoginLog(request,name,"???????????????????????????");
					throw new BusinessException("??????????????????");
				}
			}

			Session session=subject.getSession();
			session.setAttribute(BaseConstant.ADMIN_USERID_SESSION, admin.getId());
			session.setAttribute(BaseConstant.ADMIN_USER_SESSION,JSONObject.toJSONString(admin));

			RoleModel role=roleService.getById(admin.getRoleId());
			session.setAttribute(BaseConstant.ADMIN_USER_ROLE, JSONObject.toJSONString(role));
			//???????????????????????????session
			List<MenuDto> menuList=menuService.getByUserId(admin.getId());
			session.setAttribute("adminUserMenu", JSONArray.toJSONString(menuList));

			asyncTask.addAdminLoginLog(request,name,"????????????");
			//???????????? ????????????key
			redisRepository.del(loginErrorKey);
		} catch (LockedAccountException e) {
			asyncTask.addAdminLoginLog(request,name,"?????????????????????");
			times++;
			if (times == 1) {
				redisRepository.set(loginErrorKey, times, 10L, TimeUnit.MINUTES);
			} else {
				redisRepository.incrOne(loginErrorKey);
			}
			throw new BusinessException("????????????");
		} catch (IncorrectCredentialsException e) {
			asyncTask.addAdminLoginLog(request,name,"??????????????????/???????????????");
			times++;
			if (times == 1) {
				redisRepository.set(loginErrorKey, times, 10L, TimeUnit.MINUTES);
			} else {
				redisRepository.incrOne(loginErrorKey);
			}
			throw new BusinessException("?????????/???????????????");
		} catch (Exception e) {
			asyncTask.addAdminLoginLog(request,name,"?????????"+e.getMessage());
			times++;
			if (times == 1) {
				redisRepository.set(loginErrorKey, times, 10L, TimeUnit.MINUTES);
			} else {
				redisRepository.incrOne(loginErrorKey);
			}
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public UserAdminModelDto findByName(String username) {
		return userAdminMapper.findByName(username);
	}

	@Override
	public String getNameById(Long id) {
		UserAdminModelDto dto=this.getById(id);
		return dto!=null?dto.getName():"";
	}

	/**
	 * ??????????????????
	 * @param id
	 * @return
	 */
	@Override
	public UserAdminModelDto getSecret(Long id){
		return  userAdminMapper.getSecret(id);
	}
}
