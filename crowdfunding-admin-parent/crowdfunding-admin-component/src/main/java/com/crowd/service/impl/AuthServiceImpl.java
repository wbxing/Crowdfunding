package com.crowd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crowd.entity.Auth;
import com.crowd.entity.AuthExample;
import com.crowd.mapper.AuthMapper;
import com.crowd.service.api.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthIdRelationship(Map<String, List<Integer>> map) {
        // 获取 roleId
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        // 删除旧数据
        authMapper.deleteRelationship(roleId);
        // 保存新数据
        List<Integer> authIdList = map.get("authIdArray");
        if (authIdList != null && authIdList.size() > 0) {
            authMapper.insertRelationship(roleId, authIdList);
        }
    }

}
