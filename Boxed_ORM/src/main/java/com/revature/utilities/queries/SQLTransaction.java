package com.revature.utilities.queries;

public interface SQLTransaction {
    //VALIDATE ------------------------------------------------------
    boolean isValidTransaction();

    void isValidName(String... args);

    void isValidConditionOperator(String ... args);

}