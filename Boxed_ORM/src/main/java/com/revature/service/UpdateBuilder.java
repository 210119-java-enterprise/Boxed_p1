package com.revature.service;


/**
 *
 */
public class UpdateBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    public enum StmtTypes {
        INSERT, VALUES, UPDATE, DELETE;

        @Override
        public String toString(){ return name() + " ";}
    }
    int numEntries;


    //Constructors --------------------------------------------------
    public UpdateBuilder() {
        statements = new StringBuilder[StmtTypes.values().length];
    }

    public UpdateBuilder craftNewTransaction(){
        for (int i = 0; i < statements.length; i++) {
            statements[i] = new StringBuilder("");
        }
        numEntries = 0;
        return this;
    }

    //INSERT --------------------------------------------------------
    /**
     *
     * @param ofClassType
     * @return
     */
    public UpdateBuilder setUpEntry(String ofClassType){
        //Validate
        isValidName(ofClassType);

        statements[StmtTypes.INSERT.ordinal()]
                .append(StmtTypes.INSERT.toString())
                .append("INTO ")
                .append(ofClassType).append(" ");

        return this;
    }

    /**
     *
     * @param ofClassType
     * @param forFields
     * @return
     */
    public UpdateBuilder setUpInsertTransaction(String ofClassType, String ... forFields){
        //Validate
        isValidName(ofClassType);
        isValidName(forFields);

        statements[StmtTypes.INSERT.ordinal()]
                .append(StmtTypes.INSERT.toString())
                .append("INTO ")
                .append(ofClassType).append(" ")
                .append("(");

        String append;
        for (int i = 0; i < forFields.length; i++) {
            append = i < forFields.length - 1? ", " : "";
            statements[StmtTypes.INSERT.ordinal()]
                    .append(forFields[i]).append(append);
        }

        statements[StmtTypes.INSERT.ordinal()]
                .append(") ");
        return this;
    }

    public UpdateBuilder addInsertValues(String ... fieldValues){
        numEntries++;

        //Allowing for a list of new entries
        if (numEntries > 1)
            statements[StmtTypes.VALUES.ordinal()]
                    .append(", ");
        else {
            //Start statement
            statements[StmtTypes.VALUES.ordinal()]
                    .append(StmtTypes.VALUES.toString());
        }
         statements[StmtTypes.VALUES.ordinal()].append("(");

        //Parse list of data to be entered
        String append;
        for (int i = 0; i < fieldValues.length; i++) {
            append = i < fieldValues.length - 1? ", " : "";
            statements[StmtTypes.VALUES.ordinal()]
                    .append(fieldValues[i]).append(append);
        }
        //End statement
        statements[StmtTypes.VALUES.ordinal()]
                .append(")");

        return this;
    }

    public String getInsertTransaction (){
        transaction = new StringBuilder("");

        transaction.append(statements[StmtTypes.INSERT.ordinal()])
                .append(statements[StmtTypes.VALUES.ordinal()]);

        return transaction.toString();
    }

    //UPDATE --------------------------------------------------------


    @Override
    public boolean isValidTransaction() {
        return false;
    }
}
