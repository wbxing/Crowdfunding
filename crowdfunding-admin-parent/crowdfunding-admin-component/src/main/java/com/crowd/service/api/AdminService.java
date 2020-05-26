package com.crowd.service.api;

import com.crowd.entity.Admin;

import java.util.List;

public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAllAdmins();

    Admin getAllAdminByLoginAcct(String loginAcct, String userPswd);
}
