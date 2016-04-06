package com.zyrone.framework.core.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;

import com.zyrone.framework.core.web.RequestUtils;

/**
 * 
 * @author Jason Phang
 */
public class RedirectController {

	public String redirect(HttpServletRequest request, HttpServletResponse response, Model model) {
        String path = null;
        if (RequestUtils.isOKtoUseSession(new ServletWebRequest(request))) {
            path = (String) request.getSession().getAttribute("ZRN_REDIRECT_URL");
        }

        if (path == null) {
            path = request.getContextPath();
        }
        return "ajaxredirect:" + path;
    }
	
}
