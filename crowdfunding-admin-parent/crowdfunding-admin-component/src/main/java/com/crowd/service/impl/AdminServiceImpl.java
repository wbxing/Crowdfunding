package com.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.crowd.utils.CrowdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.entity.AdminExample;
import com.crowd.entity.AdminExample.Criteria;
import com.crowd.exception.LoginAcctAlreadyInUseException;
import com.crowd.exception.LoginFailedException;
import com.crowd.mapper.AdminMapper;
import com.crowd.service.api.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminMapper adminMapper;

    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void saveAdmin(Admin admin) {
        // 密码加密
        admin.setUserPswd(CrowdUtils.md5(admin.getUserPswd()));
        // 生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        try {
            // 执行保存
            adminMapper.insert(admin);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            // 为了不掩盖问题，如果当前捕获到的不是 DuplicateKeyException 类型的异常，则把当前捕获到的异常对象继续向上抛出
            throw e;
        }
    }

    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> admins = adminMapper.selectByExample(new AdminExample());
        return admins;
    }

    @Override
    public Admin getAllAdminByLoginAcct(String loginAcct, String userPswd) {
        // 根据登录对象查询
        AdminExample adminExample = new AdminExample();
        Criteria createCriteria = adminExample.createCriteria();
        createCriteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        // 查询结果为空则抛出异常
        if (admins.size() == 0 || admins == null) {
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

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 调用 PageHelper 的方法开启分页查询
        // PageHelper 的非侵入式设计，原本的查询不需要修改
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Admin> adminByKeyword = adminMapper.selectAdminByKeyword(keyword);
        // 封装到 PageInfo 对象中
        return new PageInfo<>(adminByKeyword);
    }

    @Override
    public void removeById(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        // 只更新有值得列
        adminMapper.updateByPrimaryKeySelective(admin);

    }

    @Override
    public void removeAdmin(List<Integer> adminIdList) {
        AdminExample example = new AdminExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdIn(adminIdList);
        adminMapper.deleteByExample(example);
    }

    @Override
    public PageInfo<Admin> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyword);
        // 封装为PageInfo 对象返回
        return new PageInfo<>(adminList);
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<String> roleIdList) {
        // 删除旧数据
        adminMapper.deleteRelationship(adminId);
        // 保存新数据
        if (roleIdList.size() > 0) {
            adminMapper.insertRelationship(adminId, roleIdList);
        }
    }
}
