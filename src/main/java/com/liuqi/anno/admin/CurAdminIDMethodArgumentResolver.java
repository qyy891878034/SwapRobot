package com.liuqi.anno.admin;

import com.google.common.base.Preconditions;
import com.liuqi.base.LoginAdminUserHelper;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurAdminIDMethodArgumentResolver implements HandlerMethodArgumentResolver{

    public CurAdminIDMethodArgumentResolver() {}

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurAdminId.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, @Nullable WebDataBinderFactory webDataBinderFactory) throws Exception {
        Long adminId= LoginAdminUserHelper.getAdminId();
        Preconditions.checkArgument(adminId!=null && adminId>0,"获取管理员异常");
        return adminId;
    }
}
