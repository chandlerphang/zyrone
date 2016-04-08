package com.zyrone.framework.admin.security.service;

import java.util.List;

import com.zyrone.framework.admin.security.domain.AdminFunction;
import com.zyrone.framework.admin.security.domain.AdminMenu;
import com.zyrone.framework.admin.security.domain.AdminUser;


/**
 * 导航菜单服务
 * 
 * @author Jason Phang
 */
public interface AdminNavigationService {

    AdminMenu buildMenu(AdminUser adminUser);

    boolean isUserAuthorizedToViewFunction(AdminUser adminUser, AdminFunction function);

    AdminFunction findAdminFunctionByURI(String uri);

    AdminFunction findAdminFunctionByKey(String functionKey);

    List<AdminFunction> findAllAdminFunctions();
    
    /**
     * 查找function的所有祖先结点的id，包括自己的id
     * 
     * @param funcKey
     * @return
     */
    List<Long> findAncestorIds(String funcKey);

    AdminFunction save(AdminFunction adminFunction);

    void remove(AdminFunction adminSection);

	AdminFunction findAdminFunctionByClassAndUrl(Class<?> clazz, String url);

	AdminFunction findAdminFunctionByClassAndUrl(String className, String url);

}
