package com.zyrone.framework.admin.security.repo;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.jpa.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.admin.security.domain.AdminUserImpl;
import com.zyrone.util.repo.TypedQueryBuilder;

@Repository("zrnAdminUserRepo")
public class AdminUserRepoImpl implements AdminUserRepo {
	
	Logger logger = LoggerFactory.getLogger(AdminUserRepo.class);
	
    @PersistenceContext(unitName = "zrnPU")
    protected EntityManager em;

    public void deleteAdminUser(AdminUser user) {
        if (!em.contains(user)) {
            user = em.find(AdminUserImpl.class, user.getId());
        }
        em.remove(user);
    }

    public AdminUser readAdminUserById(Long id) {
        return em.find(AdminUserImpl.class, id);
    }
    
    @Override
    public List<AdminUser> readAdminUsersByIds(Set<Long> ids) {
        TypedQueryBuilder<AdminUser> tqb = new TypedQueryBuilder<AdminUser>(AdminUser.class, "au");

        if (ids != null && !ids.isEmpty()) {
            tqb.addRestriction("au.id", "in", ids);
        }
        
        TypedQuery<AdminUser> query = tqb.toQuery(em);
        return query.getResultList();
    }

    public AdminUser saveAdminUser(AdminUser user) {
        if (em.contains(user) || user.getId() != null) {
            return em.merge(user);
        } else {
            em.persist(user);
            return user;
        }
    }

    public AdminUser readAdminUserByLogin(String login) {
        TypedQuery<AdminUser> query = em.createNamedQuery("ZRN_READ_ADMIN_USER_BY_LOGIN", AdminUser.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter("login", login);
        List<AdminUser> users = query.getResultList();
        if (users != null && !users.isEmpty()) {
        	if (users != null && !users.isEmpty()) {
        		if(users.size() > 1 && logger.isErrorEnabled()) {
        			logger.error("Admin user with login value [ " + login +" ] is duplicate");
            	}
                return users.get(0);
            }
        }
        return null;
    }
    
    @Override
	public AdminUser readAdminUserByLoginOrPhoneOrEmail(String account) {
    	 TypedQuery<AdminUser> query = em.createNamedQuery("ZRN_READ_ADMIN_USER_BY_LOGIN_EMAIL_PHONE", AdminUser.class);
         query.setHint(QueryHints.HINT_CACHEABLE, true);
         query.setParameter("account", account);
         List<AdminUser> users = query.getResultList();
         if (users != null && !users.isEmpty()) {
        	 if(users.size() > 1 && logger.isErrorEnabled()) {
        		 logger.error("Admin user with login, email or phone value [ " + account +" ] is duplicate");
        	 }
             return users.get(0);
         }
         return null;
	}

    public List<AdminUser> readAllAdminUsers() {
        TypedQuery<AdminUser> query = em.createNamedQuery("ZRN_READ_ALL_ADMIN_USERS", AdminUser.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @Override
    public AdminUser readAdminUserByEmail(String emailAddress) {
        TypedQuery<AdminUser> query = em.createNamedQuery("ZRN_READ_ADMIN_USER_BY_EMAIL", AdminUser.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter("email", emailAddress);
        List<AdminUser> users = query.getResultList();
        if (users != null && !users.isEmpty()) {
       	 if(users.size() > 1 && logger.isErrorEnabled()) {
       		 logger.error("Admin user with email value [ " + emailAddress +" ] is duplicate");
       	 }
            return users.get(0);
        }
        return null;
    }

}
