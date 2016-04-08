package com.zyrone.framework.admin.security.service;

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
import com.zyrone.framework.admin.security.domain.AdminPermission;
import com.zyrone.framework.admin.security.domain.AdminRole;
import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.admin.security.repo.AdminNavigationRepo;

import static com.zyrone.util.CollectionUtil.*;

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
        List<AdminFunction> modules = adminNavigationRepo.readAllAdminModules();
        populateAdminMenu(adminUser, adminMenu, modules);
        return adminMenu;
    }

    protected void populateAdminMenu(AdminUser adminUser, AdminMenu adminMenu, List<AdminFunction> modules) {
        List<AdminFunction> functions = createArrayList();
    	for (AdminFunction module : modules) {
        	AdminFunction funcTree = buildAuthorizedFunctionTree(adminUser, module);
            if (funcTree != null) {
                functions.add(funcTree);
            }
        }
        
        Collections.sort(functions, FUNCTION_COMPARATOR);
        adminMenu.setAdminFunctions(functions);
    }
    
    protected AdminFunction buildAuthorizedFunctionTree(AdminUser adminUser, AdminFunction root) {
    	/** 先判断对root结点本身是否具有权限，如果没有的话，直接返回null，表示不予显示，没必要还对它的子结点进行权限判断 */
        if(isUserAuthorizedToViewFunction(adminUser, root)) {
        	/** 这里做了一个逻辑假设：一个Function如果没有任何子结点的话，就表示它是用户最终会点击的“菜单项”，不会是组织“菜单项”的中间模块
        	 *  事实上这个假设不一定成立，比如创建了一个中间模块，但忘记给它添加子结点了，中间模块的url属性值都是无效的 */
        	if(root.getChildren() != null && root.getChildren().size() > 0) {
        		List<AdminFunction> authorizedFunctions = createArrayList();
        		for(AdminFunction func : root.getChildren()) {
        			AdminFunction funcTree = buildAuthorizedFunctionTree(adminUser, func);
        			if(funcTree != null) {
        				authorizedFunctions.add(funcTree);
        			}
             	}
        		
        		if(authorizedFunctions.isEmpty()) {
        			/** 没有任何被授权的子Function结点, 该root结点也不应展示 */
        			return null;
        		} else {
        			Collections.sort(authorizedFunctions, FUNCTION_COMPARATOR);
        			
        			AdminFunction temp = root.copyWithoutHierarchy();
        			temp.setParent(root.getParent());
        			temp.setChildren(authorizedFunctions);
        			return temp;
        		}
        	} else {
        		AdminFunction temp = root.copyWithoutHierarchy();
    			temp.setParent(root.getParent());
        		return temp;
            }
        } else {
        	return null;
        }
    }

    @Override
    public AdminFunction findAdminFunctionByURI(String uri) {
        return adminNavigationRepo.readAdminFunctionByURI(uri);
    }
    
    @Override
    public AdminFunction findAdminFunctionByKey(String functionKey) {
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
        List<AdminFunction> functions = adminNavigationRepo.readAllAdminFunctions();
        Collections.sort(functions, FUNCTION_COMPARATOR);
        return functions;
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

	@Override
	public List<Long> findAncestorIds(String funcKey) {
		AdminFunction function = adminNavigationRepo.readAdminFunctionByFunctionKey(funcKey);
		List<Long> ids = createArrayList();
		while(function != null) {
			ids.add(0, function.getId());
			function = function.getParent();
		}
		return ids;
	}

}
