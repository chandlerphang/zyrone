package com.zyrone.framework.admin.security.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zyrone.framework.admin.security.domain.AdminPermission;
import com.zyrone.framework.admin.security.domain.AdminRole;
import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.admin.security.domain.ForgotPasswordSecurityToken;
import com.zyrone.framework.admin.security.repo.AdminPermissionRepo;
import com.zyrone.framework.admin.security.repo.AdminRoleRepo;
import com.zyrone.framework.admin.security.repo.AdminUserRepo;
import com.zyrone.framework.admin.security.repo.ForgotPasswordSecurityTokenRepo;
import com.zyrone.framework.admin.security.type.PermissionType;
import com.zyrone.framework.core.service.GenericResponse;

@Service("zrnAdminSecurityService")
public class AdminSecurityServiceImpl implements AdminSecurityService {

    @Resource(name = "zrnAdminRoleRepo")
    protected AdminRoleRepo adminRoleRepo;

    @Resource(name = "zrnAdminUserRepo")
    protected AdminUserRepo adminUserRepo;
    
    @Resource(name = "zrnForgotPasswordSecurityTokenRepo")
    protected ForgotPasswordSecurityTokenRepo forgotPasswordSecurityTokenDao;

    @Resource(name = "zrnAdminPermissionRepo")
    protected AdminPermissionRepo adminPermissionRepo;

    @Resource(name="zrnAdminPasswordEncoder")
    protected PasswordEncoder passwordEncoderNew;

    @Override
    @Transactional("zrnTransactionManager")
    public void deleteAdminPermission(AdminPermission permission) {
        adminPermissionRepo.deleteAdminPermission(permission);
    }

    @Override
    @Transactional("zrnTransactionManager")
    public void deleteAdminRole(AdminRole role) {
        adminRoleRepo.deleteAdminRole(role);
    }

    @Override
    @Transactional("zrnTransactionManager")
    public void deleteAdminUser(AdminUser user) {
        adminUserRepo.deleteAdminUser(user);
    }

    @Override
    public AdminPermission readAdminPermissionById(Long id) {
        return adminPermissionRepo.readAdminPermissionById(id);
    }

    @Override
    public AdminRole readAdminRoleById(Long id) {
        return adminRoleRepo.readAdminRoleById(id);
    }

    @Override
    public AdminUser readAdminUserById(Long id) {
        return adminUserRepo.readAdminUserById(id);
    }
    
    @Override
    public AdminUser readAdminUserByLogin(String login) {
        return adminUserRepo.readAdminUserByLogin(login);
    }
    
	@Override
	public AdminUser readAdminUserByLoginOrPhoneOrEmail(String acount) {
		return adminUserRepo.readAdminUserByLoginOrPhoneOrEmail(acount);
	}

    @Override
    public AdminUser readAdminUsersByEmail(String email) {
        return adminUserRepo.readAdminUserByEmail(email);
    }

    @Override
    public List<AdminUser> readAllAdminUsers() {
        return adminUserRepo.readAllAdminUsers();
    }

    @Override
    @Transactional("zrnTransactionManager")
    public AdminPermission saveAdminPermission(AdminPermission permission) {
        return adminPermissionRepo.saveAdminPermission(permission);
    }

    @Override
    @Transactional("zrnTransactionManager")
    public AdminRole saveAdminRole(AdminRole role) {
        return adminRoleRepo.saveAdminRole(role);
    }

    @Override
    @Transactional("zrnTransactionManager")
    public AdminUser saveAdminUser(AdminUser user) {
        boolean encodePasswordNeeded = false;
        String unencodedPassword = user.getUnencodedPassword();

        if (user.getUnencodedPassword() != null) {
            encodePasswordNeeded = true;
            user.setPassword(unencodedPassword);
        }

        if (user.getPassword() == null) {
            user.setPassword(generateSecurePassword());
        }

        AdminUser returnUser = adminUserRepo.saveAdminUser(user);
        if (encodePasswordNeeded) {
            returnUser.setPassword(encodePassword(unencodedPassword));
        }

        return adminUserRepo.saveAdminUser(returnUser);
    }
    
    protected String generateSecurePassword() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

    @Override
    public boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        boolean response = adminPermissionRepo.isUserQualifiedForOperationOnCeilingEntity(adminUser, permissionType, ceilingEntityFullyQualifiedName);
        if (!response) {
            response = adminPermissionRepo.isUserQualifiedForOperationOnCeilingEntityViaDefaultPermissions(ceilingEntityFullyQualifiedName);
        }
        return response;
    }

    @Override
    public boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        return adminPermissionRepo.doesOperationExistForCeilingEntity(permissionType, ceilingEntityFullyQualifiedName);
    }

    @Override
    public List<AdminRole> readAllAdminRoles() {
        return adminRoleRepo.readAllAdminRoles();
    }

    @Override
    public List<AdminPermission> readAllAdminPermissions() {
        return adminPermissionRepo.readAllAdminPermissions();
    }

    protected void invalidateAllTokensForAdminUser(AdminUser user) {
        List<ForgotPasswordSecurityToken> tokens = forgotPasswordSecurityTokenDao.readUnusedTokensByAdminUserId(user.getId());
        for (ForgotPasswordSecurityToken token : tokens) {
            token.setTokenUsedFlag(true);
            forgotPasswordSecurityTokenDao.saveToken(token);
        }
    }

    protected void checkUser(AdminUser user, GenericResponse response) {
        if (user == null) {
            response.addErrorCode("invalidUser");
        } else if (StringUtils.isBlank(user.getEmail())) {
            response.addErrorCode("emailNotFound");
        } else if (BooleanUtils.isNotTrue(user.getActiveStatusFlag())) {
            response.addErrorCode("inactiveUser");
        }
    }
    
    protected void checkPassword(String password, String confirmPassword, GenericResponse response) {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            response.addErrorCode("invalidPassword");
        } else if (! password.equals(confirmPassword)) {
            response.addErrorCode("passwordMismatch");
        }
    }

    protected boolean isPasswordValid(String encodedPassword, String rawPassword) {
        return passwordEncoderNew.matches(rawPassword, encodedPassword);
    }

    protected String encodePassword(String rawPassword) {
        return passwordEncoderNew.encode(rawPassword);
    }

}
