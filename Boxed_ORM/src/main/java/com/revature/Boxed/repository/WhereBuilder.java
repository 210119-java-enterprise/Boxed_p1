package com.revature.Boxed.repository;

/**
 * Handles the cretion of all WHERE Transactions
 * format: WHERE condition, ...
 *
 * @author Gabrielle Luna
 */
public class WhereBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    /**
     * Represents basic structure of WHERE statement
     */
    private enum StmtType {
        WHERE, LIST_CONDITIONS;

        @Override
        public String toString(){ return name() + " "; }
    }
    private boolean chainWithAnd = true;

    //Constructors --------------------------------------------------
    public WhereBuilder () {statements = new StringBuilder[StmtType.values().length];}

    public void newTransaction(){
        super.newTransaction();
        statements[StmtType.WHERE.ordinal()].append(StmtType.WHERE.toString());
    }

    //Chain Operator ------------------------------------------------
    public WhereBuilder switchToOr() {chainWithAnd = false; return this;}
    public WhereBuilder switchToAnd() {chainWithAnd = true; return this;}

    //Conditions ----------------------------------------------------
    /**
     * Adds a condition dependent upon a logic Operator
     * format: this > that
     * @param thisField             the left side of the operator
     * @param conditionOperator     the logic operator used
     * @param thatField             the right side of the operator
     * @param isString              boolean stating whether thatField should be wrapped in ' '
     */
    public void addCondition_Operator(String thisField, String conditionOperator,
                                     String thatField, boolean isString){
        //Validation
        isValidName(thisField);
        isValidConditionOperator(conditionOperator);

        //Allow for condition chaining
        numConditions++;
        if(numConditions > 1){
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append(chainWithAnd? "AND " : "OR ");
        }

        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(thisField).append(" ")
                .append(conditionOperator).append(" ");

        //Wrap string literals
        if (isString)
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append("'");
        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(thatField);
        if (isString)
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append("'");

        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(" ");

    }

    /**
     * Adds a condition that uses the BETWEEN operator
     * format: thisField BETWEEN lowerBound AND upperBound
     * @param thisField         the left side of the operator
     * @param lowerBound        the lower end of accepted range
     * @param upperBound        the upper end of accepted range
     */
    public void addCondition_Between(String thisField, String lowerBound, String upperBound){
        //Validation
        isValidName(thisField);

        //Allow for condition chaining
        numConditions++;
        if(numConditions > 1){
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append(chainWithAnd? "AND " : "OR ");
        }

        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(thisField).append(" ")
                .append("BETWEEN ")
                .append(lowerBound).append(" ")
                .append("AND ")
                .append(upperBound).append(" ");
    }

    /**
     * Adds a condition that uses the LIKE operator
     * format: thisField LIKE '%s'
     * @param thisField         the left side fo the operator
     * @param stringComparison  the string being compared to the left
     */
    public void addCondition_Like(String thisField, String stringComparison){
        //Validation
        isValidName(thisField);

        //Allow for condition chaining
        numConditions++;
        if(numConditions > 1){
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append(chainWithAnd? "AND " : "OR ");
        }

        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(thisField).append(" ")
                .append("LIKE ")
                .append("'").append(stringComparison).append("' ");
    }

    /**
     * Adds a condition that uses the IN operator
     * format: thisField IN (value, ...)
     * @param thisField     the left side of the operator
     * @param listValues    a list of possible matches for 'thisField'
     */
    public void addCondition_In(String thisField, String[] listValues){
        //Validation
        if (listValues == null || listValues.length == 0)
            throw new IllegalArgumentException("IN condition requires a list or sub-query to search, empty lists are not accepted");
        isValidName(thisField);

        //Allow for condition chaining
        numConditions++;
        if(numConditions > 1){
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append(chainWithAnd? "AND " : "OR ");
        }

        //Start string
        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(thisField).append(" ")
                .append("IN ")
                .append("(");

        //get values from list
        String append;
        for (int i = 0; i < listValues.length; i++) {
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append("'").append(listValues[i]).append("'");
            append = i < listValues.length - 1? ", " : "";
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append(append);
        }
        //finish string
        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(") ");
    }

    /**
     * Adds a condition that uses the IN operator
     * format: thisField IN (subQuery)
     * @param thisField     the left side of the operator
     * @param subQuery      the subquery from which a match will be looked for
     */
    public void addCondition_In(String thisField, String subQuery){
        //Validation
        isValidName(thisField, subQuery);

        //Allow for condition chaining
        numConditions++;
        if(numConditions > 1){
            statements[StmtType.LIST_CONDITIONS.ordinal()]
                    .append(chainWithAnd? "AND " : "OR ");
        }

        statements[StmtType.LIST_CONDITIONS.ordinal()]
                .append(thisField).append(" ")
                .append("IN ")
                .append("(").append(subQuery).append(") ");
    }

    /**
     * Ensures each transaction has a LIST_CONDITIONS statement
     * @return boolean stating whether minimum features are present
     */
    @Override
    public boolean isValidTransaction(){
        return !statements[StmtType.WHERE.ordinal()].toString().equals("")
                && !statements[StmtType.LIST_CONDITIONS.ordinal()].toString().equals("");
    }
}
