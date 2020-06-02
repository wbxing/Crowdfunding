package com.crowd.service.api;

import java.util.List;
import java.util.Map;

import com.crowd.entity.Auth;

public interface AuthService {

    List<Auth> getAll();

    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    void saveRoleAuthIdRelationship(Map<String, List<Integer>> map);

}
