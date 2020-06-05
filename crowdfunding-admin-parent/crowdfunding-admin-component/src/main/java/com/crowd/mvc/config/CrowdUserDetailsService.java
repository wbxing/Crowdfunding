package com.crowd.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.crowd.entity.Admin;
import com.crowd.entity.Role;
import com.crowd.service.api.AdminService;
import com.crowd.service.api.AuthService;
import com.crowd.service.api.RoleService;

@Component
public class CrowdUserDetailsService implements UserDetailsService {

    private final AdminService adminService;
    private final RoleService roleService;
    private final AuthService authService;

    public CrowdUserDetailsService(AdminService adminService, RoleService roleService, AuthService authService) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.authService = authService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据账号名称查询 Admin 对象
        Admin admin = adminService.getAdminByLoginAcct(username);
        // 获取 adminId
        Integer adminId = admin.getId();
        // 根据 adminId 查询角色信息
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        // 根据 adminId 查询权限信息
        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
        // 创建集合对象用来存储 GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 遍历 assignedRoleList 存入角色信息
        for (Role role : assignedRoleList) {
            // 注意：不要忘了加前缀！
            String roleName = "ROLE_" + role.getName();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authorities.add(simpleGrantedAuthority);
        }
        // 遍历 authNameList 存入权限信息
        for (String authName : authNameList) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
            authorities.add(simpleGrantedAuthority);
        }
        // 返回封装 SecurityAdmin 对象
        return new SecurityAdmin(admin, authorities);
    }

}
