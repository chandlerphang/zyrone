package com.zyrone.framework.core.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerUtility {
    protected static final Logger LOG = LoggerFactory.getLogger(ControllerUtility.class);
    
    public static final String ZRN_REDIRECT_ATTRIBUTE = "zrn_redirect";
    public static final String ZRN_AJAX_PARAMETER = "zrnAjax";

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxParameter = request.getParameter(ZRN_AJAX_PARAMETER);
        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean result = (ajaxParameter != null && "true".equals(ajaxParameter))
                || "XMLHttpRequest".equals(requestedWithHeader);
        
        if (LOG.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder()
                .append("Request URL: [").append(request.getServletPath()).append("]")
                .append(" - ")
                .append("ajaxParam: [").append(String.valueOf(ajaxParameter)).append("]")
                .append(" - ")
                .append("X-Requested-With: [").append(requestedWithHeader).append("]")
                .append(" - ")
                .append("Returning: [").append(result).append("]");
            LOG.debug(sb.toString());
        }
        
        return result;
    }
    
    public static String getContextPath(HttpServletRequest request) {
        String ctxPath = request.getContextPath();
        if (StringUtils.isBlank(ctxPath)) {
            return "/";
        } else {
            if (ctxPath.charAt(0) != '/') {
                ctxPath = '/' + ctxPath;
            }
            
            if (ctxPath.charAt(ctxPath.length() - 1) != '/') {
                ctxPath = ctxPath + '/';
            }
            
            return ctxPath;
        }
    }
    
}
