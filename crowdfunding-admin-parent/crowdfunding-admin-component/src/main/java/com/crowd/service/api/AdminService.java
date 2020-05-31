package com.crowd.service.api;

import java.util.List;

import com.crowd.entity.Admin;
import com.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAllAdmins();

    Admin getAllAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void removeById(Integer adminId);

    Admin getAdminById(Integer adminId);

    void update(Admin admin);

    void removeAdmin(List<Integer> adminIdList);

    PageInfo<Admin> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void saveAdminRoleRelationship(Integer adminId, List<String> roleId);
}
