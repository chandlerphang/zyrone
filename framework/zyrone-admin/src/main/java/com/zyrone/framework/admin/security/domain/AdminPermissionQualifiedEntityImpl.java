package com.zyrone.framework.admin.security.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ZRN_ADMIN_PERM_ENTITY")
public class AdminPermissionQualifiedEntityImpl implements AdminPermissionQualifiedEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminPermissionEntityId")
    @GenericGenerator(
        name="AdminPermissionEntityId",
        strategy="com.zyrone.framework.core.jpa.IdTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminPermissionEntityImpl")
        }
    )
    @Column(name = "ADMIN_PERM_ENTITY_ID")
    protected Long id;

    @Column(name = "CEILING_ENTITY", nullable=false)
    protected String ceilingEntityClassName;

    @ManyToOne(targetEntity = AdminPermissionImpl.class)
    @JoinColumn(name = "ADMIN_PERMISSION_ID")
    protected AdminPermission adminPermission;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCeilingEntityClassName() {
        return ceilingEntityClassName;
    }

    @Override
    public void setCeilingEntityClassName(String ceilingEntityClassName) {
        this.ceilingEntityClassName = ceilingEntityClassName;
    }

    @Override
    public AdminPermission getAdminPermission() {
        return adminPermission;
    }

    @Override
    public void setAdminPermission(AdminPermission adminPermission) {
        this.adminPermission = adminPermission;
    }

}
