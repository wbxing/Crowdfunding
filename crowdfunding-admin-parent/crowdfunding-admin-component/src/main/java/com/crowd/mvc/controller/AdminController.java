package com.crowd.mvc.controller;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

public class AdminController {

    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/admin/do/login.html")
    private String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPswd") String userPswd,
                           HttpSession session) {

        // 调用 Service 方法执行登录检查
        // 查询成功返回 Admin 对象，执行不成功抛出异常
        Admin admin = adminService.getAllAdminByLoginAcct(loginAcct, userPswd);
        // 将登录成功返回的 Admin 对象存入 Session 域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ACCT, admin);
        // 跳转后台主界面
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/admin/do/logout.html")
    private String doLogout(HttpSession session) {

        // Session 失效
        session.invalidate();
        // 跳转后台主界面
        return "redirect:/admin/to/login/page.html";
    }
}
