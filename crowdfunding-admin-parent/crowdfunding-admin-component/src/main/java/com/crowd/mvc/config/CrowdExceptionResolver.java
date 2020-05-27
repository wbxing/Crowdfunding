package com.crowd.mvc.config;

import com.crowd.constant.CrowdConstant;
import com.crowd.exception.AccessForbiddenException;
import com.crowd.exception.LoginAcctAlreadyInUseException;
import com.crowd.exception.LoginFailedException;
import com.crowd.utils.CrowdUtils;
import com.crowd.utils.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CrowdExceptionResolver {

    // 每一种异常对应一种处理方法
    // 加上这个可以指定未登录时权限检查的跳转页面
//    @ExceptionHandler(value = AccessForbiddenException.class)
//    public ModelAndView resolveAccessForbiddenException(AccessForbiddenException exception, HttpServletRequest
//    request,
//                                                    HttpServletResponse response) throws IOException {
//        String viewName = "admin-login";
//        return commonResolveException(exception, request, response, viewName);
//    }

    // 每一种异常对应一种处理方法
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolveException(exception, request, response, viewName);
    }

    // 每一种异常对应一种处理方法
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception, HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolveException(exception, request, response, viewName);
    }

    // 每一种异常对应一种处理方法
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolveException(exception, request, response, viewName);
    }

    /**
     * 核心异常处理方法
     *
     * @param exception 实际捕获到的异常
     * @param request   当前请求对象
     * @param response  当前响应对象
     * @param viewName  异常处理完成之后的视图名称
     * @return 返回相应视图对象
     * @throws IOException 响应 IO 异常
     */
    private ModelAndView commonResolveException(Exception exception, HttpServletRequest request,
                                                HttpServletResponse response, String viewName) throws IOException {
        // 判断当前请求类型
        boolean isAjax = CrowdUtils.isAjax(request);
        // 如果是 Ajax 请求
        if (isAjax) {
            // 新建 ResultEntity 对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            // 转换为 json 字符串
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);
            // 返回给浏览器
            response.getWriter().write(json);
            // 上面已经通过原生 response 返回了响应，所以可以直接返回 null
            return null;
        }
        // 如果不是 Ajax 请求，创建 ModelAndView 对象
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 设置对应视图名称
        modelAndView.setViewName(viewName);

        return modelAndView;
    }
}
