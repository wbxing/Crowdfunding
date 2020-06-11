package com.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器访问的 url 地址
        String urlPath = "/auth/member/to/reg/page";
        // 目标视图的名称
        String viewName = "member-reg";
        // 创建 view-controller
        registry.addViewController(urlPath).setViewName(viewName);
    }
}