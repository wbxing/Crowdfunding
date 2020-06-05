package com.crowd.mvc.config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.crowd.entity.Admin;

/**
 * 考虑到 User类只包含账号和密码，为了能够得到原始的 Admin 对象，专门创建这个类对 User 进行扩展
 *
 */
public class SecurityAdmin extends User {

    private static final long serialVersionUID = 1L;
    // 原始 Admin 对象
    private Admin originalAdmin;

    /**
     * 传入原始对象信息，擦除密码信息
     * @param originalAdmin 原始 Admin 对象
     * @param authorities 角色权限信息的集合
     */
    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;
        // 擦除密码
        this.originalAdmin.setUserPswd(null);
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }

}
