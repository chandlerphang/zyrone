package com.zyrone.framework.admin.security.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.zyrone.framework.admin.security.type.PermissionType;

/**
 *
 * @author Jason Phang
 */
public interface AdminPermission extends Serializable {

    void setId(Long id);
    
    Long getId();
    
    String getName();
    
    void setName(String name);
    
    String getDescription();
    
    void setDescription(String description);
    
    PermissionType getType();
    
    void setType(PermissionType type);

    List<AdminPermissionQualifiedEntity> getQualifiedEntities();
    
    void setQualifiedEntities(List<AdminPermissionQualifiedEntity> qualifiedEntities);

    Set<AdminUser> getAllUsers();
    
    void setAllUsers(Set<AdminUser> allUsers);

    Set<AdminRole> getAllRoles();
    
    void setAllRoles(Set<AdminRole> allRoles);
    
    List<AdminPermission> getAllChildPermissions();
    
    List<AdminPermission> getAllParentPermissions();
    
    Boolean isFriendly();
}
