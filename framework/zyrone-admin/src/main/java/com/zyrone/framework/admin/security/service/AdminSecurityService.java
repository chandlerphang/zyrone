package com.zyrone.framework.admin.security.service;

import java.util.List;

import com.zyrone.framework.admin.security.domain.AdminPermission;
import com.zyrone.framework.admin.security.domain.AdminRole;
import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.admin.security.type.PermissionType;

/**
 *
 * @author Jason Phang
 */
public interface AdminSecurityService {

    final String[] DEFAULT_PERMISSIONS = { "PERMISSION_OTHER_DEFAULT" };

    List<AdminUser> readAllAdminUsers();
    
    AdminUser readAdminUserById(Long id);
    
    AdminUser readAdminUserByLogin(String login);
    
    AdminUser readAdminUserByLoginOrPhoneOrEmail(String acount);
    
    AdminUser saveAdminUser(AdminUser user);
    
    void deleteAdminUser(AdminUser user);
    
    AdminUser readAdminUsersByEmail(String email);

    List<AdminRole> readAllAdminRoles();
    
    AdminRole readAdminRoleById(Long id);
    
    AdminRole saveAdminRole(AdminRole role);
    
    void deleteAdminRole(AdminRole role);

    List<AdminPermission> readAllAdminPermissions();
    
    AdminPermission readAdminPermissionById(Long id);
    
    AdminPermission saveAdminPermission(AdminPermission permission);
    
    void deleteAdminPermission(AdminPermission permission);

    boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName);
    
    boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName);

}
