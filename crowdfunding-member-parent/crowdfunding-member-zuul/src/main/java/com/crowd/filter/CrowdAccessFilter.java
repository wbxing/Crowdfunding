package com.crowd.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.crowd.constant.AccessPassResources;
import com.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        // 获取 RequestContext 对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 通过 RequestContext 对象获取当前请求对象
        // 框架底层是借助 ThreadLocal 从当前线程上获取事先绑定的 Request 对象
        HttpServletRequest request = requestContext.getRequest();
        // 获取 servletPath 值
        String servletPath = request.getServletPath();
        // 根据 servletPath 判断当前请求是否对应可以直接放行的特定功能
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);
        if (containsResult) {
            // 如果当前请求是可以直接放行的特定功能请求则返回 false 放行
            return false;
        }
        // 判断当前请求是否为静态资源
        // 工具方法返回 true：说明当前请求是静态资源请求，取反为 false 表示放行不做登录检查
        // 工具方法返回 false：说明当前请求不是可以放行的特定请求也不是静态资源
        // 非静态资源需要进行登录检查
        // 取反为 true 表示需要做登录检查
        return !AccessPassResources.isStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {
        // 获取当前请求对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        // 获取当前 Session 对象
        HttpSession session = request.getSession();
        // 尝试从 Session 对象中获取已登录的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_MEMBER_NAME);
        // 判断 loginMember 是否为空
        if (loginMember == null) {
            // 从 requestContext 对象中获取 Response 对象
            HttpServletResponse response = requestContext.getResponse();
            // 将提示消息存入 Session 域
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
            // 重定向到 auth-consumer 工程中的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String filterType() {
        // 这里返回 pre 意思是在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

}