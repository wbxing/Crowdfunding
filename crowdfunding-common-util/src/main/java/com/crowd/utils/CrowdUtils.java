package com.crowd.utils;

import javax.servlet.http.HttpServletRequest;

public class CrowdUtils {

    /**
     * 判断当前请求是否为 Ajax 请求
     *
     * @param request 请求对象
     * @return true: Ajax 请求; false: 非  Ajax 请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        // 获取请求头信息
        String accept = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        // 判断
        // 判断 accept 内容
        boolean acceptIsJson = (accept != null && accept.length() > 0 && accept.contains("application/json"));
        // 判断 xRequestedWith 内容
        boolean xRequestedWithIsJson = (xRequestedWith != null && xRequestedWith.length() > 0
                && xRequestedWith.equals("XMLHttpRequest"));
        // 返回结果
        return acceptIsJson || xRequestedWithIsJson;
    }
}
