package com.revature.Boxed.repository;

import org.junit.Test;

public class TransactionBuilderTest {
    TransactionBuilder transactionBuilder = new TransactionBuilder() {
        @Override
        public boolean isValidTransaction() {
            return true;
        }
    };

    @Test
    public void testValidName(){
        //Arrange
        //Act
        transactionBuilder.isValidName("_car", "Car", "car_", "car0", "C4r_", "?");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidName_EmptyString(){
        //Arrange
        //Act
        transactionBuilder.isValidName("");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidName_WhiteSpace(){
        //Arrange
        //Act
        transactionBuilder.isValidName(  " ");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidName_startsNum(){
        //Arrange
        //Act
        transactionBuilder.isValidName(  "0car");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidName_specialChar(){
        //Arrange
        //Act
        transactionBuilder.isValidName(  "car$");
    }

    @Test
    public void testValidConditionOp(){
        //Arrange
        //Act
        transactionBuilder.isValidConditionOperator(  "=", "<", ">", "<=", ">=", "!=", "<>");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidConditionOp_alpha(){
        //Arrange
        //Act
        transactionBuilder.isValidConditionOperator(  "apple");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidConditionOp_badOp(){
        //Arrange
        //Act
        transactionBuilder.isValidConditionOperator(  "==");
    }
}
