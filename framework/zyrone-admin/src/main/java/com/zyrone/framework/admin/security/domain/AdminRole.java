package com.zyrone.framework.admin.security.domain;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Jason Phang
 */
public interface AdminRole extends Serializable {

    void setId(Long id);
    
    Long getId();
    
    String getName();
    
    void setName(String name);
    
    String getDescription();
    
    void setDescription(String description);
    
    Set<AdminPermission> getAllPermissions();
    
}
