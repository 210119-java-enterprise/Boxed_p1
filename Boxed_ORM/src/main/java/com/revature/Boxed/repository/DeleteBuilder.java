package com.revature.Boxed.repository;

public class DeleteBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    private enum StmtType {
        DELETE{
            @Override
            public String toString(){ return name() + " FROM "; }
        },
        WHERE;

    }

    //Constructors  -------------------------------------------------
    public DeleteBuilder() {
        statements = new StringBuilder[StmtType.values().length];
    }

    //DELETE --------------------------------------------------------
    public void ofEntityType(String entityName){
        super.setType(entityName, StmtType.DELETE.toString());
    }

    //VALIDATE ------------------------------------------------------
    @Override
    public boolean isValidTransaction() {
        if (numConditions > 0) {
            statements[StmtType.WHERE.ordinal()].append(whereBuilder.getTransaction());
            whereBuilder = null;
        }

        return !statements[StmtType.DELETE.ordinal()].toString().equals("");
    }
}
