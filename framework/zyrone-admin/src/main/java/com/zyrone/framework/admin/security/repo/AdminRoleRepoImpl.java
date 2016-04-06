package com.zyrone.framework.admin.security.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.zyrone.framework.admin.security.domain.AdminRole;
import com.zyrone.framework.admin.security.domain.AdminRoleImpl;

@Repository("zrnAdminRoleRepo")
public class AdminRoleRepoImpl implements AdminRoleRepo {

    @PersistenceContext(unitName = "zrnPU")
    protected EntityManager em;

    public void deleteAdminRole(AdminRole role) {
        if (!em.contains(role)) {
            role = readAdminRoleById(role.getId());
        }
        em.remove(role);
    }

    public AdminRole readAdminRoleById(Long id) {
        return em.find(AdminRoleImpl.class, id);
    }

    public AdminRole saveAdminRole(AdminRole role) {
        return em.merge(role);
    }

    public List<AdminRole> readAllAdminRoles() {
        TypedQuery<AdminRole> query = em.createNamedQuery("ZRN_READ_ALL_ADMIN_ROLES", AdminRole.class);
        List<AdminRole> roles = query.getResultList();
        return roles;
    }

}
