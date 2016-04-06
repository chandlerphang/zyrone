package com.zyrone.framework.admin.security.domain;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Jason Phang
 */
public interface AdminUser extends Serializable {
	
    Long getId();
    
    void setId(Long id);
    
    String getName();
    
    void setName(String name);
    
    String getLogin();
    
    void setLogin(String login);
    
    String getPassword();
    
    void setPassword(String password);
    
    String getPhoneNumber();
    
    void setPhoneNumber(String phone);

    String getEmail();
    
    void setEmail(String email);
    
    Set<AdminRole> getAllRoles();
    
    void setAllRoles(Set<AdminRole> allRoles);
    
    String getUnencodedPassword();
    
    void setUnencodedPassword(String unencodedPassword);
    
    Set<AdminPermission> getAllPermissions();
    
    void setAllPermissions(Set<AdminPermission> allPermissions);

    void setActiveStatusFlag(Boolean activeStatus);
    
    Boolean getActiveStatusFlag();

}
