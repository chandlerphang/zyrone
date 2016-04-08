package com.zyrone.framework.admin.security.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.zyrone.framework.admin.security.domain.AdminFunction;

@Repository("zrnAdminNavigationRepo")
public class AdminNavigationRepoImpl implements AdminNavigationRepo {

    @PersistenceContext(unitName = "zrnPU")
    protected EntityManager em;
    
    @Override
    public AdminFunction save(AdminFunction adminFunction) {
        return em.merge(adminFunction);
    }

    @Override
    public void remove(AdminFunction adminFunction) {
        em.remove(adminFunction);
    }

    @Override
    public List<AdminFunction> readAllAdminModules() {
        TypedQuery<AdminFunction> query = em.createNamedQuery("ZRN_READ_ALL_ADMIN_MODULES", AdminFunction.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminFunction> modules = query.getResultList();
        return modules;
    }
    
    @Override
    public List<AdminFunction> readAllAdminFunctions() {
    	TypedQuery<AdminFunction> query = em.createNamedQuery("ZRN_READ_ALL_ADMIN_FUNCTIONS", AdminFunction.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminFunction> sections = query.getResultList();
        return sections;
    }
    
    @Override
    public AdminFunction readAdminFunctionByClassAndUrl(Class<?> clazz, String functionId) {
        String className = clazz.getName();
        
        List<AdminFunction> functions = readAdminFunctionForClassName(className);
        if (CollectionUtils.isEmpty(functions)) {
            if (className.endsWith("Impl")) {
                className = className.substring(0, className.length() - 4);
                functions = readAdminFunctionForClassName(className);
            }
        }
        
        if (!CollectionUtils.isEmpty(functions)) {
        	AdminFunction returnFunction = functions.get(0);
            if (functionId != null) {
                if (!functionId.startsWith("/")) {
                    functionId = "/" + functionId;
                }
                for (AdminFunction function : functions) {
                    if (functionId.equals(function.getUrl())) {
                        returnFunction = function;
                        break;
                    }
                }
            }
            return returnFunction;
        }
        
        return null;
    }
    
    protected List<AdminFunction> readAdminFunctionForClassName(String className) {
        TypedQuery<AdminFunction> q = em.createQuery(
            "select s from " + AdminFunction.class.getName() + " s where s.ceilingEntity = :className", AdminFunction.class);
        q.setParameter("className", className);
        q.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminFunction> result = q.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return q.getResultList();
    }

    @Override
    public AdminFunction readAdminFunctionByURI(String uri) {
        Query query = em.createNamedQuery("ZRN_READ_ADMIN_FUNCTION_BY_URI");
        query.setParameter("uri", uri);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        AdminFunction adminFunction = null;
        try {
             adminFunction = (AdminFunction) query.getSingleResult();
        } catch (NoResultException e) {
        	// do nothing
        }
        return adminFunction;
    }

    @Override
    public AdminFunction readAdminFunctionByFunctionKey(String functionKey) {
        Query query = em.createNamedQuery("ZRN_READ_ADMIN_FUNCTION_BY_FUNCTION_KEY");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter("functionKey", functionKey);
        AdminFunction adminSection = null;
        try {
            adminSection = (AdminFunction) query.getSingleResult();
        } catch (NoResultException e) {
        	// do nothing
        }
        return adminSection;
    }

}
