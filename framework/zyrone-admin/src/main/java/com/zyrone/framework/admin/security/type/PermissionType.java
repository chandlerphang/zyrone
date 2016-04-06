package com.zyrone.framework.admin.security.type;

import com.zyrone.framework.core.EnumType;

/**
 *
 * @author Jason Phang
 */
public enum PermissionType implements EnumType {

    READ("Read"),
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    ALL("All"),
    OTHER("Other");

    private String friendlyName;

    private PermissionType(final String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getName() {
        return name();
    }

    public String getFriendlyName() {
        return friendlyName;
    }

}
