package com.revature.Boxed.repository;


/**
 *
 */
public class InsertBuilder extends TransactionBuilder{
    //Attributes ----------------------------------------------------
    private enum StmtType {
        INSERT{
            @Override
            public String toString(){ return name() + " INTO ";}
        }, COLUMNS, VALUES;

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

    public void newTransaction(){
        super.newTransaction();
        numEntries = numKVPairs = 0;
        colListed = false;
    }

    //INSERT --------------------------------------------------------
    /**
     *
     * @param entityName
     */
    public void ofEntityType(String entityName){
        super.setType(entityName, StmtType.INSERT.toString());
    }

    public void insertKeyValuePair(String key, String value, boolean isString) {
        numKVPairs ++;
        colListed = true;

       //Does not work with multiple entries
        if (numEntries == 1)
            return;

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
    }

    @Override
    public boolean isValidTransaction() {
        if(colListed)
            statements[StmtType.COLUMNS.ordinal()].append(") ");

        statements[StmtType.VALUES.ordinal()].append(") ");

        return !statements[StmtType.INSERT.ordinal()].toString().equals("")
                && !statements[StmtType.VALUES.ordinal()].toString().equals("");
    }
}
