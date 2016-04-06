package com.zyrone.util.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.CollectionUtils;

/**
 *
 * @author Jason Phang
 */
public class TypedQueryBuilder<T> {
    
    protected Class<T> rootClass;
    protected String rootAlias;
    protected List<TQRestriction> restrictions = new ArrayList<TQRestriction>();
    protected List<TQJoin> joins = new ArrayList<TQJoin>();
    protected List<TQOrder> orders = new ArrayList<TQOrder>();
    protected Map<String, Object> paramMap = new HashMap<String, Object>();

    public TypedQueryBuilder(Class<T> rootClass, String rootAlias) {
        this.rootClass = rootClass;
        this.rootAlias = rootAlias;
    }

    public TypedQueryBuilder<T> addRestriction(String expression, String operation, Object parameter) {
        restrictions.add(new TQRestriction(expression, operation, parameter));
        return this;
    }

    public TypedQueryBuilder<T> addRestriction(TQRestriction restriction) {
        restrictions.add(restriction);
        return this;
    }

    public TypedQueryBuilder<T> addJoin(TQJoin join) {
        joins.add(join);
        return this;
    }

    public TypedQueryBuilder<T> addOrder(TQOrder order) {
        orders.add(order);
        return this;
    }

    public String toQueryString() {
        return toQueryString(false);
    }

    public String toQueryString(boolean count) {
        StringBuilder sb = getSelectClause(new StringBuilder(), count)
                .append(" FROM ").append(rootClass.getName()).append(" ").append(rootAlias);
        if (CollectionUtils.isNotEmpty(joins)) {
            for (TQJoin join : joins) {
                sb.append(" JOIN");
                sb.append(" ");
                sb.append(join.toQl());
            }
        }
        if (CollectionUtils.isNotEmpty(restrictions)) {
            sb.append(" WHERE ");
            for (int i = 0; i < restrictions.size(); i++) {
                TQRestriction r = restrictions.get(i);
                sb.append(r.toQl("p" + i, paramMap));
                if (i != restrictions.size() - 1) {
                    sb.append(" AND ");
                }
            }
        }
        if (CollectionUtils.isNotEmpty(orders)) {
            sb.append(" ORDER BY");
            for (int j=0;j<orders.size();j++){
                sb.append(" ");
                sb.append(orders.get(j).toQl());
                if (j < orders.size() - 1) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    protected StringBuilder getSelectClause(StringBuilder sb, boolean count) {
        sb.append("SELECT ");
        if (count) {
            return sb.append("COUNT(*)");
        } else {
            return sb.append(rootAlias);
        }
    }

    public TypedQuery<T> toQuery(EntityManager em) {
        TypedQuery<T> q = em.createQuery(toQueryString(), rootClass);
        fillParameterMap(q);
        return q;
    }
    
    public TypedQuery<Long> toCountQuery(EntityManager em) {
        TypedQuery<Long> q = em.createQuery(toQueryString(true), Long.class);
        fillParameterMap(q);
        return q;
    }
    
    protected void fillParameterMap(TypedQuery<?> q) {
        for (Entry<String, Object> entry : paramMap.entrySet()) {
            if (entry.getValue() != null) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }
    
}

