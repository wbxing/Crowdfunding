package com.crowd.mvc.controller;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Role;
import com.crowd.service.api.AdminService;
import com.crowd.service.api.RoleService;
import com.crowd.utils.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AssignController {

    private final RoleService roleService;
    private final AdminService adminService;

    public AssignController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @ResponseBody
    @RequestMapping("/assign/role.json")
    public ResultEntity<Map<String, List<Role>>> assignRole(Integer adminId, ModelMap modelMap) {
        Map<String, List<Role>> maps = new HashMap<>();
        // 查询已分配角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        // 查询未分配角色
        List<Role> unassignedRoleList = roleService.getUnassignedRole(adminId);
        maps.put(CrowdConstant.ATTR_NAME_ASSIGNED_ROLE_NAME, assignedRoleList);
        maps.put(CrowdConstant.ATTR_NAME_UNASSIGNED_ROLE_NAME, unassignedRoleList);

        return ResultEntity.successWithData(maps);
    }

    @ResponseBody
    @RequestMapping("/assign/save/role.json")
    public ResultEntity<String> saveAdminRoleRelationship(@RequestParam("adminId") Integer adminId,
                                                          @RequestParam("roleIdList") String roleIdList) {
        String[] strings;
        List<String> roleId;
        if (roleIdList.length() > 4) {

            strings = roleIdList.substring(2, roleIdList.length() - 2).split("\",\"");
            roleId = Arrays.asList(strings);
        System.out.println("************************************************");
        } else {
            roleId = null;
        }
        adminService.saveAdminRoleRelationship(adminId, roleId);
        return ResultEntity.successWithoutData();
    }
}
