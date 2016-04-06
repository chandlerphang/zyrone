package com.zyrone.framework.admin.security.service.remote;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.zyrone.framework.admin.security.domain.AdminPermission;
import com.zyrone.framework.admin.security.domain.AdminRole;
import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.core.security.ExploitProtectionService;
import com.zyrone.framework.core.service.ServiceException;

@Service("zrnAdminSecurityRemoteService")
public class AdminSecurityServiceRemote implements AdminSecurityService {
    
    private static final String ANONYMOUS_USER_NAME = "anonymousUser";
    private static final Logger LOG = LoggerFactory.getLogger(AdminSecurityServiceRemote.class);
    
    @Resource(name="zrnAdminSecurityService")
    protected com.zyrone.framework.admin.security.service.AdminSecurityService securityService;

    @Resource(name="zrnExploitProtectionService")
    protected ExploitProtectionService exploitProtectionService;
    
    @Override
    public com.zyrone.framework.admin.security.service.remote.AdminUser getAdminUser() throws ServiceException {
        AdminUser persistentAdminUser = getPersistentAdminUser();
        if (persistentAdminUser != null) {
        	com.zyrone.framework.admin.security.service.remote.AdminUser response = new com.zyrone.framework.admin.security.service.remote.AdminUser();
            for (AdminRole role : persistentAdminUser.getAllRoles()) {
                response.getRoles().add(role.getName());
                for (AdminPermission permission : role.getAllPermissions()) {
                    response.getPermissions().add(permission.getName());
                }
            }
            for (AdminPermission permission : persistentAdminUser.getAllPermissions()) {
                response.getPermissions().add(permission.getName());
            }
            response.setUserName(persistentAdminUser.getLogin());
            response.setEmail(persistentAdminUser.getEmail());
            response.setName(persistentAdminUser.getName());
            response.setPhoneNumber(persistentAdminUser.getPhoneNumber());
            response.setId(persistentAdminUser.getId());
            return response;
        }

        return null;
    }
    
    public AdminUser getPersistentAdminUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            if (auth != null && !auth.getName().equals(ANONYMOUS_USER_NAME)) {
                UserDetails temp = (UserDetails) auth.getPrincipal();

                return securityService.readAdminUserByLogin(temp.getUsername());
            }
        }

        return null;
    }
}
