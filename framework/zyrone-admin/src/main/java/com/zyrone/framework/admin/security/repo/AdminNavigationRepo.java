package com.zyrone.framework.admin.security.repo;

import java.util.List;

import com.zyrone.framework.admin.security.domain.AdminFunction;
import com.zyrone.framework.admin.security.domain.AdminModule;

/**
 *
 * @author Jason Phang
 */
public interface AdminNavigationRepo {

    List<AdminModule> readAllAdminModules();

    List<AdminFunction> readAllAdminFunctions();
    
    AdminFunction readAdminFunctionByClassAndUrl(Class<?> clazz, String functionId);

    AdminFunction readAdminFunctionByURI(String uri);

    AdminFunction readAdminFunctionByFunctionKey(String functionKey);

    AdminFunction save(AdminFunction adminFunction);

    void remove(AdminFunction adminFunction);

    AdminModule readAdminModuleByModuleKey(String moduleKey);

}
