package com.zyrone.framework.admin.security.repo;

import java.util.List;

import com.zyrone.framework.admin.security.domain.AdminRole;

/**
 *
 * @author Jason Phang
 */
public interface AdminRoleRepo {
	
    List<AdminRole> readAllAdminRoles();
    
    AdminRole readAdminRoleById(Long id);
    
    AdminRole saveAdminRole(AdminRole role);
    
    void deleteAdminRole(AdminRole role);
    
}
