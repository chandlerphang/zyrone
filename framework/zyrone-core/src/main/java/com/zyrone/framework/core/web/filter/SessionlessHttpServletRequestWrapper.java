package com.zyrone.framework.core.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class SessionlessHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public SessionlessHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (!create) {
            return null;
        }
        throw new UnsupportedOperationException("cannot get/create a HttpSession in a sessionless environment.");
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("cannot get/create a HttpSession in a sessionless environment.");
    }

}
