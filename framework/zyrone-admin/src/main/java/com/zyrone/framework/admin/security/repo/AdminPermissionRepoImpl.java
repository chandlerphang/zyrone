package com.zyrone.framework.admin.security.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ClassUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import com.zyrone.framework.admin.security.domain.AdminPermission;
import com.zyrone.framework.admin.security.domain.AdminPermissionImpl;
import com.zyrone.framework.admin.security.domain.AdminUser;
import com.zyrone.framework.admin.security.service.AdminSecurityService;
import com.zyrone.framework.admin.security.type.PermissionType;

@Repository("zrnAdminPermissionRepo")
public class AdminPermissionRepoImpl implements AdminPermissionRepo {
    
    @PersistenceContext(unitName = "zrnPU")
    protected EntityManager em;

    public void deleteAdminPermission(AdminPermission permission) {
        if (!em.contains(permission)) {
            permission = readAdminPermissionById(permission.getId());
        }
        em.remove(permission);
    }

    public AdminPermission readAdminPermissionById(Long id) {
        return em.find(AdminPermissionImpl.class, id);
    }

    @Override
    public AdminPermission readAdminPermissionByNameAndType(String name, String type) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AdminPermission> criteria = builder.createQuery(AdminPermission.class);
        Root<AdminPermissionImpl> adminPerm = criteria.from(AdminPermissionImpl.class);
        criteria.select(adminPerm);

        List<Predicate> restrictions = new ArrayList<Predicate>();
        restrictions.add(builder.equal(adminPerm.get("name"), name));
        restrictions.add(builder.equal(adminPerm.get("type"), type));

        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
        TypedQuery<AdminPermission> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminPermission> results = query.getResultList();
        if (results == null || results.size() == 0) {
            return null;
        } else {
            return results.get(0);
        }
    }
    
    @Override
    public AdminPermission readAdminPermissionByName(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AdminPermission> criteria = builder.createQuery(AdminPermission.class);
        Root<AdminPermissionImpl> adminPerm = criteria.from(AdminPermissionImpl.class);
        criteria.select(adminPerm);

        List<Predicate> restrictions = new ArrayList<Predicate>();
        restrictions.add(builder.equal(adminPerm.get("name"), name));

        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
        TypedQuery<AdminPermission> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminPermission> results = query.getResultList();
        if (results == null || results.size() == 0) {
            return null;
        } else {
            return results.get(0);
        }
    }

    public AdminPermission saveAdminPermission(AdminPermission permission) {
        return em.merge(permission);
    }

    public List<AdminPermission> readAllAdminPermissions() {
        TypedQuery<AdminPermission> query = em.createNamedQuery("ZRN_READ_ALL_ADMIN_PERMISSIONS", AdminPermission.class);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List<AdminPermission> permissions = query.getResultList();
        return permissions;
    }

    public boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        List<String> testClasses = new ArrayList<String>();
        testClasses.add(ceilingEntityFullyQualifiedName);
        try {
            for (Object interfaze : ClassUtils.getAllInterfaces(Class.forName(ceilingEntityFullyQualifiedName))) {
                testClasses.add(((Class<?>) interfaze).getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String testClass : testClasses) {
            Query query = em.createNamedQuery("ZRN_COUNT_PERMISSIONS_FOR_USER_BY_TYPE_AND_CEILING_ENTITY");
            query.setParameter("adminUser", adminUser);
            query.setParameter("type", permissionType.getName());
            query.setParameter("ceilingEntity", testClass);
            query.setHint(QueryHints.HINT_CACHEABLE, true);

            Long count = (Long) query.getSingleResult();
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserQualifiedForOperationOnCeilingEntityViaDefaultPermissions(String ceilingEntityFullyQualifiedName) {
        List<String> testClasses = new ArrayList<String>();
        testClasses.add(ceilingEntityFullyQualifiedName);
        try {
            for (Object interfaze : ClassUtils.getAllInterfaces(Class.forName(ceilingEntityFullyQualifiedName))) {
                testClasses.add(((Class<?>) interfaze).getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String testClass : testClasses) {
            Query query = em.createNamedQuery("ZRN_COUNT_BY_PERMISSION_AND_CEILING_ENTITY");
            query.setParameter("permissionNames", Arrays.asList(AdminSecurityService.DEFAULT_PERMISSIONS));
            query.setParameter("ceilingEntity", testClass);
            query.setHint(QueryHints.HINT_CACHEABLE, true);

            Long count = (Long) query.getSingleResult();
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName) {
        List<String> testClasses = new ArrayList<String>();
        testClasses.add(ceilingEntityFullyQualifiedName);
        try {
            for (Object interfaze : ClassUtils.getAllInterfaces(Class.forName(ceilingEntityFullyQualifiedName))) {
                testClasses.add(((Class<?>) interfaze).getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String testClass : testClasses) {
            Query query = em.createNamedQuery("ZRN_COUNT_PERMISSIONS_BY_TYPE_AND_CEILING_ENTITY");
            query.setParameter("type", permissionType.getName());
            query.setParameter("ceilingEntity", testClass);
            query.setHint(QueryHints.HINT_CACHEABLE, true);

            Long count = (Long) query.getSingleResult();
            if (count > 0) {
                return true;
            }
        }
        return false;
    }
}
