package com.zyrone.framework.core.security;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.zyrone.framework.core.service.ServiceException;

public class CsrfFilter extends GenericFilterBean {
	
    protected static final Logger LOG = LoggerFactory.getLogger(CsrfFilter.class);
    
    @Resource(name="zrnExploitProtectionService")
    protected ExploitProtectionService exploitProtectionService;

    protected List<String> excludedRequestPatterns;

    @Override
    public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) baseRequest;
        HttpServletResponse response = (HttpServletResponse) baseResponse;

        boolean excludedRequestFound = false;
        if (excludedRequestPatterns != null && excludedRequestPatterns.size() > 0) {
            for (String pattern : excludedRequestPatterns) {
                RequestMatcher matcher = new AntPathRequestMatcher(pattern);
                if (matcher.matches(request)){
                    excludedRequestFound = true;
                    break;
                }
            }
        }

        // 只在POST请求上验证CSRF Token
        if (request.getMethod().equals("POST") && !excludedRequestFound) {
            String requestToken = request.getParameter(exploitProtectionService.getCsrfTokenParameter());
            try {
                exploitProtectionService.compareToken(requestToken);
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
        }
        
        chain.doFilter(request, response);
    }

    public List<String> getExcludedRequestPatterns() {
        return excludedRequestPatterns;
    }

    public void setExcludedRequestPatterns(List<String> excludedRequestPatterns) {
        this.excludedRequestPatterns = excludedRequestPatterns;
    }
    
}
