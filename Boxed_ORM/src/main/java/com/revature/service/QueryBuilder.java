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
    //Acceptable operators
    Set<String> conditionalOperators = new HashSet<>();
    QueryString qString;

    public QueryBuilder() {
      qString = new QueryString();
    }

    public QueryBuilder craftQuery(){
        qString = new QueryString();
        return this;
    }

    //SELECT --------------------------------------------------------
    public QueryBuilder returnFields(String ... ColumnNames){
        if (ColumnNames == null || ColumnNames.length == 0){
            throw new IllegalArgumentException("Valid Column name required");
        }
        int numColumns = ColumnNames.length;
        qString.select = selectBase;
        String append = " ";
        for (int i = 0; i < ColumnNames.length; i++) {

            if (i < numColumns - 1 )
                append = ", ";

            qString.select += ColumnNames[i] + append;
            append = " ";
        }
        return this;
    }

    public QueryBuilder returnAllFields(){
        qString.select = selectBase + "* ";
        return this;
    }

    //FROM ----------------------------------------------------------
    public QueryBuilder ofClassType(String entityName){
        qString.from = fromBase + entityName + " ";
        return this;
    }

    //WHERE ---------------------------------------------------------
    public QueryBuilder addCondition_Operator(String thisField, String conditionOperator, String thatField){
        qString.where = whereBase + thisField + " " + conditionOperator + " "
                + thatField + " ";
        return this;
    }

    public QueryBuilder addCondition_Between(String thisField, String lowerBound, String upperBound){
        qString.where = whereBase + thisField + " BETWEEN " + lowerBound
                + " AND " + upperBound + " ";
        return this;
    }


    //FINALIZE ------------------------------------------------------
    public String getQuery() {
      return qString.select + qString.from + qString.where;
    }

}
