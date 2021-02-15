package com.revature.model.queries;

import com.revature.service.QueryBuilder;

public class QueryString {
    public StringBuilder select;
    public StringBuilder from;
    public StringBuilder where;
    public StringBuilder join;

    //TODO: add binary code to encode select query type?

    public QueryString() {
        select = new StringBuilder("");
        from = new StringBuilder("");
        where = new StringBuilder("");
        join = new StringBuilder("");
    }
}
