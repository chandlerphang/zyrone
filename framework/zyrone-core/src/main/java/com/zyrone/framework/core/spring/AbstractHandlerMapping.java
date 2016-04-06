package com.zyrone.framework.core.spring;

public abstract class AbstractHandlerMapping extends org.springframework.web.servlet.handler.AbstractHandlerMapping {

	protected String controllerName;

    @Override
    public Object getDefaultHandler() {
        return null;        
    }
    
    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }
    
}
