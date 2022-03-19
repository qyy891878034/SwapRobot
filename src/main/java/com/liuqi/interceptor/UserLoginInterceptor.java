package com.liuqi.interceptor;


import ch.qos.logback.core.util.CloseUtil;
import cn.hutool.json.JSONUtil;
import com.liuqi.base.BaseConstant;
import com.liuqi.response.ReturnResponse;
import com.liuqi.token.RedisTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员登录拦截器
 */
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RedisTokenManager redisTokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//刷新token
		String token = request.getHeader(BaseConstant.TOKEN_NAME);
		redisTokenManager.refreshUserToken(token);
		return super.preHandle(request, response, handler);
	}


	/**
	 * 响应信息
	 *
	 * @param request
	 * @param response
	 * @param message  响应的信息
	 * @return
	 */
	public void response(HttpServletRequest request, HttpServletResponse response, Integer code, String message) {
		ReturnResponse returnResponse = ReturnResponse.builder()
				.code(code).msg(message).build();
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(JSONUtil.toJsonStr(returnResponse).getBytes());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CloseUtil.closeQuietly(outputStream);
		}
	}

}