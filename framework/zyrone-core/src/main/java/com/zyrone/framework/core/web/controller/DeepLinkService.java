package com.zyrone.framework.core.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.zyrone.framework.core.config.RuntimeEnvConfigManager;
import com.zyrone.framework.core.web.BaseUrlResolver;

public abstract class DeepLinkService<T> {
    
    @Resource(name = "zrnBaseUrlResolver")
    protected BaseUrlResolver baseUrlResolver;
    
    @Autowired
    protected RuntimeEnvConfigManager rtecMgr;

    public final List<DeepLink> getLinks(T item) {
        return getLinksInternal(item);
    }
    
    protected String getAdminBaseUrl() {
        return baseUrlResolver.getAdminBaseUrl();
    }

    protected abstract List<DeepLink> getLinksInternal(T item);

}
