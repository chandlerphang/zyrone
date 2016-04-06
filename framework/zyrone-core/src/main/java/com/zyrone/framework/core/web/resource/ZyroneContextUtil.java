package com.zyrone.framework.core.web.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.zyrone.framework.core.util.ThreadLocalManager;
import com.zyrone.framework.core.web.ZyroneRequestContext;

@Service("zrnContextUtil")
public class ZyroneContextUtil {
    
    protected boolean versioningEnabled = false;

    public void establishThinRequestContext() {
        establishThinRequestContextInternal(true, true);
    }

    public void establishThinRequestContextWithoutSandBox() {
        establishThinRequestContextInternal(true, false);
    }

    public void establishThinRequestContextWithoutThemeOrSandbox() {
        establishThinRequestContextInternal(false, false);
    }

    protected void establishThinRequestContextInternal(boolean includeTheme, boolean includeSandBox) {
    	ZyroneRequestContext brc = ZyroneRequestContext.getRequestContext();
        if (brc.getRequest() == null) {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = req.getSession(false);
            SecurityContext ctx = readSecurityContextFromSession(session);
            if (ctx != null) {
                SecurityContextHolder.setContext(ctx);
            }
            brc.setRequest(req);
        }
    }

    public void clearThinRequestContext() {
        ThreadLocalManager.remove();
    }

    protected String getContextName(HttpServletRequest request) {
        String contextName = request.getServerName();
        int pos = contextName.indexOf('.');
        if (pos >= 0) {
            contextName = contextName.substring(0, contextName.indexOf('.'));
        }
        return contextName;
    }

    // **NOTE** This method is lifted from HttpSessionSecurityContextRepository
    protected SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }

        Object ctxFromSession = httpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (ctxFromSession == null) {
            return null;
        }

        if (!(ctxFromSession instanceof SecurityContext)) {
            return null;
        }

        return (SecurityContext) ctxFromSession;
    }
    
    

}
