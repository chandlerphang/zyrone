package com.zyrone.framework.admin.security.service.remote;

import com.zyrone.framework.core.service.ServiceException;

public interface AdminSecurityService {

    public AdminUser getAdminUser() throws ServiceException;
    
}
