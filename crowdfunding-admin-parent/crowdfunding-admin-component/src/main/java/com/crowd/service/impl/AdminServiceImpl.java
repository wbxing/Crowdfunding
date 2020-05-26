package com.crowd.service.impl;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.entity.AdminExample;
import com.crowd.exception.LoginFailedException;
import com.crowd.mapper.AdminMapper;
import com.crowd.service.api.AdminService;
import com.crowd.utils.CrowdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    @Override
    public List<Admin> getAllAdmins() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAllAdminByLoginAcct(String loginAcct, String userPswd) {
        // 根据登录对象查询
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        // 查询结果为空则抛出异常
        if (admins.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 查询结果不唯一
        if (admins.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = admins.get(0);
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 查询结果不为空，取出密码
        String userPswdFromDB = admin.getUserPswd();
        // 将表单传输的明文密码进行加密，与取出的数据库中的密码进行比较
        String userPswdFromForm = CrowdUtils.md5(userPswd);
        // 比较结果不一致抛出异常
        if (!Objects.equals(userPswdFromDB, userPswdFromForm)) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 比较结果一致，返回 Admin 对象
        return admin;
    }
}
