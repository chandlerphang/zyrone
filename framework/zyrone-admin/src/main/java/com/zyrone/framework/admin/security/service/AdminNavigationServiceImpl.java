package com.zyrone.framework.admin.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zyrone.framework.admin.security.domain.AdminFunction;
import com.zyrone.framework.admin.security.domain.AdminMenu;
import com.zyrone.framework.admin.security.domain.AdminModule;
import com.zyrone.framework.admin.security.domain.AdminModuleDTO;
import com.zyrone.framework.admin.security.domain.AdminModuleImpl;
import com.zyrone.framework.admin.security.domain.AdminPermission;
import com.zyrone.framework.admin.security.domain.AdminRole;
import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.admin.security.repo.AdminNavigationRepo;

/**
 *
 * @author Jason Phang
 */
@Service("zrnAdminNavigationService")
public class AdminNavigationServiceImpl implements AdminNavigationService {

	private static final Logger LOG = LoggerFactory.getLogger(AdminNavigationServiceImpl.class);
    private static final String PATTERN = "_";

    @Resource(name = "zrnAdminNavigationRepo")
    protected AdminNavigationRepo adminNavigationRepo;

    @Override
    @Transactional("zrnTransactionManager")
    public AdminFunction save(AdminFunction adminFunction) {
        return adminNavigationRepo.save(adminFunction);
    }

    @Override
    public void remove(AdminFunction adminFunction) {
        adminNavigationRepo.remove(adminFunction);
    }

    @Override
    public AdminMenu buildMenu(AdminUser adminUser) {
        AdminMenu adminMenu = new AdminMenu();
        List<AdminModule> modules = adminNavigationRepo.readAllAdminModules();
        populateAdminMenu(adminUser, adminMenu, modules);
        return adminMenu;
    }

    protected void populateAdminMenu(AdminUser adminUser, AdminMenu adminMenu, List<AdminModule> modules) {
        for (AdminModule module : modules) {
            List<AdminFunction> functions = buildAuthorizedFunctionsList(adminUser, module);
            if (functions != null && functions.size() > 0) {
                AdminModuleDTO adminModuleDto = ((AdminModuleImpl) module).getAdminModuleDTO();
                adminMenu.getAdminModules().add(adminModuleDto);
                adminModuleDto.setFunctions(functions);
            }
        }
        
        Collections.sort(adminMenu.getAdminModules(), new Comparator<AdminModule>() {
			@Override
			public int compare(AdminModule o1, AdminModule o2) {
				return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
			}
        });
    }

    protected List<AdminFunction> buildAuthorizedFunctionsList(AdminUser adminUser, AdminModule module) {
        List<AdminFunction> authorizedFunctions = new ArrayList<AdminFunction>();
        for (AdminFunction function : module.getFunctions()) {
            if (isUserAuthorizedToViewFunction(adminUser, function)) {
            	authorizedFunctions.add(function);
            }
        }

        Collections.sort(authorizedFunctions, FUNCTION_COMPARATOR);
        return authorizedFunctions;
    }

    @Override
    public boolean isUserAuthorizedToViewModule(AdminUser adminUser, AdminModule module) {
        List<AdminFunction> functions = module.getFunctions();
        if (functions != null && !functions.isEmpty()) {
            for (AdminFunction function : functions) {
                if (isUserAuthorizedToViewFunction(adminUser, function)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    @Override
    public AdminFunction findAdminFunctionByURI(String uri) {
        return adminNavigationRepo.readAdminFunctionByURI(uri);
    }
    
    @Override
    public AdminFunction findAdminFunctionByFunctionKey(String functionKey) {
        return adminNavigationRepo.readAdminFunctionByFunctionKey(functionKey);
    }
    
    @Override
	public AdminFunction findAdminFunctionByClassAndUrl(String className, String url) {
        try {
            return findAdminFunctionByClassAndUrl(Class.forName(className), url);
        } catch (ClassNotFoundException e) {
            LOG.warn("Invalid classname received. This likely points to a configuration error.");
            return null;
        }
    }
    
    @Override
	public AdminFunction findAdminFunctionByClassAndUrl(Class<?> clazz, String url) {
    	return adminNavigationRepo.readAdminFunctionByClassAndUrl(clazz, url);
    }
    
    @Override
    public boolean isUserAuthorizedToViewFunction(AdminUser adminUser, AdminFunction function) {
        boolean response = false;
        List<AdminPermission> authorizedPermissions = function.getPermissions();
        checkAuth: {
            if (!CollectionUtils.isEmpty(adminUser.getAllRoles())) {
                for (AdminRole role : adminUser.getAllRoles()) {
                    for (AdminPermission permission : role.getAllPermissions()){
                        if (checkPermissions(authorizedPermissions, permission)) {
                            response = true;
                            break checkAuth;
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(adminUser.getAllPermissions())) {
                for (AdminPermission permission : adminUser.getAllPermissions()){
                    if (checkPermissions(authorizedPermissions, permission)) {
                        response = true;
                        break checkAuth;
                    }
                }
            }
            for (String defaultPermission : AdminSecurityService.DEFAULT_PERMISSIONS) {
                for (AdminPermission authorizedPermission : authorizedPermissions) {
                    if (authorizedPermission.getName().equals(defaultPermission)) {
                        response = true;
                        break checkAuth;
                    }
                }
            }
        }

        return response;
    }
    
    @Override
    public List<AdminFunction> findAllAdminFunctions() {
        List<AdminFunction> sections = adminNavigationRepo.readAllAdminFunctions();
        Collections.sort(sections, FUNCTION_COMPARATOR);
        return sections;
    }

    protected boolean checkPermissions(List<AdminPermission> authorizedPermissions, AdminPermission permission) {
        if (authorizedPermissions != null) {
            if (authorizedPermissions.contains(permission)){
                return true;
            }

            for (AdminPermission authorizedPermission : authorizedPermissions) {
                if (permission.getName().equals(parseForAllPermission(authorizedPermission.getName()))) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String parseForAllPermission(String currentPermission) {
        String[] pieces = currentPermission.split(PATTERN);
        StringBuilder builder = new StringBuilder(50);
        builder.append(pieces[0]);
        builder.append("_ALL_");
        for (int j = 2; j<pieces.length; j++) {
            builder.append(pieces[j]);
            if (j < pieces.length - 1) {
                builder.append('_');
            }
        }
        return builder.toString();
    }

    private static Comparator<AdminFunction> FUNCTION_COMPARATOR = new Comparator<AdminFunction>() {
		@Override
		public int compare(AdminFunction o1, AdminFunction o2) {
			if (o1.getDisplayOrder() != null) {
                if (o2.getDisplayOrder() != null) {
                    return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
                }
                else {
                    return -1;
                }
            } else if (o2.getDisplayOrder() != null) {
                return 1;
            }

	        return o1.getId().compareTo(o2.getId());
		}
    };

}
