package com.crowd.mvc.controller;

import com.crowd.entity.Admin;
import com.crowd.service.api.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestController {

    private AdminService adminService;

    Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request) {

        List<Admin> admins = adminService.getAllAdmins();
        modelMap.addAttribute("admins", admins);
        // 测试异常
//        System.out.println(10 / 0);
        return "target";
    }
}
