package com.zyrone.framework.admin.security.repo;

import java.util.List;

import com.zyrone.framework.admin.security.domain.ForgotPasswordSecurityToken;

/**
 *
 * @author Jason Phang
 */
public interface ForgotPasswordSecurityTokenRepo {
	
    ForgotPasswordSecurityToken readToken(String token);
    
    List<ForgotPasswordSecurityToken> readUnusedTokensByAdminUserId(Long adminId);
    
    ForgotPasswordSecurityToken saveToken(ForgotPasswordSecurityToken token);
    
}
