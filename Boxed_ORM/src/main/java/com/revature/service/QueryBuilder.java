package com.revature.service;

import com.revature.model.queries.JoinTypes;
import com.revature.model.queries.QueryString;

/**
 *
 */
public class QueryBuilder {
    //Attributes ----------------------------------------------------
    //string bases for queries
    String selectBase = "SELECT ";
    String fromBase = "FROM ";
    String whereBase = "WHERE ";

    //Allows recall of previously used or intermediary queries
    QueryString qString;
    StringBuilder query;

    //Save up to 3 queries
    static QueryString[] speedDial = new QueryString[3];

    //Track num entities
    int numTables = 0;

    //TODO: add aliases for select statements
    //TODO: add aggregate functions
    //TODO: add chained conditions to WHERE clause (or, and)

    //Constructors --------------------------------------------------
    public QueryBuilder() {
        qString = new QueryString();
    }


    public QueryBuilder craftQuery(){
        qString = new QueryString();
        query = new StringBuilder("");
        numTables = 0;
        return this;
    }

    public void saveQuery(int i){
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("SpeedDialQuery: Value must be between 0 and 2 inclusive");
        speedDial[i] = qString;
    }

    public void loadQuery(int i){
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("SpeedDialQuery: Value must be between 0 and 2 inclusive");
        qString = speedDial[i];
    }

    //SELECT --------------------------------------------------------
    /**
     * Allows user to specify which columns to get a return value from
     * format SELECT col1, col2, ...
     * @param ColumnNames list of columns to select
     * @return this QueryBuilder object
     */
    public QueryBuilder returnFields(String ... ColumnNames){
        //Validation
        if (ColumnNames == null || ColumnNames.length == 0){
            throw new IllegalArgumentException("Valid Column name required");
        }
        stringValidation(ColumnNames);

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

    /**
     * Allows the user to add a basic SELECT statement that returns all columns in query
     * format: SELECT *
     * @return this QueryBuilder object
     */
    public QueryBuilder returnAllFields(){
        qString.select.append(selectBase).append("* ");
        return this;
    }

    //FROM ----------------------------------------------------------
    /**
     * Allows the user to add the table being queried. This is one of two operations considered
     * mandatory to create a valid query
     * format: FROM table T1
     * @param entityName name of the table being queried
     * @return this QueryBuilder object
     */
    public QueryBuilder ofClassType(String entityName){
       //Validation
        stringValidation(entityName);

        qString.from.append(fromBase).append(entityName).append(" T1 ");
        numTables++;
        return this;
    }

    /**
     * Allows the user to add an additional table to the query via JOIN operation of which there are
     * five types a accepted via the JoinTypes enum{INNER, OUTER, FULL, LEFT, RIGHT}.
     * format: JOIN table T2
     * @param entityName The name of the table being added to the Query
     * @param type the JOIN type
     * @return this QueryBuilder object
     */
    public QueryBuilder joinWith(String entityName, JoinTypes type){
        //validation
        stringValidation(entityName);

        if (qString.from.toString().equals("")){
            throw new IllegalArgumentException("Main entity being searched must be set first!");
        }
        numTables++;
        qString.join.append(type.name()).append(" JOIN ").append(entityName)
                .append(" T").append(numTables).append(" ");

        return this;
    }

    /**
     * Allows the user to add an additional table to the query via a JOIN operation of which there are
     * Five types accepted via the JoinTypes enum {INNER, OUTER, FULL, LEFT, RIGHT}. It adds an ON condition
     * to allow the user to decide which columns will be used for the JOIN
     * format: JOIN table T2 ON this = that
     * @param entityName The name of the table being added to the Query
     * @param type the JOIN type
     * @param mainClassField the column from the entity added in the FROM statement that will be used for the comparison
     * @param joinClassField the column from this entity that will be used for the comparison
     * @return this QueryBuilder object
     */
    public QueryBuilder joinOn(String entityName, JoinTypes type, String mainClassField, String joinClassField){
        //Validation
        stringValidation(entityName, mainClassField, joinClassField);

        if (qString.from.toString().equals("")){
            throw new IllegalArgumentException("Main entity being searched must be set first!");
        }
        numTables++;
        qString.join.append(type.name()).append(" JOIN ").append(entityName)
                .append(" T").append(numTables).append(" ");
        qString.join.append("ON ").append("T1.").append(mainClassField).append(" ")
                .append("= ").append("T2.").append(joinClassField).append(" ");

        return this;
    }

    //WHERE ---------------------------------------------------------
    /**
     * Allows user to create a type of WHERE caluse that uses a condition operator like
     * <, >, <=, >=, =, <>, != to compare two columns
     * format: WHERE this = that
     * @param thisField the name of the Column on the left side of the condition operator
     * @param conditionOperator the condition operator being used
     * @param thatField the name of the column on the right side of the condition operator
     * @param isString tells whether the thatField needs ' ' surrounding it because it represents a string literal
     * @return this QueryBuilder object
     */
    public QueryBuilder addCondition_Operator(String thisField, String conditionOperator,
                                              String thatField, boolean isString){
        //Validation
        stringValidation(thisField, thatField);
        conditionOperatorValidation(conditionOperator);

        qString.where.append(whereBase)
                     .append(thisField).append(" ")
                     .append(conditionOperator).append(" ");
        if (isString)
            qString.where.append("'");
        qString.where.append(thatField);
        if (isString)
            qString.where.append("'");
        qString.where.append(" ");

        return this;
    }

    /**
     * Allows user to create a type of WHERE clause that uses LIKE for its conditional statement
     * format: WHERE Column BETWEEN 10 AND 100
     * @param thisField name of the column having condition applied to it
     * @param lowerBound the lower end of accepted range
     * @param upperBound the upper end of accepted range
     * @return this QueryBuilder object
     */
    public QueryBuilder addCondition_Between(String thisField, String lowerBound, String upperBound){
        //Validation
        stringValidation(thisField);

        qString.where.append(whereBase)
                .append(thisField).append(" ")
                .append("BETWEEN ")
                .append(lowerBound).append(" ")
                .append("AND ")
                .append(upperBound).append(" ");
        return this;
    }

    /**
     * Allows user to create a type of WHERE clause that uses LIKE for its conditional statement
     * format: WHERE Column LIKE s%
     * @param thisField name of the column having the condition applied to it
     * @param comparison a string being matched against thisField via like operator
     * @return this QueryBuilder
     */
    public QueryBuilder addCondition_Like(String thisField, String comparison){
        //Validation
        stringValidation(thisField);

        qString.where.append(whereBase)
                .append(thisField).append(" ")
                .append("LIKE ")
                .append(comparison).append(" ");
        return this;
    }

    /**
     * Allows user to create a type of WHERE clause that uses IN for its conditional statement
     * format: WHERE Column IN ('value', 'value' ... )
     * @param thisField the name of the column having the condition applied to it
     * @param listValues a list from which a match to the Column value will be searched for
     * @return this QueryBuilder Object
     */
    public QueryBuilder addCondition_In(String thisField, String[] listValues){

        //Validation
        if (listValues == null || listValues.length == 0)
            throw new IllegalArgumentException("IN condition requires a list or sub-query to search, empty lists are not accepted");
        stringValidation(thisField);

        //Start string
        qString.where.append(whereBase)
                .append(thisField).append(" ")
                .append("IN (");
        //get values from list
        String appendEnding;
        for (int i = 0; i < listValues.length; i++) {
            qString.where.append("'").append(listValues[i]).append("'");
            if (i < listValues.length - 1 )
                appendEnding = ", ";
            else
                appendEnding = " ";
            qString.where.append(appendEnding);
        }
        //finish string
        qString.where.append(") ");
        return this;
    }

    /**
     * Allows user to create a type of WHERE clause that uses IN for its conditional statement
     * format: WHERE Column IN ( sub-query)
     * @param thisField the column having the conditional statement applied to it
     * @param subQuery a string holding a complete query, from which the values supplied will be used in condition
     * @return this QueryBuilder object
     */
    public QueryBuilder addCondition_In(String thisField, String subQuery){
        //Validation
        stringValidation(thisField, subQuery);

        qString.where.append(whereBase)
                .append(thisField).append(" ")
                .append("IN ");

        qString.where.append("(").append(subQuery).append(") ");
        return this;
    }

    //FINALIZE ------------------------------------------------------
    /**
     * Builds the final query from the string building blocks stored in the QueryString object
     * @return string holding constructed query
     */
    public String getQuery() {
        if(!isValid())
            return "";
        query.append(qString.select)
                .append(qString.from)
                .append(qString.join)
                .append(qString.where);
      return query.toString();
    }

    //VALIDATE ------------------------------------------------------
    /**
     * Ensures a query cannot be returned without a built Select and From statement
     * @return a boolean stating whether statement has a select and from statement
     */
    public boolean isValid(){
        boolean valid = true;
        if (qString.select.toString().equals("") || qString.from.toString().equals(""))
            valid = false;
        return valid;
    }

    /**
     * reject all empty strings and any string that is made from special chars or starts with a number
     * @param args  string values to be tested
     */
    static void stringValidation(String... args){
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("[a-zA-Z_][a-zA-Z0-9_]*")){
                throw new IllegalArgumentException("Only alphanumeric values and _ accepted as class or field names");
            }
        }
    }

    /**
     * reject all empty strings and any string that is an invalid condition operator
     * @param args  string values to be tested
     */
    static void conditionOperatorValidation(String ... args){
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("(=|<|>|<=|>=|!=|<>)")){
                throw new IllegalArgumentException("Only <, >, =, <=, >=, !=, <> are accepted condition operators");
            }
        }
    }

}
