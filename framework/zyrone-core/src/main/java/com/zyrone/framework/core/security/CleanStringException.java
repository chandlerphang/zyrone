package com.zyrone.framework.core.security;

import org.owasp.validator.html.CleanResults;

import com.zyrone.framework.core.service.ServiceException;

public class CleanStringException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public CleanStringException(CleanResults cleanResults) {
        this.cleanResults = cleanResults;
    }

    protected CleanResults cleanResults;

    public CleanResults getCleanResults() {
        return cleanResults;
    }

    public void setCleanResults(CleanResults cleanResults) {
        this.cleanResults = cleanResults;
    }
    
}
