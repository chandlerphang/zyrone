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
    
    String getIcon();

    void setIcon(String icon);

    String getUrl();
    
    void setUrl(String url);

    List<AdminPermission> getPermissions();
    
    void setPermissions(List<AdminPermission> permissions);
    
    AdminFunction getParent();
    
    void setParent(AdminFunction parent);
    
    List<AdminFunction> getChildren();

    void setChildren(List<AdminFunction> functions);

    Integer getDisplayOrder();
    
    void setDisplayOrder(Integer displayOrder);
    
    String getCeilingEntity();
    
    void setCeilingEntity(String ceilingEntity);
    
    AdminFunction copyWithoutHierarchy();
    
}
