package com.zyrone.framework.core.web;

import org.springframework.web.context.request.WebRequest;

public interface WebRequestProcessor {

    public void process(WebRequest request);

    public void postProcess(WebRequest request);

}
