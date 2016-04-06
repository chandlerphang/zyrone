package com.zyrone.framework.admin.security.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.zyrone.framework.admin.security.type.PermissionType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ZRN_ADMIN_PERMISSION")
public class AdminPermissionImpl implements AdminPermission {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminPermissionId")
    @GenericGenerator(
        name="AdminPermissionId",
        strategy="com.zyrone.framework.core.jpa.IdTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminPermissionImpl")
        }
    )
    @Column(name = "ADMIN_PERMISSION_ID")
    protected Long id;

    @Column(name = "NAME", nullable=false)
    protected String name;

    @Column(name = "PERMISSION_TYPE", nullable=false)
    protected String type;

    @Column(name = "DESCRIPTION", nullable=false)
    protected String description;
    
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminRoleImpl.class)
    @JoinTable(name = "ZRN_ADMIN_ROLE_PERM_XREF", 
    	joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"),
    	inverseJoinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"))
    @BatchSize(size = 50)
    protected Set<AdminRole> allRoles= new HashSet<AdminRole>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminUserImpl.class)
    @JoinTable(name = "ZRN_ADMIN_USER_PERM_XREF", 
    	joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), 
    	inverseJoinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"))
    @BatchSize(size = 50)
    protected Set<AdminUser> allUsers= new HashSet<AdminUser>();

    @OneToMany(mappedBy = "adminPermission", targetEntity = AdminPermissionQualifiedEntityImpl.class, orphanRemoval=true, cascade = {CascadeType.ALL})
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    @BatchSize(size = 50)
    protected List<AdminPermissionQualifiedEntity> qualifiedEntities = new ArrayList<AdminPermissionQualifiedEntity>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermissionImpl.class)
    @JoinTable(name = "ZRN_ADMIN_PERM_XREF", 
    	joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), 
    	inverseJoinColumns = @JoinColumn(name = "CHILD_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    @BatchSize(size = 50)
    protected List<AdminPermission> allChildPermissions = new ArrayList<AdminPermission>();
    
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermissionImpl.class)
    @JoinTable(name = "ZRN_ADMIN_PERM_XREF", 
    	joinColumns = @JoinColumn(name = "CHILD_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), 
    	inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    @BatchSize(size = 50)
    protected List<AdminPermission> allParentPermissions = new ArrayList<AdminPermission>();

    @Column(name = "IS_FRIENDLY")
    protected Boolean isFriendly = Boolean.FALSE;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    @Override
    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }

    @Override
    public PermissionType getType() {
    	return PermissionType.valueOf(type);
    }

    @Override
    public void setType(PermissionType type) {
        if (type != null) {
            this.type = type.getName();
        }
    }

    @Override
    public List<AdminPermissionQualifiedEntity> getQualifiedEntities() {
        return qualifiedEntities;
    }

    @Override
    public void setQualifiedEntities(List<AdminPermissionQualifiedEntity> qualifiedEntities) {
        this.qualifiedEntities = qualifiedEntities;
    }

    @Override
    public Set<AdminUser> getAllUsers() {
        return allUsers;
    }

    @Override
    public void setAllUsers(Set<AdminUser> allUsers) {
        this.allUsers = allUsers;
    }

    @Override
    public List<AdminPermission> getAllChildPermissions() {
        return allChildPermissions;
    }

    @Override
    public List<AdminPermission> getAllParentPermissions() {
        return allParentPermissions;
    }

    @Override
    public Boolean isFriendly() {
        if(isFriendly == null) {
            return false;
        }
        return isFriendly;
    }
    
}
