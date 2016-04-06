package com.zyrone.framework.admin.security.domain;

/**
 *
 * @author Jason Phang
 */
public interface AdminPermissionQualifiedEntity {
	
    Long getId();

    void setId(Long id);

    String getCeilingEntityClassName();

    void setCeilingEntityClassName(String ceilingEntityFullyQualifiedName);

    AdminPermission getAdminPermission();

    void setAdminPermission(AdminPermission adminPermission);
    
}
