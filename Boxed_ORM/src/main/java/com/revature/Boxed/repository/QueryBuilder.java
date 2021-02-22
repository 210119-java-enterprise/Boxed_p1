package com.revature.Boxed.repository;

import jdk.nashorn.internal.objects.annotations.Where;

/**
 * Handles the creation of all SELECT Transactions
 * format: SELECT fields, ... FROM entity, SELECT field FROM entity WHERE condition,
 * SELECT field FROM entity OUTER JOIN entity
 */
public class QueryBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    /**
     * Represents basic structure of SELECT statement
     */
    private enum StmtType {
        SELECT, FROM, JOIN, WHERE;

        @Override
        public String toString() { return name() + " ";}
    }

    /**
     * Represents the available variety of JOINs
     */
    public enum JoinType {
        INNER, OUTER, FULL, LEFT, RIGHT;

        @Override
        public String toString() {
            return name() + " ";
        }
    }


    //statement stats
    private int numTables = 0;

    //Save up to 3 queries
    static String[] speedDial = new String[3];


    //TODO: add aggregate functions

    //Constructors --------------------------------------------------
    public QueryBuilder() {
        statements = new StringBuilder[StmtType.values().length];
    }

    public void newTransaction(){
        super.newTransaction();
        numTables = 0;
    }

    public void saveQuery(int i){
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("SpeedDialQuery: Value must be between 0 and 2 inclusive");

        speedDial[i] = getTransaction();
    }

    public void loadQuery(int i){
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("SpeedDialQuery: Value must be between 0 and 2 inclusive");
        transaction = new StringBuilder(speedDial[i]);
    }

    //SELECT --------------------------------------------------------
    /**
     * Allows the user to add a basic SELECT statement that returns all columns in query
     * format: SELECT *
     */
    public void returnFields(){
        statements[StmtType.SELECT.ordinal()]
                .append(StmtType.SELECT.toString()).append("* ");
    }

    /**
     * Allows user to specify which columns to get a return value from
     * format SELECT col1, col2, ...
     * @param ColumnNames list of columns to select
     */
    public void returnFields(String ... ColumnNames){
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
    }

    //FROM ----------------------------------------------------------
    /**
     * Allows the user to add the table being queried. This is one of two operations considered
     * mandatory to create a valid query
     * format: FROM table T1
     * @param entityName name of the table being queried
     */
    public void ofEntityType(String entityName){
        statements[StmtType.FROM.ordinal()]
                .append(StmtType.FROM.toString())
                .append(entityName).append(" ")
                .append("T1 ");
        numTables++;
    }

    /**
     * Allows the user to add an additional table to the query via JOIN operation of which there are
     * five types a accepted via the JoinTypes enum{INNER, OUTER, FULL, LEFT, RIGHT}.
     * format: JOIN table T2
     * @param entityName The name of the table being added to the Query
     * @param joinType the JOIN type
     */
    public void joinWith(String entityName, JoinType joinType){
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
     */
    public void joinOn(String entityName, JoinType joinType, String mainClassField, String joinClassField){
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

    }

    //VALIDATE ------------------------------------------------------
    /**
     * Ensures a query cannot be returned without a built Select and From statement and
     * adds the where statement if one is needed
     * @return a boolean stating whether statement has a select and from statement
     */
    @Override
    public boolean isValidTransaction(){
        if (numConditions > 0 ) {
            statements[StmtType.WHERE.ordinal()] = new StringBuilder(whereBuilder.getTransaction());
        }

        return !statements[StmtType.SELECT.ordinal()].toString().equals("")
                && !statements[StmtType.FROM.ordinal()].toString().equals("");
    }

}
