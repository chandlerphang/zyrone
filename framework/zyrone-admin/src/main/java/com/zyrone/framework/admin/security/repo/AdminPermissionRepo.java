package com.zyrone.framework.admin.security.repo;

import java.util.List;

import com.zyrone.framework.admin.security.domain.AdminPermission;
import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.admin.security.type.PermissionType;

/**
 *
 * @author Jason Phang
 */
public interface AdminPermissionRepo {

    List<AdminPermission> readAllAdminPermissions();
    
    AdminPermission readAdminPermissionById(Long id);
    
    AdminPermission readAdminPermissionByName(String name);
    
    AdminPermission saveAdminPermission(AdminPermission permission);
    
    void deleteAdminPermission(AdminPermission permission);
    
    boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName);
    
    boolean isUserQualifiedForOperationOnCeilingEntityViaDefaultPermissions(String ceilingEntityFullyQualifiedName);
    
    boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName);
    
    AdminPermission readAdminPermissionByNameAndType(String name, String type);

}
