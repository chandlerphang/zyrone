package com.zyrone.framework.core.web;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component("zrnRequestProcessor")
public class DefaultRequestProcessor extends AbstractWebRequestProcessor {

    @Resource(name = "messageSource")
    protected MessageSource messageSource;
    
    @Override
    public void process(WebRequest request) {
    	ZyroneRequestContext zrc = new ZyroneRequestContext();
        zrc.setWebRequest(request);
        zrc.setAdminContext(false);

        ZyroneRequestContext.setRequestContext(zrc);

        zrc.setMessageSource(messageSource);

        String adminUserId = request.getParameter(ZyroneRequestFilter.ADMIN_USER_ID_PARAM_NAME);
        if (StringUtils.isNotBlank(adminUserId)) {
            zrc.setAdminUserId(Long.parseLong(adminUserId));
        }

    }
}
