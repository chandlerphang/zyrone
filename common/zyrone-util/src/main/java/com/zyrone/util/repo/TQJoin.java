package com.zyrone.util.repo;

public class TQJoin {

    protected String expression;
    protected String alias;

    public TQJoin(String expression, String alias) {
        this.expression = expression;
        this.alias = alias;
    }

    public String toQl() {
        StringBuilder sb = new StringBuilder();
        sb.append(expression);
        sb.append(" ");
        sb.append(alias);

        return sb.toString();
    }
}
