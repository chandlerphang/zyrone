package com.zyrone.util.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

/**
 *
 * @author Jason Phang
 */
public class TQRestriction {
    
    protected String expression;
    protected String operation;
    protected Object parameter;
    
    protected Mode joinMode;
    protected List<TQRestriction> restrictions = new ArrayList<TQRestriction>();

    public TQRestriction(String expression, String operation) {
        this.expression = expression;
        this.operation = operation.toLowerCase();
    }

    public TQRestriction(String expression, String operation, Object parameter) {
        this(expression, operation);
        this.parameter = parameter;
    }

    public TQRestriction(Mode joinMode) {
        this.joinMode = joinMode;
    }

    public TQRestriction addChildRestriction(TQRestriction r) {
        restrictions.add(r);
        return this;
    }

    public String toQl(String parameterName, Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder("(");
        if (expression != null && operation != null) {
            sb.append(expression).append(" ").append(operation);

            if (parameter != null) {
                sb.append(' ');
                String pname = ':' + parameterName;
                if (operation.equals("in")) {
                    pname = "(" + pname + ")";
                }
                sb.append(pname);
                paramMap.put(parameterName, parameter);
            }
        }
        
        if (CollectionUtils.isNotEmpty(restrictions)) {
            for (int i = 0; i < restrictions.size(); i++) {
                TQRestriction r = restrictions.get(i);
                String internalParamName = parameterName + "_" + i;
                
                sb.append(r.toQl(internalParamName, paramMap));
                paramMap.put(internalParamName, r.parameter);
                
                if (restrictions.size() - 1 != i) {
                    sb.append(joinMode == Mode.OR ? " OR " : " AND ");
                }
            }
        }
        
        return sb.append(")").toString();
    }
    
    public enum Mode {
        OR, AND
    }
    
}
