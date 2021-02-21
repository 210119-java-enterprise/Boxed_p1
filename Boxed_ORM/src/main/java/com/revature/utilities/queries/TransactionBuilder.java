package com.revature.utilities.queries;

public abstract class TransactionBuilder implements SQLTransaction {
    StringBuilder transaction;

    StringBuilder[] statements;

    abstract public TransactionBuilder craftNewTransaction();

    //VALIDATE ------------------------------------------------------
    @Override
    public abstract boolean isValidTransaction();

    @Override
    public void isValidName(String... args)throws IllegalArgumentException {
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !i.matches("[?]")){
                throw new IllegalArgumentException("Only alphanumeric values and _ accepted as class or field names, values may contain numbers but not start with them : " + i);
            }
        }
    }

    @Override
    public void isValidConditionOperator(String... args) {
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("(=|<|>|<=|>=|!=|<>)")){
                throw new IllegalArgumentException("Only <, >, =, <=, >=, !=, <> are accepted condition operators : " + i);
            }
        }
    }
}
