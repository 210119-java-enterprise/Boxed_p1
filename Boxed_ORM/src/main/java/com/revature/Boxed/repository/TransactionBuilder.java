package com.revature.Boxed.repository;

/**
 * Has basic validation for all SQL transactions and acts as intermediary for
 * WHERE statements. Parent class to all Builder classes
 *
 * @author Gabrielle Luna
 */
public abstract class TransactionBuilder {
    StringBuilder transaction;
    StringBuilder[] statements;

    int numConditions;
    WhereBuilder whereBuilder;

    /**
     * resets the statements array that holds the incomplete Transaction
     */
    void newTransaction(){
        for (int i = 0; i < statements.length; i++)
            statements[i] = new StringBuilder("");

        whereBuilder = null;
        numConditions = 0;
    }

    //Class Type ----------------------------------------------------
    /**
     * Sets the first statement which typically has the same structure for all Transactions
     * format: TRANSACTION_TYPE entityName
     * @param entityName    the name of the table at the center of the query
     * @param start         the start to the chosen transaction ex: "INSERT INTO"
     */
    void setType(String entityName, String start){
        //Validate
        isValidName(entityName);

        statements[0]
                .append(start)
                .append(entityName).append(" ");
    }

    //Where ---------------------------------------------------------
    /**
     * reset the where builder and avoids storing one when not in use
     */
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

    /**
     * Reject any strings that are not SQL friendly
     * @param args  list of String to check
     * @throws IllegalArgumentException if a string is deemed invalid
     */
    public void isValidName(String... args)throws IllegalArgumentException {
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !i.matches("[?]")){
                throw new IllegalArgumentException("Only alphanumeric values and _ accepted as class or field names, values may contain numbers but not start with them : " + i);
            }
        }
    }

    /**
     * Reject any logic operators that are not SQL friendly
     * @param args  list of strings to check
     */
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
    /**
     * Final SQL string can be built by appending strings in the same order as they
     * are presented in the statements array
     * @return  SQL string
     */
    public String getTransaction(){
        transaction = new StringBuilder("");

        if(!isValidTransaction())
            return "";
        for (StringBuilder statement : statements) {
            transaction.append(statement);
        }

        return transaction.toString();
    }
}
