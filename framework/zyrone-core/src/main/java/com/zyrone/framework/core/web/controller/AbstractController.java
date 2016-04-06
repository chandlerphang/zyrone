package com.zyrone.framework.core.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

/**
 *
 * @author Jason Phang
 */
public abstract class AbstractController {

    protected boolean isAjaxRequest(HttpServletRequest request) {
        return ControllerUtility.isAjaxRequest(request);       
    }

    protected String getContextPath(HttpServletRequest request) {
       return ControllerUtility.getContextPath(request);
    }
    
    protected <T> void addDeepLink(ModelAndView model, DeepLinkService<T> service, T item) {
        if (service == null) {
            return;
        }
        
        List<DeepLink> links = service.getLinks(item);
        if (links.size() == 1) {
            model.addObject("adminDeepLink", links.get(0));
        } else {
            model.addObject("adminDeepLink", links);
        }
    }
    
    protected String jsonResponse(HttpServletResponse response, Map<?, ?> responseMap) throws IOException {
        response.setHeader("Content-Type", "application/json");
        JSON.writeJSONStringTo(responseMap, response.getWriter());
        return null;
    }

}
