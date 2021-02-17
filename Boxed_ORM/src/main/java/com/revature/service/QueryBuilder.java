package com.revature.service;

/**
 *
 */
public class QueryBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    public enum StmtType {
        SELECT, FROM, JOIN, WHERE;

        @Override
        public String toString() { return name() + " ";}
    }

    public enum JoinType {
        INNER, OUTER, FULL, LEFT, RIGHT;

        @Override
        public String toString() {
            return name() + " ";
        }
    }

    //statement stats
    private int numTables = 0;
    private int numConditions;
    private boolean chainWithAnd = true;

    //Save up to 3 queries
    static StringBuilder[] speedDial = new StringBuilder[3];


    //TODO: add aliases for select statements
    //TODO: add aggregate functions
    //TODO: add chained conditions to WHERE clause (or, and)

    //Constructors --------------------------------------------------
    public QueryBuilder() {
        statements = new StringBuilder[StmtType.values().length];
    }

    public QueryBuilder craftNewTransaction(){
        for (int i = 0; i < statements.length; i++) {
            statements[i] = new StringBuilder("");
        }
        numTables = 0;
        numConditions = 0;
        return this;
    }

    public void saveQuery(int i){
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("SpeedDialQuery: Value must be between 0 and 2 inclusive");
        speedDial[i] = transaction;
    }

    public void loadQuery(int i){
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("SpeedDialQuery: Value must be between 0 and 2 inclusive");
        transaction = speedDial[i];
    }

    //SELECT --------------------------------------------------------
    /**
     * Allows the user to add a basic SELECT statement that returns all columns in query
     * format: SELECT *
     * @return this QueryBuilder object
     */
    public QueryBuilder returnFields(){
        statements[StmtType.SELECT.ordinal()]
                .append(StmtType.SELECT.toString()).append("* ");
        return this;
    }

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
        isValidName(ColumnNames);


        int numColumns = ColumnNames.length;
        statements[StmtType.SELECT.ordinal()]
                .append(StmtType.SELECT.toString());

        String append;
        for (int i = 0; i < numColumns; i++) {
            append = i < numColumns - 1 ? ", " : " ";

            statements[StmtType.SELECT.ordinal()]
                    .append(ColumnNames[i])
                    .append(append);
        }
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
        isValidName(entityName);

        statements[StmtType.FROM.ordinal()]
                .append(StmtType.FROM.toString())
                .append(entityName).append(" T1 ");

        numTables++;
        return this;
    }

    /**
     * Allows the user to add an additional table to the query via JOIN operation of which there are
     * five types a accepted via the JoinTypes enum{INNER, OUTER, FULL, LEFT, RIGHT}.
     * format: JOIN table T2
     * @param entityName The name of the table being added to the Query
     * @param joinType the JOIN type
     * @return this QueryBuilder object
     */
    public QueryBuilder joinWith(String entityName, JoinType joinType){
        //validation
        isValidName(entityName);
        if (statements[StmtType.FROM.ordinal()].toString().equals(""))
            throw new IllegalArgumentException("Main entity being searched must be set first!");

        numTables++;
        statements[StmtType.JOIN.ordinal()]
                .append(joinType.toString())
                .append(StmtType.JOIN.toString())
                .append(entityName).append(" ")
                .append("T").append(numTables).append(" ");

        return this;
    }

    /**
     * Allows the user to add an additional table to the query via a JOIN operation of which there are
     * Five types accepted via the JoinTypes enum {INNER, OUTER, FULL, LEFT, RIGHT}. It adds an ON condition
     * to allow the user to decide which columns will be used for the JOIN
     * format: JOIN table T2 ON this = that
     * @param entityName The name of the table being added to the Query
     * @param joinType the JOIN type
     * @param mainClassField the column from the entity added in the FROM statement that will be used for the comparison
     * @param joinClassField the column from this entity that will be used for the comparison
     * @return this QueryBuilder object
     */
    public QueryBuilder joinOn(String entityName, JoinType joinType, String mainClassField, String joinClassField){
        //Validation
        isValidName(entityName, mainClassField, joinClassField);
        if (statements[StmtType.FROM.ordinal()].toString().equals(""))
            throw new IllegalArgumentException("Main entity being searched must be set first!");

        numTables++;
        //Join statement
        statements[StmtType.JOIN.ordinal()]
                .append(joinType.toString())
                .append(StmtType.JOIN.toString())
                .append(entityName).append(" ")
                .append("T").append(numTables).append(" ");
        //On statement
        statements[StmtType.JOIN.ordinal()]
                .append("ON ")
                .append("T1.").append(mainClassField).append(" ")
                .append("= ")
                .append("T2.").append(joinClassField).append(" ");

        return this;
    }

    //WHERE ---------------------------------------------------------
    public QueryBuilder switchToOR(){chainWithAnd = false; return this;}
    public QueryBuilder switchToAND(){chainWithAnd = true; return this;}

    /**
     * Allows user to create a type of WHERE clause that uses a condition operator like
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
        isValidName(thisField, thatField);
        isValidConditionOperator(conditionOperator);

        //Allow for condition chaining
        numConditions++;
        if(numConditions == 1){
            statements[StmtType.WHERE.ordinal()]
                    .append(StmtType.WHERE.toString());
        }else{
            String condition = chainWithAnd? "AND " : "OR ";
            statements[StmtType.WHERE.ordinal()]
                    .append(condition);
        }

        statements[StmtType.WHERE.ordinal()]
                .append(thisField).append(" ")
                .append(conditionOperator).append(" ");

        //Wrap string literals
        if (isString)
            statements[StmtType.WHERE.ordinal()]
                    .append("'");
        statements[StmtType.WHERE.ordinal()]
                .append(thatField);
        if (isString)
            statements[StmtType.WHERE.ordinal()]
                    .append("'");

        statements[StmtType.WHERE.ordinal()]
                .append(" ");

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
        isValidName(thisField);

        //Allow for condition chaining
        numConditions++;
        if(numConditions == 1){
            statements[StmtType.WHERE.ordinal()]
                    .append(StmtType.WHERE.toString());
        }else{
            String condition = chainWithAnd? "AND " : "OR ";
            statements[StmtType.WHERE.ordinal()]
                    .append(condition);
        }

        statements[StmtType.WHERE.ordinal()]
                .append(thisField).append(" ")
                .append("BETWEEN ")
                .append(lowerBound).append(" ")
                .append("AND ")
                .append(upperBound).append(" ");
        return this;
    }

    /**
     * Allows user to create a type of WHERE clause that uses LIKE for its conditional statement
     * format: WHERE Column LIKE 's%'
     * @param thisField name of the column having the condition applied to it
     * @param comparison a string being matched against thisField via like operator
     * @return this QueryBuilder
     */
    public QueryBuilder addCondition_Like(String thisField, String comparison){
        //Validation
        isValidName(thisField);

        //Allow for condition chaining
        numConditions++;
        if(numConditions == 1){
            statements[StmtType.WHERE.ordinal()]
                    .append(StmtType.WHERE.toString());
        }else{
            String condition = chainWithAnd? "AND " : "OR ";
            statements[StmtType.WHERE.ordinal()]
                    .append(condition);
        }

        statements[StmtType.WHERE.ordinal()]
                .append(thisField).append(" ")
                .append("LIKE ")
                .append("'").append(comparison).append("' ");
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
        isValidName(thisField);

        //Allow for condition chaining
        numConditions++;
        if(numConditions == 1){
            statements[StmtType.WHERE.ordinal()]
                    .append(StmtType.WHERE.toString());
        }else{
            String condition = chainWithAnd? "AND " : "OR ";
            statements[StmtType.WHERE.ordinal()]
                    .append(condition);
        }

        //Start string
        statements[StmtType.WHERE.ordinal()]
                .append(thisField).append(" ")
                .append("IN ")
                .append("(");

        //get values from list
        String append;
        for (int i = 0; i < listValues.length; i++) {
            statements[StmtType.WHERE.ordinal()]
                    .append("'").append(listValues[i]).append("'");
            append = i < listValues.length - 1? ", " : "";
            statements[StmtType.WHERE.ordinal()]
                    .append(append);
        }
        //finish string
        statements[StmtType.WHERE.ordinal()]
                .append(") ");
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
        isValidName(thisField, subQuery);

        //Allow for condition chaining
        numConditions++;
        if(numConditions == 1){
            statements[StmtType.WHERE.ordinal()]
                    .append(StmtType.WHERE.toString());
        }else{
            String condition = chainWithAnd? "AND " : "OR ";
            statements[StmtType.WHERE.ordinal()]
                    .append(condition);
        }

        statements[StmtType.WHERE.ordinal()]
                .append(thisField).append(" ")
                .append("IN ")
                .append("(").append(subQuery).append(") ");
        return this;
    }

    //FINALIZE ------------------------------------------------------
    /**
     * Builds the final query from the string building blocks stored in the QueryString object
     * @return string holding constructed query
     */
    public String getQuery() {
        transaction = new StringBuilder("");

        if(!isValidTransaction())
            return "";
        for (StringBuilder statement : statements) {
            transaction.append(statement);
        }
      return transaction.toString();
    }

    //VALIDATE ------------------------------------------------------
    /**
     * Ensures a query cannot be returned without a built Select and From statement
     * @return a boolean stating whether statement has a select and from statement
     */
    @Override
    public boolean isValidTransaction(){
        return !statements[StmtType.SELECT.ordinal()].toString().equals("")
                && !statements[StmtType.FROM.ordinal()].toString().equals("");
    }

}
