package com.zyrone.framework.admin.security.repo;

import java.util.List;
import java.util.Set;

import com.zyrone.framework.admin.security.domain.AdminUser;

/**
 *
 * @author Jason Phang
 */
public interface AdminUserRepo {
	
    List<AdminUser> readAllAdminUsers();
    
    AdminUser readAdminUserById(Long id);
    
    AdminUser readAdminUserByLogin(String login);
    
    AdminUser readAdminUserByLoginOrPhoneOrEmail(String account);
    
    AdminUser saveAdminUser(AdminUser user);
    
    void deleteAdminUser(AdminUser user);
    
    AdminUser readAdminUserByEmail(String emailAddress);
    
    List<AdminUser> readAdminUsersByIds(Set<Long> ids);
    
}
