package com.zyrone.framework.core.web;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.zyrone.framework.core.util.ThreadLocalManager;

public class ZyroneRequestContext {
    
    protected static final Logger LOG = LoggerFactory.getLogger(ZyroneRequestContext.class);
    private static final ThreadLocal<ZyroneRequestContext> ZYRONE_REQUEST_CONTEXT = ThreadLocalManager.createThreadLocal(ZyroneRequestContext.class);
    
    public static ZyroneRequestContext getRequestContext() {
        return ZYRONE_REQUEST_CONTEXT.get();
    }
    
    public static void setRequestContext(ZyroneRequestContext requestContext) {
        ZYRONE_REQUEST_CONTEXT.set(requestContext);
    }

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected WebRequest webRequest;
    protected Boolean adminContext = false;
    protected Long adminUserId;
    protected MessageSource messageSource;
    protected Locale locale;

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
        this.webRequest = new ServletWebRequest(request);
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setWebRequest(WebRequest webRequest) {
        this.webRequest = webRequest;
        if (webRequest instanceof ServletWebRequest) {
            this.request = ((ServletWebRequest) webRequest).getRequest();
            setResponse(((ServletWebRequest) webRequest).getResponse());
        }
    }

    public WebRequest getWebRequest() {
        return webRequest;
    }

    public String getRequestURIWithoutContext() {
        String requestURIWithoutContext = null;

        if (request != null && request.getRequestURI() != null) {
            if (request.getContextPath() != null) {
                requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
            } else {
                requestURIWithoutContext = request.getRequestURI();
            }

            // 去除JSESSION-ID
            int pos = requestURIWithoutContext.indexOf(";");
            if (pos >= 0) {
                requestURIWithoutContext = requestURIWithoutContext.substring(0,pos);
            }
        }
        
        return requestURIWithoutContext;
    }
    
    public static Map<String, String[]> getRequestParameterMap() {
        return getRequestContext().getRequest().getParameterMap();
    }

    public Boolean isAdminContext() {
        return adminContext == null ? false : adminContext;
    }

    public void setAdminContext(Boolean admin) {
        adminContext = admin;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }
   
}
