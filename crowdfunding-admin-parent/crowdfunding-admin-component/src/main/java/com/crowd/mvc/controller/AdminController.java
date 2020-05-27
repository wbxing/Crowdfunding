package com.crowd.mvc.controller;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/admin/get/page.html")
    private String getPageInfo(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               ModelMap modelMap) {

        // 调用 service 方法获取 pageInfo 对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        // 将 pageInfo 存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    private String removeAdmin(@PathVariable("adminId") Integer adminId,
                               @PathVariable("pageNum") Integer pageNum,
                               @PathVariable("keyword") String keyword) {
        // 执行删除操作
        adminService.removeById(adminId);
        // 实现页面跳转
        // 1 不能直接转发
        // return "admin-page";
        // 2 转发到 /admin/get/page.html，会出现重复删除
        // return "forward:/admin/get/page.html";
        // 3 重定向，需要附加 pagheNum 和 keyword
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword="
                + keyword;
    }

    @RequestMapping("/admin/save.html")
    private String removeAdmin(Admin admin) {
        // 执行删除操作
        adminService.saveAdmin(admin);

        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId, ModelMap modelMap) {

        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_ADMIN_NAME, admin);
        return "admin-edit";
    }

}
