package com.crowd.service.api;

import com.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAllAdmins();

    Admin getAllAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);
}
