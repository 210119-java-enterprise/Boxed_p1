package com.revature.Boxed.repository;

/**
 * Handles the creation of all DELETE Transactions
 * format: DELETE FROM entity, DELETE FROM entity WHERE condition
 *
 * @author Gabrielle Luna
 */
public class DeleteBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    /**
     * Represents basic structure of DELETE statement
     */
    private enum StmtType {
        DELETE{
            @Override
            public String toString(){ return name() + " FROM "; }
        },
        WHERE

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
    /**
     * Ensures each transaction has a Delete statement included and adds the
     * where statement if one is needed
     * @return boolean stating whether minimum features are present
     */
    @Override
    public boolean isValidTransaction() {
        if (numConditions > 0) {
            statements[StmtType.WHERE.ordinal()].append(whereBuilder.getTransaction());
            whereBuilder = null;
        }

        return !statements[StmtType.DELETE.ordinal()].toString().equals("");
    }
}
