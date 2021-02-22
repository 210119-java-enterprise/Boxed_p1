package com.revature.Boxed.repository;


public class WhereBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    private enum StmtType {
        WHERE, LIST_CONDITIONS;

        @Override
        public String toString(){ return name() + " "; }
    }
    private boolean chainWithAnd = true;

    //Constructors --------------------------------------------------
    public WhereBuilder () {statements = new StringBuilder[StmtType.values().length];}

    public WhereBuilder craftNewTransaction(){
        super.newTransaction();
        statements[StmtType.WHERE.ordinal()].append(StmtType.WHERE.toString());
        return this;
    }

    //Chain Operator ------------------------------------------------
    public WhereBuilder switchToOr() {chainWithAnd = false; return this;}
    public WhereBuilder switchToAnd() {chainWithAnd = true; return this;}

    //Conditions ----------------------------------------------------
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

    @Override
    public boolean isValidTransaction(){
        return !statements[StmtType.WHERE.ordinal()].toString().equals("")
                && !statements[StmtType.LIST_CONDITIONS.ordinal()].toString().equals("");
    }
}
