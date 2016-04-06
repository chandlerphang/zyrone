package com.zyrone.framework.admin.security.domain;

import java.io.Serializable;
import java.util.List;

public interface AdminModule extends Serializable {

    Long getId();

    String getName();

    void setName(String name);

    String getModuleKey();

    void setModuleKey(String moduleKey);

    String getIcon();

    void setIcon(String icon);

    List<AdminFunction> getFunctions();

    void setFunctions(List<AdminFunction> functions);

    Integer getDisplayOrder();

    void setDisplayOrder(Integer displayOrder);

}
