package com.revature.Boxed.repository;

public interface SQLTransaction {
    //VALIDATE ------------------------------------------------------
    boolean isValidTransaction();

    void isValidName(String... args);

    void isValidConditionOperator(String ... args);

}
