package com.zyrone.framework.admin.security.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 功能
 * 
 * @author Jason Phang
 */
public interface AdminFunction extends Serializable {

    Long getId();

    String getName();
    
    void setName(String name);

    String getFunctionKey();
    
    void setFunctionKey(String functionKey);

    String getUrl();
    
    void setUrl(String url);

    List<AdminPermission> getPermissions();
    
    void setPermissions(List<AdminPermission> permissions);

    AdminModule getModule();
    
    void setModule(AdminModule module);

    String getCeilingEntity();
    
    void setCeilingEntity(String ceilingEntity);

    Integer getDisplayOrder();
    
    void setDisplayOrder(Integer displayOrder);
    
}
