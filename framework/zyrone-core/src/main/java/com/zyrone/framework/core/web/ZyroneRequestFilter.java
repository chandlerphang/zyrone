package com.zyrone.framework.core.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

@Component("zrnRequestFilter")
public class ZyroneRequestFilter extends OncePerRequestFilter {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public static String REQUEST_DTO_PARAM_NAME = "zrnRequestDTO";
    public static final String ADMIN_USER_ID_PARAM_NAME = "zrnAdminUserId";
    private static final String ZRN_ADMIN_PREFIX = "zrnadmin";
    private static final String ZRN_ADMIN_SERVICE = ".service";

    private Set<String> ignoreSuffixes;

    @Resource(name = "zrnRequestProcessor")
    protected WebRequestProcessor requestProcessor;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!shouldProcessURL(request, request.getRequestURI())) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Process URL not processing URL " + request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }
        
        if (LOG.isTraceEnabled()) {
            String requestURIWithoutContext;
            if (request.getContextPath() != null) {
                requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
            } else {
                requestURIWithoutContext = request.getRequestURI();
            }

            // 移除 JSESSION-ID
            int pos = requestURIWithoutContext.indexOf(";");
            if (pos >= 0) {
                requestURIWithoutContext = requestURIWithoutContext.substring(0, pos);
            }

            LOG.trace("Process URL Filter Begin " + requestURIWithoutContext);
        }

        try {
            requestProcessor.process(new ServletWebRequest(request, response));
            filterChain.doFilter(request, response);
        } finally {
            requestProcessor.postProcess(new ServletWebRequest(request, response));
        }
    }

    protected boolean shouldProcessURL(HttpServletRequest request, String requestURI) {
        if (requestURI.endsWith(ZRN_ADMIN_SERVICE) || requestURI.contains(ZRN_ADMIN_PREFIX)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("ZyroneRequestFilter ignoring admin request URI " + requestURI);
            }
            return false;
        } else {
            int pos = requestURI.lastIndexOf(".");
            if (pos > 0) {
                String suffix = requestURI.substring(pos);
                if (getIgnoreSuffixes().contains(suffix.toLowerCase())) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("ZyroneRequestFilter ignoring request due to suffix " + requestURI);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
	protected Set getIgnoreSuffixes() {
        if (ignoreSuffixes == null || ignoreSuffixes.isEmpty()) {
            String[] ignoreSuffixList = { ".aif", ".aiff", ".asf", ".avi", ".bin", ".bmp", ".css", ".doc", ".eps", ".gif", ".hqx", ".js", ".jpg", ".jpeg", ".mid", ".midi", ".mov", ".mp3", ".mpg", ".mpeg", ".p65", ".pdf", ".pic", ".pict", ".png", ".ppt", ".psd", ".qxd", ".ram", ".ra", ".rm", ".sea", ".sit", ".stk", ".swf", ".tif", ".tiff", ".txt", ".rtf", ".vob", ".wav", ".wmf", ".xls", ".zip" };
            ignoreSuffixes = new HashSet<String>(Arrays.asList(ignoreSuffixList));
        }
        return ignoreSuffixes;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}
