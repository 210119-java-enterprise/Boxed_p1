package com.revature.Boxed.repository;

/**
 * Handles the cretion of all UPDATE Transactions
 * format: UPDATE entity SET column = value, ... ,
 * UPDATE entity SET column = value, ... WHERE condition
 *
 * @author Gabrielle Luna
 */
public class UpdateBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    /**
     * Represents the basic structure of UPDATE statement
     */
    private enum StmtType {
        UPDATE, SET, COLUMNS, WHERE;

        @Override
        public String toString(){ return name() + " ";}
    }

    int numKVPairs;

    //Constructors --------------------------------------------------
    public UpdateBuilder() {statements = new StringBuilder[StmtType.values().length];}

    public void craftNewTransaction(){
        super.newTransaction();
        numKVPairs = 0;
    }

    //Update --------------------------------------------------------
    public void ofEntityType(String entityType){
        super.setType(entityType, StmtType.UPDATE.toString());
    }

    /**
     * Allows the key (column) and its value to be set at once.
     * format : SET column = value, ...
     * @param key           the column being updated
     * @param value         the value being updates
     * @param isString      boolean stating whether the value needs to be wrapped in ' '
     */
    public void updateKeyValuePair(String key, String value, boolean isString){
        numKVPairs++;

        if (numKVPairs == 1){
            statements[StmtType.SET.ordinal()]
                    .append(StmtType.SET.toString());
        }else{
            statements[StmtType.SET.ordinal()]
                    .append(", ");
        }

        statements[StmtType.SET.ordinal()]
                .append(key).append(" ")
                .append("= ");

        if (isString)
            statements[StmtType.SET.ordinal()]
                    .append("'");
        statements[StmtType.SET.ordinal()]
                .append(value);
        if (isString)
            statements[StmtType.SET.ordinal()]
                    .append("'");
    }


    /**
     * Ensures a query cannot be returned without a UPDATE or SET statement and adds
     * the where statement isone is needed
     * @return boolean stating whether minimum features are present
     */
    @Override
    public boolean isValidTransaction() {
        if (numConditions > 0) {
            statements[StmtType.WHERE.ordinal()].append(whereBuilder.getTransaction());
            whereBuilder = null;
        }

        statements[StmtType.SET.ordinal()].append(" ");
        return !statements[StmtType.UPDATE.ordinal()].toString().equals("")
                && !statements[StmtType.SET.ordinal()].toString().equals(" ");
    }
}
