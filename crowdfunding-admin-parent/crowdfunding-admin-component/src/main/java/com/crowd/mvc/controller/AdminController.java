package com.crowd.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.service.api.AdminService;
import com.crowd.utils.ResultEntity;
import com.github.pagehelper.PageInfo;

@Controller
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

    @ResponseBody
    @RequestMapping("/admin/get/page/info.json")
    public ResultEntity<PageInfo<Admin>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        // 调用 Service 方法获取分页数据
        PageInfo<Admin> pageInfo = adminService.getPageInfo(pageNum, pageSize, keyword);
        // 封装到 ResultEntity 对象中返回（如果上面的操作抛出异常，交给异常映射机制处理）
        return ResultEntity.successWithData(pageInfo);
    }

    @ResponseBody
    @RequestMapping("admin/remove.json")
    private ResultEntity<String> removeAdmin1(@RequestBody List<Integer> adminIdList) {
        // 执行删除操作
        adminService.removeAdmin(adminIdList);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/admin/save.json")
    public ResultEntity<String> saveAdmin(Admin admin) {
        adminService.saveAdmin(admin);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/admin/update.json")
    public ResultEntity<String> updateRole(Admin admin) {
        adminService.update(admin);
        return ResultEntity.successWithoutData();
    }

}
