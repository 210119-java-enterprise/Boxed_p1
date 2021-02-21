package com.revature.utilities.queries;


/**
 *
 */
public class InsertBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    public enum StmtType {
        INSERT, COLUMNS, VALUES;

        @Override
        public String toString(){ return name() + " ";}
    }
    int numEntries;
    int numKVPairs;
    boolean colListed;


    //Constructors --------------------------------------------------
    public InsertBuilder() {
        statements = new StringBuilder[StmtType.values().length];
    }

    public InsertBuilder craftNewTransaction(){
        for (int i = 0; i < statements.length; i++) {
            statements[i] = new StringBuilder("");
        }
        numEntries = numKVPairs = 0;
        colListed = false;
        return this;
    }

    //INSERT --------------------------------------------------------
    /**
     *
     * @param ofClassType
     * @return
     */
    public InsertBuilder insertType(String ofClassType){
        //Validate
        isValidName(ofClassType);

        statements[StmtType.INSERT.ordinal()]
                .append(StmtType.INSERT.toString())
                .append("INTO ")
                .append(ofClassType).append(" ");

        return this;
    }

    public InsertBuilder insertKeyValuePair(String key, String value, boolean isString) {
        numKVPairs ++;
        colListed = true;

        System.out.println(key + " = " + value);
        //Does not work with multiple entries
        if (numEntries == 1)
            return this;

        //This is the first key value to be entered so format start of list
        if (numKVPairs == 1){
            statements[StmtType.COLUMNS.ordinal()]
                    .append("( ");
            statements[StmtType.VALUES.ordinal()]
                    .append(StmtType.VALUES.toString()).append(" ")
                    .append("( ");
        }else { //This is not the first, so add necessary commas to maintain list
            statements[StmtType.COLUMNS.ordinal()]
                    .append(", ");
            statements[StmtType.VALUES.ordinal()]
                    .append(", ");
        }

        //Add values to list
        statements[StmtType.COLUMNS.ordinal()]
                .append(key);

        if (isString)
            statements[StmtType.VALUES.ordinal()].append("'");
        statements[StmtType.VALUES.ordinal()]
                .append(value);
        if (isString)
            statements[StmtType.VALUES.ordinal()].append("'");

        return this;
    }

    public String getInsertTransaction (){
        transaction = new StringBuilder("");

        if (isValidTransaction())
            transaction.append(statements[StmtType.INSERT.ordinal()])
                    .append(statements[StmtType.COLUMNS.ordinal()]);
        if(colListed)
            transaction.append(") ");
        transaction.append(statements[StmtType.VALUES.ordinal()])
                .append(") ");
        System.out.println(transaction.toString());
        return transaction.toString();
    }

    @Override
    public boolean isValidTransaction() {
        return !statements[InsertBuilder.StmtType.INSERT.ordinal()].toString().equals("")
                && !statements[InsertBuilder.StmtType.VALUES.ordinal()].toString().equals("");
    }
}
