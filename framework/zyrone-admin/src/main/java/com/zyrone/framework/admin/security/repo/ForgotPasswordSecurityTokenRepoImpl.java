package com.zyrone.framework.admin.security.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.zyrone.framework.admin.security.domain.ForgotPasswordSecurityToken;
import com.zyrone.framework.admin.security.domain.ForgotPasswordSecurityTokenImpl;
import com.zyrone.util.repo.TypedQueryBuilder;

@Repository("zrnForgotPasswordSecurityTokenRepo")
public class ForgotPasswordSecurityTokenRepoImpl implements ForgotPasswordSecurityTokenRepo {

    @PersistenceContext(unitName = "zrnPU")
    protected EntityManager em;

    @Override
    public ForgotPasswordSecurityToken readToken(String token) {
        return em.find(ForgotPasswordSecurityTokenImpl.class, token);        
    }

    @Override
    public List<ForgotPasswordSecurityToken> readUnusedTokensByAdminUserId(Long adminUserId) {
        TypedQuery<ForgotPasswordSecurityToken> query = new TypedQueryBuilder<ForgotPasswordSecurityToken>(ForgotPasswordSecurityToken.class, "token")
                .addRestriction("token.adminUserId", "=", adminUserId)
                .addRestriction("token.tokenUsedFlag", "=", false)
                .toQuery(em);
        return query.getResultList();
    }

    @Override
    public ForgotPasswordSecurityToken saveToken(ForgotPasswordSecurityToken token) {
        return em.merge(token);
    }
    
}
