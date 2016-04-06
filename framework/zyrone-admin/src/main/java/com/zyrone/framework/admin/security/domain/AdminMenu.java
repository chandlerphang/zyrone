package com.zyrone.framework.admin.security.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台菜单
 * 
 * @author Jason Phang
 */
public class AdminMenu {

    private List<AdminModule> adminModules = new ArrayList<AdminModule>();

    public List<AdminModule> getAdminModules() {
        return adminModules;
    }

    public void setAdminModule(List<AdminModule> adminModules) {
        this.adminModules = adminModules;
    }
    
}
