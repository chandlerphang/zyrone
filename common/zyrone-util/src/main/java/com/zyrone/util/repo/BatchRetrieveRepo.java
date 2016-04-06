package com.zyrone.util.repo;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jason Phang
 */
public class BatchRetrieveRepo {
    
    private int inClauseBatchSize = 300;
    
    @SuppressWarnings("unchecked")
    public <T> List<T> batchExecuteReadQuery(Query query, List<?> params, String parameterName) {
        List<T> response = new ArrayList<T>();
        int start = 0;
        while (start < params.size()) {
            List<?> batchParams = params.subList(start, params.size() < inClauseBatchSize ? params.size() : inClauseBatchSize);
            query.setParameter(parameterName, batchParams);
            response.addAll(query.getResultList());
            start += inClauseBatchSize;
        }
        return response;
    }

    public int getInClauseBatchSize() {
        return inClauseBatchSize;
    }

    public void setInClauseBatchSize(int inClauseBatchSize) {
        this.inClauseBatchSize = inClauseBatchSize;
    }
    
}
