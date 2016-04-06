package com.zyrone.framework.core.web;

import org.springframework.web.context.request.WebRequest;

import com.zyrone.framework.core.web.ZyroneRequestContext;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    
    private static String OK_TO_USE_SESSION = "zrnOkToUseSession";

    public static boolean isOKtoUseSession(WebRequest request) {
        Boolean okToUseSession = (Boolean) request.getAttribute(OK_TO_USE_SESSION, WebRequest.SCOPE_REQUEST);
        if (okToUseSession == null) {
            return true; 	 // 默认可以使用session
        } else {
            return okToUseSession;
        }
    }

    public static Object getSessionAttributeIfOk(WebRequest request, String attribute) {
        if (isOKtoUseSession(request)) {
            return request.getAttribute(attribute, WebRequest.SCOPE_GLOBAL_SESSION);
        }
        return null;
    }

    public static boolean setSessionAttributeIfOk(WebRequest request, String attribute, Object value) {
        if (isOKtoUseSession(request)) {
            request.setAttribute(attribute, value, WebRequest.SCOPE_GLOBAL_SESSION);
            return true;
        }
        return false;
    }

    public static void setOKtoUseSession(WebRequest request, Boolean value) {
        request.setAttribute(OK_TO_USE_SESSION, value, WebRequest.SCOPE_REQUEST);
    }

    public static String getRequestedServerPrefix() {
        HttpServletRequest request = ZyroneRequestContext.getRequestContext().getRequest();
        String scheme = request.getScheme();
        StringBuilder serverPrefix = new StringBuilder(scheme);
        serverPrefix.append("://");
        serverPrefix.append(request.getServerName());
        if ((scheme.equalsIgnoreCase("http") && request.getServerPort() != 80) || (scheme.equalsIgnoreCase("https") && request.getServerPort() != 443)) {
            serverPrefix.append(":");
            serverPrefix.append(request.getServerPort());
        }
        return serverPrefix.toString();
    }
}
