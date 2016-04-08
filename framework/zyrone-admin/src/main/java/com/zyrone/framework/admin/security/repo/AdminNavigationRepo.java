package com.zyrone.framework.admin.security.repo;

import java.util.List;

import com.zyrone.framework.admin.security.domain.AdminFunction;

/**
 *
 * @author Jason Phang
 */
public interface AdminNavigationRepo {

	/**
	 * <p>
	 * 读取所有系统模块，模块就是没有父级的AdminFunction
	 * </p>
	 */
    List<AdminFunction> readAllAdminModules();

    List<AdminFunction> readAllAdminFunctions();
    
    AdminFunction readAdminFunctionByClassAndUrl(Class<?> clazz, String functionId);

    AdminFunction readAdminFunctionByURI(String uri);

    AdminFunction readAdminFunctionByFunctionKey(String functionKey);

    AdminFunction save(AdminFunction adminFunction);

    void remove(AdminFunction adminFunction);

}
