package com.zyrone.framework.admin.security.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Jason Phang
 */
public interface ForgotPasswordSecurityToken extends Serializable {

    public String getToken();

    public void setToken(String token);

    public Date getCreateDate();

    public void setCreateDate(Date date);

    public Date getTokenUsedDate();

    public void setTokenUsedDate(Date date);

    public Long getAdminUserId();

    public void setAdminUserId(Long adminUserId);

    public boolean isTokenUsedFlag();

    public void setTokenUsedFlag(boolean tokenUsed);
}
