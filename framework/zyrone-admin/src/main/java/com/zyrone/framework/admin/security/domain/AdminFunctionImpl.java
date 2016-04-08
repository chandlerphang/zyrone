package com.zyrone.framework.admin.security.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import static com.zyrone.util.CollectionUtil.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ZRN_ADMIN_FUNCITON", indexes = {
		 @Index(name="ADMINFUNCITON_NAME_INDEX", columnList="NAME"),
})
public class AdminFunctionImpl implements AdminFunction {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminFunctionId")
    @GenericGenerator(
        name="AdminFunctionId",
        strategy="com.zyrone.framework.core.jpa.IdTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminFunctionImpl")
        }
    )
    @Column(name = "ADMIN_FUNCTION_ID")
    protected Long id;

    @Column(name = "NAME", nullable=false)
    protected String name;

    @Column(name = "FUNCTION_KEY", nullable=false, unique = true)
    protected String functionKey;

    @Column(name = "URL", nullable=true)
    protected String url;
    
    @Column(name = "ICON", nullable=true)
    protected String icon;

    @ManyToOne(targetEntity = AdminFunctionImpl.class)
    @JoinColumn(name = "PARENT_ID")
    protected AdminFunction parent;
    
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, targetEntity = AdminFunctionImpl.class)
    @BatchSize(size = 10)
    protected List<AdminFunction> children = createArrayList();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermissionImpl.class)
    @JoinTable(name = "ZRN_ADMIN_FUNC_PERM_XREF", 
    	joinColumns = @JoinColumn(name = "ADMIN_FUNCTION_ID", referencedColumnName = "ADMIN_FUNCTION_ID"), 
    	inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    @BatchSize(size = 50)
    protected List<AdminPermission> permissions = createArrayList();

    @Column(name = "CEILING_ENTITY", nullable = true)
    protected String ceilingEntity;

    @Column(name = "DISPLAY_ORDER", nullable = true)
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
	public String getFunctionKey() {
		return functionKey;
	}

	@Override
	public void setFunctionKey(String functionKey) {
		this.functionKey = functionKey;
	}

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public List<AdminPermission> getPermissions() {
        return permissions;
    }

    @Override
    public void setPermissions(List<AdminPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getCeilingEntity() {
        return ceilingEntity;
    }

    @Override
    public void setCeilingEntity(String ceilingEntity) {
        this.ceilingEntity = ceilingEntity;
    }

    @Override
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    @Override
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

	@Override
	public AdminFunction getParent() {
		return parent;
	}

	@Override
	public void setParent(AdminFunction parent) {
		this.parent = parent;
	}

	@Override
	public List<AdminFunction> getChildren() {
		return children;
	}

	@Override
	public void setChildren(List<AdminFunction> functions) {
		this.children = functions;
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
	public AdminFunction copyWithoutHierarchy() {
		AdminFunctionImpl dto = new AdminFunctionImpl();
		dto.setId(id);
		dto.setName(name);
		dto.setFunctionKey(functionKey);
		dto.setIcon(icon);
		dto.setUrl(url);
        dto.setDisplayOrder(displayOrder);
        dto.setCeilingEntity(ceilingEntity);
        return dto;
    }
    
}
