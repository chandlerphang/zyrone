package com.zyrone.framework.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zyrone.framework.core.config.RuntimeEnvConfigManager;

/**
 * 
 * @author Jason Phang
 */
@Component("zrnBaseUrlResolver")
public class BaseUrlResolverImpl implements BaseUrlResolver {

    @Autowired
    protected RuntimeEnvConfigManager rtecMgr;
    
    @Override
    public String getSiteBaseUrl() {
    	return getBaseUrl("site");
    }

    @Override
    public String getAdminBaseUrl() {
    	return getBaseUrl("admin");
    }

	@Override
	public String getBaseUrl(String appname) {
		String baseUrl = rtecMgr.getProperty(appname + ".baseurl");
        if (baseUrl.charAt(baseUrl.length() - 1) == '/') {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
	}
    
}
