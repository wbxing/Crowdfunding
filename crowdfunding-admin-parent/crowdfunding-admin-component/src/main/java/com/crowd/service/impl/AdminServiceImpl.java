package com.crowd.service.impl;

import com.crowd.entity.Admin;
import com.crowd.mapper.AdminMapper;
import com.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminMapper adminMapper;

    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminMapper.insert(admin);
    }
}
