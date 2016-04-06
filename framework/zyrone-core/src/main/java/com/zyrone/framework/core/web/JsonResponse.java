package com.zyrone.framework.core.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public class JsonResponse {
    
    protected Map<String, Object> map = new HashMap<String, Object>();
    protected HttpServletResponse response;
    
    public JsonResponse(HttpServletResponse response) {
        this.response = response;
    }
    
    public JsonResponse with(String key, Object value) {
        map.put(key, value);
        return this;
    }
    
    public String done() {
        response.setHeader("Content-Type", "application/json");
        try {
        	JSON.writeJSONStringTo(map, response.getWriter());
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize JSON", e);
        }
        return null;
    }

}
