package com.zyrone.framework.core.web;

/**
 * 解析全站的根地址
 * 
 * <p>
 * 返回的根地址会做规整，如果是以 "/" 结尾的话，会去掉该字符。
 * </p>
 * 
 * @author Jason Phang
 */
public interface BaseUrlResolver {

    String getSiteBaseUrl();

    String getAdminBaseUrl();
    
    String getBaseUrl(String appname);

}
