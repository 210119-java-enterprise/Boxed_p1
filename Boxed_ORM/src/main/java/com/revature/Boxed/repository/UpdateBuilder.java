package com.revature.Boxed.repository;

public class UpdateBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    public enum StmtType {
        UPDATE, SET, COLUMNS, WHERE;

        @Override
        public String toString(){ return name() + " ";}
    }
    int numKVPairs;

    //Constructors --------------------------------------------------
    public UpdateBuilder() {statements = new StringBuilder[StmtType.values().length];}

    public UpdateBuilder craftNewTransaction(){
        for (int i = 0; i < statements.length; i++) {
            statements[i] = new StringBuilder("");
        }
        numKVPairs = 0;
        return this;
    }

    //Update --------------------------------------------------------
    public UpdateBuilder updateType(String ofClassType){
        //Validate
        isValidName(ofClassType);

        statements[StmtType.UPDATE.ordinal()]
                .append(StmtType.UPDATE.toString())
                .append( ofClassType ).append(" ");

        return this;
    }

    public UpdateBuilder updateKeyValuePair(String key, String value, boolean isString){
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
                .append("= ")
                .append(value);

        return this;
    }

    //TODO: Make a where query builder
    public UpdateBuilder updateConditions(String thisField, String conditionOperator, String thatField, boolean isString){
        //Validation
        isValidName(thisField);
        isValidConditionOperator(conditionOperator);

        statements[StmtType.WHERE.ordinal()]
                .append(StmtType.WHERE.toString())
                .append(thisField).append(" ")
                .append(conditionOperator).append(" ");

        //Wrap string literals
        if (isString)
            statements[QueryBuilder.StmtType.WHERE.ordinal()]
                    .append("'");
        statements[QueryBuilder.StmtType.WHERE.ordinal()]
                .append(thatField);
        if (isString)
            statements[QueryBuilder.StmtType.WHERE.ordinal()]
                    .append("'");

        statements[QueryBuilder.StmtType.WHERE.ordinal()]
                .append(" ");

        return this;
    }

    public String getUpdateTransaction(){
        transaction = new StringBuilder("");

        if (isValidTransaction())
            transaction.append(statements[StmtType.UPDATE.ordinal()])
                    .append(statements[StmtType.SET.ordinal()])
                    .append(" ")
                    .append(statements[StmtType.WHERE.ordinal()]);

        return transaction.toString();
    }

    @Override
    public boolean isValidTransaction() {
        return !statements[StmtType.UPDATE.ordinal()].toString().equals("")
                && !statements[StmtType.SET.ordinal()].toString().equals("")
                && !statements[StmtType.WHERE.ordinal()].toString().equals("");
    }
}
