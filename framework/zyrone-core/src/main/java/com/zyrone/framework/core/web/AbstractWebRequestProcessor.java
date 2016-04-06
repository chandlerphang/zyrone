package com.zyrone.framework.core.web;

import org.springframework.web.context.request.WebRequest;

public abstract class AbstractWebRequestProcessor implements WebRequestProcessor {
    
    public void postProcess(WebRequest request) {
        // nada
    }

}
