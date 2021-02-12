package com.revature.service;

import com.revature.model.QueryString;
import org.postgresql.core.Query;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class QueryBuilder {
    //Attributes ----------------------------------------------------
    //string bases for queries
    String selectBase = "SELECT ";
    String fromBase = "FROM ";
    String whereBase = "WHERE ";
    QueryString qString;
    StringBuilder query, prevQuery;
    //TODO: speed dial queries via map?
    //TODO: trim whitespace in strings

    //Constructors --------------------------------------------------
    public QueryBuilder() {
        qString = new QueryString();
    }


    public QueryBuilder craftQuery(){
        qString = new QueryString();
        prevQuery = query;
        query = new StringBuilder("");
        return this;
    }

    //SELECT --------------------------------------------------------
    public QueryBuilder returnFields(String ... ColumnNames){
        if (ColumnNames == null || ColumnNames.length == 0){
            throw new IllegalArgumentException("Valid Column name required");
        }
        int numColumns = ColumnNames.length;
        qString.select.append(selectBase);
        String append;
        for (int i = 0; i < ColumnNames.length; i++) {

            if (i < numColumns - 1 )
                append = ", ";
            else
                append = " ";

            qString.select.append(ColumnNames[i]).append(append);
        }
        return this;
    }

    public QueryBuilder returnAllFields(){
        qString.select.append(selectBase).append("* ");
        return this;
    }

    //FROM ----------------------------------------------------------
    public QueryBuilder ofClassType(String entityName){
        qString.from.append(fromBase).append(entityName).append(" ");
        return this;
    }

    //WHERE ---------------------------------------------------------
    public QueryBuilder addCondition_Operator(String thisField, String conditionOperator, String thatField){
        qString.where.append(whereBase)
                     .append(thisField).append(" ")
                     .append(conditionOperator).append(" ")
                     .append(thatField).append(" ");
        return this;
    }

    public QueryBuilder addCondition_Between(String thisField, String lowerBound, String upperBound){
        qString.where.append(whereBase)
                .append(thisField).append(" ")
                .append("BETWEEN ")
                .append(lowerBound).append(" ")
                .append("AND ")
                .append(upperBound).append(" ");
        return this;
    }

    public QueryBuilder addCondition_Like(String thisField, String comparison){
        qString.where.append(whereBase)
                .append(thisField).append(" ")
                .append("LIKE ")
                .append(comparison).append(" ");
        return this;
    }

    public QueryBuilder addCondition_In(String thisField, String[] listVals){
        qString.where.append(whereBase)
                .append(thisField).append(" ")
                .append("IN (");
        String append;
        for (int i = 0; i < listVals.length; i++) {
            qString.where.append(listVals[i]);
            if (i < listVals.length - 1 )
                append = ", ";
            else
                append = " ";
            qString.select.append(append);
        }
        qString.where.append(") ");
        return this;
    }

    //FINALIZE ------------------------------------------------------
    public String getQuery() {
        query.append(qString.select).append(qString.from).append(qString.where);
      return query.toString();
    }

}
