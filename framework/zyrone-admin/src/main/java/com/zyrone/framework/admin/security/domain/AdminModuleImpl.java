package com.zyrone.framework.admin.security.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ZRN_ADMIN_MODULE", indexes={
		 @Index(name="ADMINMODULE_NAME_INDEX", columnList="NAME")
})
public class AdminModuleImpl implements AdminModule {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminModuleId")
    @GenericGenerator(
        name="AdminModuleId",
        strategy="com.zyrone.framework.core.jpa.IdTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminModuleImpl")
        }
    )
    @Column(name = "ADMIN_MODULE_ID")
    protected Long id;

    @Column(name = "NAME", nullable=false)
    protected String name;

    @Column(name = "MODULE_KEY", nullable=false)
    protected String moduleKey;

    @Column(name = "ICON", nullable=true)
    protected String icon;

    @OneToMany(mappedBy = "module", targetEntity = AdminFunctionImpl.class)
    @BatchSize(size = 50)
    protected List<AdminFunction> functions = new ArrayList<AdminFunction>();

    @Column(name = "DISPLAY_ORDER", nullable=true)
    protected Integer displayOrder;

    @Override
    public Long getId() {
        return id;
    }

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
    public String getModuleKey() {
        return moduleKey;
    }

    @Override
    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public List<AdminFunction> getFunctions() {
        return functions;
    }

    @Override
    public void setFunctions(List<AdminFunction> sections) {
        this.functions = sections;
    }

    @Override
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    @Override
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public AdminModuleDTO getAdminModuleDTO() {
        AdminModuleDTO dto = new AdminModuleDTO();
        dto.setDisplayOrder(displayOrder);
        dto.setIcon(icon);
        dto.setId(id);
        dto.setModuleKey(moduleKey);
        dto.setName(name);
        return dto;
    }
}
