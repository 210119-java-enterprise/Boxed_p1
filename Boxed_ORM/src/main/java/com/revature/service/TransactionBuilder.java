package com.revature.service;

public abstract class TransactionBuilder implements SQLTransaction {
    StringBuilder transaction;

    StringBuilder[] statements;

    abstract public TransactionBuilder craftNewTransaction();

    //VALIDATE ------------------------------------------------------
    @Override
    public abstract boolean isValidTransaction();

    @Override
    public void isValidName(String... args) {
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("[a-zA-Z_][a-zA-Z0-9_]*")){
                throw new IllegalArgumentException("Only alphanumeric values and _ accepted as class or field names");
            }
        }
    }

    @Override
    public void isValidConditionOperator(String... args) {
        for (String i: args) {
            if(i == null || i.trim().equals(""))
                throw new IllegalArgumentException("Arguments cannot be empty!");
            if (!i.matches("(=|<|>|<=|>=|!=|<>)")){
                throw new IllegalArgumentException("Only <, >, =, <=, >=, !=, <> are accepted condition operators");
            }
        }
    }
}
