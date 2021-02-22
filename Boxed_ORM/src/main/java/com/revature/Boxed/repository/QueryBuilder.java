package com.revature.Boxed.repository;

/**
 *
 */
public class QueryBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    private enum StmtType {
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

    //Save up to 3 queries
    static String[] speedDial = new String[3];


    //TODO: add aggregate functions
    //TODO: add chained conditions to WHERE clause (or, and)

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
     * @return this QueryBuilder object
     */
    public void returnFields(){
        statements[StmtType.SELECT.ordinal()]
                .append(StmtType.SELECT.toString()).append("* ");
    }

    /**
     * Allows user to specify which columns to get a return value from
     * format SELECT col1, col2, ...
     * @param ColumnNames list of columns to select
     * @return this QueryBuilder object
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
     * @return this QueryBuilder object
     */
    public void ofEntityType(String entityName){
        statements[StmtType.FROM.ordinal()]
                .append(StmtType.FROM.ordinal())
                .append(entityName).append(" ");
        numTables++;
    }

    /**
     * Allows the user to add an additional table to the query via JOIN operation of which there are
     * five types a accepted via the JoinTypes enum{INNER, OUTER, FULL, LEFT, RIGHT}.
     * format: JOIN table T2
     * @param entityName The name of the table being added to the Query
     * @param joinType the JOIN type
     * @return this QueryBuilder object
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
     * @return this QueryBuilder object
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

    //WHERE ---------------------------------------------------------
    //SEE SUPER

    //VALIDATE ------------------------------------------------------
    /**
     * Ensures a query cannot be returned without a built Select and From statement
     * @return a boolean stating whether statement has a select and from statement
     */
    @Override
    public boolean isValidTransaction(){
        if (numConditions > 0) {
            statements[StmtType.WHERE.ordinal()].append(whereBuilder.getTransaction());
            whereBuilder = null;
        }

        return !statements[StmtType.SELECT.ordinal()].toString().equals("")
                && !statements[StmtType.FROM.ordinal()].toString().equals("");
    }

}
