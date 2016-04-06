package com.zyrone.framework.core.spring;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

/**
 * web容器启动监听器, 用于在web容器启动的时候加载zyrone framework支持的多模块项目的spring配置文件.
 * 
 * @see {@link XmlWebApplicationContext}
 * 
 * @author Jason Phang
 */
public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {

    public static final String SHUTDOWN_HOOK_BEAN = "shutdownHookBean";
    public static final String SHUTDOWN_HOOK_METHOD = "shutdownHookMethod";

    @Override
    protected WebApplicationContext createWebApplicationContext(ServletContext servletContext) throws BeansException {
        XmlWebApplicationContext xwac = new XmlWebApplicationContext();
        xwac.setServletContext(servletContext);
        xwac.setConfigLocation(servletContext.getInitParameter(ContextLoaderListener.CONFIG_LOCATION_PARAM));
        xwac.setShutdownBean(servletContext.getInitParameter(SHUTDOWN_HOOK_BEAN));
        xwac.setShutdownMethod(servletContext.getInitParameter(SHUTDOWN_HOOK_METHOD));
 
        return xwac;
    }

}
