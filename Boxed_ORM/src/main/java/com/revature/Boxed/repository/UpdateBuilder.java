package com.revature.Boxed.repository;

public class UpdateBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
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
