package com.zyrone.framework.admin.security.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台菜单
 * 
 * @author Jason Phang
 */
public class AdminMenu {

    private List<AdminFunction> adminFunctions = new ArrayList<AdminFunction>();

    public List<AdminFunction> getAdminFunctions() {
        return adminFunctions;
    }

    public void setAdminFunctions(List<AdminFunction> adminFunctions) {
        this.adminFunctions = adminFunctions;
    }
    
}
