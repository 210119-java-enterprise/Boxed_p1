package com.revature.Boxed.repository;

public abstract class TransactionBuilder {
    StringBuilder transaction;
    StringBuilder[] statements;

    int numConditions;
    WhereBuilder whereBuilder;

    void newTransaction(){
        for (int i = 0; i < statements.length; i++) {
            statements[i] = new StringBuilder("");
        }

        numConditions = 0;
    }

    //Class Type ----------------------------------------------------
    void setType(String entityName, String start){
        //Validate
        isValidName(entityName);

        statements[0]
                .append(start)
                .append(entityName).append(" ");
    }

    //Where ---------------------------------------------------------
    private void prepWhere(){
        //Allow for condition chaining
        numConditions++;
        if(numConditions == 1){
            whereBuilder = new WhereBuilder();
            whereBuilder.newTransaction();
        }
    }
    public void addCondition_Operator(String thisField, String conditionOperator,
                           String thatField, boolean isString){
        prepWhere();
        whereBuilder.addCondition_Operator(thisField, conditionOperator, thatField, isString);
    }

    void addCondition_Between(String thisField, String lowerBound, String upperBound) {
        prepWhere();
        whereBuilder.addCondition_Between(thisField, lowerBound, upperBound);
    }

    void addCondition_Like(String thisField, String stringComparison){
        prepWhere();
        whereBuilder.addCondition_Like(thisField, stringComparison);
    }

    void addCondition_In(String thisField, String[] listValues){
        prepWhere();
        whereBuilder.addCondition_In(thisField, listValues);
    }

    void addCondition_In(String thisField, String subQuery){
        prepWhere();
        whereBuilder.addCondition_In(thisField, subQuery);
    }


    //VALIDATE ------------------------------------------------------
    public abstract boolean isValidTransaction ();

    public void isValidName(String... args)throws IllegalArgumentException {
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !i.matches("[?]")){
                throw new IllegalArgumentException("Only alphanumeric values and _ accepted as class or field names, values may contain numbers but not start with them : " + i);
            }
        }
    }

    public void isValidConditionOperator(String... args) {
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("(=|<|>|<=|>=|!=|<>)")){
                throw new IllegalArgumentException("Only <, >, =, <=, >=, !=, <> are accepted condition operators : " + i);
            }
        }
    }

    //Finalize
    public String getTransaction(){
        transaction = new StringBuilder("");

        if(!isValidTransaction())
            return "";
        for (StringBuilder statement : statements) {
            transaction.append(statement);
        }

        whereBuilder = null;
        return transaction.toString();
    }
}
