package com.zyrone.framework.admin.security.service;

import java.util.List;

import com.zyrone.framework.admin.security.domain.AdminFunction;
import com.zyrone.framework.admin.security.domain.AdminMenu;
import com.zyrone.framework.admin.security.domain.AdminModule;
import com.zyrone.framework.admin.security.domain.AdminUser;


/**
 * 导航菜单服务
 * 
 * @author Jason Phang
 */
public interface AdminNavigationService {

    AdminMenu buildMenu(AdminUser adminUser);

    boolean isUserAuthorizedToViewFunction(AdminUser adminUser, AdminFunction function);

    boolean isUserAuthorizedToViewModule(AdminUser adminUser, AdminModule module);

    AdminFunction findAdminFunctionByURI(String uri);

    AdminFunction findAdminFunctionByFunctionKey(String functionKey);

    List<AdminFunction> findAllAdminFunctions();

    AdminFunction save(AdminFunction adminFunction);

    void remove(AdminFunction adminSection);

	AdminFunction findAdminFunctionByClassAndUrl(Class<?> clazz, String url);

	AdminFunction findAdminFunctionByClassAndUrl(String className, String url);

}
