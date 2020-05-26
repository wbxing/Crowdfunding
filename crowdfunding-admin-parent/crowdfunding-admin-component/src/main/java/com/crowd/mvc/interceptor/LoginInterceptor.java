package com.crowd.mvc.interceptor;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 request 域中获取 session
        HttpSession session = request.getSession();
        // 尝试从 session 中获取 Admin 对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ACCT);
        // 如果 Admin 对象为空
        if (admin == null) {
            // 未登录，抛异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        // 放行
        return true;
    }
}
