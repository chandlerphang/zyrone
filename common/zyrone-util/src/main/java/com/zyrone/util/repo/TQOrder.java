package com.zyrone.util.repo;

public class TQOrder {

    protected String expression;
    protected Boolean ascending = true;

    public TQOrder(String expression, Boolean ascending) {
        this.expression = expression;
        this.ascending = ascending;
    }

    public String toQl() {
        StringBuilder sb = new StringBuilder();
        sb.append(expression);
        sb.append(" ");
        sb.append(ascending != null && ascending?"ASC":"DESC");

        return sb.toString();
    }
}
