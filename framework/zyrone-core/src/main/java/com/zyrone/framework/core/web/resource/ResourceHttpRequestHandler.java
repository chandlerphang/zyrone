package com.zyrone.framework.core.web.resource;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;

public class ResourceHttpRequestHandler extends org.springframework.web.servlet.resource.ResourceHttpRequestHandler {
    
    @Resource(name = "blBroadleafContextUtil")
    protected ZyroneContextUtil blcContextUtil;

    @PostConstruct
    protected void sortCollections() {
        OrderedComparator oc = new OrderedComparator();

        if (getLocations() != null) {
            Collections.sort(getLocations(), oc);
        }

        if (getResourceResolvers() != null) {
            Collections.sort(getResourceResolvers(), oc);
        }

        if (getResourceTransformers() != null) {
            Collections.sort(getResourceTransformers(), oc);
        }
        
    }
    
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            blcContextUtil.establishThinRequestContext();
            super.handleRequest(request, response);
        } finally {
            blcContextUtil.clearThinRequestContext();
        }
    }

    protected class OrderedComparator implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Ordered && o2 instanceof Ordered) {
                return ((Ordered) o1).getOrder() - ((Ordered) o2).getOrder();
            }

            if (o1 instanceof Ordered) {
                if (((Ordered) o1).getOrder() == Integer.MAX_VALUE) {
                    // Put MAX_VALUE items at the end of the list (even behind nulls)
                    return 1;
                } else {
                    return -1;
                }
            }

            if (o2 instanceof Ordered) {
                if (((Ordered) o2).getOrder() == Integer.MAX_VALUE) {
                    // Put MAX_VALUE items at the end of the list (even behind nulls)
                    return -1;
                } else {
                    return 1;
                }
            }

            // Neither is ordered (respect the natural order)
            return 0;
        }

    }
}
