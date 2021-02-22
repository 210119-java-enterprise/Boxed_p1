package com.revature.Boxed.repository;

import org.junit.Test;

import static org.junit.Assert.*;

public class InsertBuilderTest {
    InsertBuilder insertBuilder = new InsertBuilder();

    @Test
    public void testInsert_MultiConditions(){
        //Arrange
        insertBuilder.newTransaction();
        //Act
        insertBuilder.ofEntityType("User");
        insertBuilder.insertKeyValuePair("firstName", "Gabby", true);
        insertBuilder.insertKeyValuePair("lastName", "Luna", true);
        //Assert
        assertEquals("INSERT INTO User ( firstName, lastName) VALUES  ( 'Gabby', 'Luna') ",
                insertBuilder.getTransaction());
    }

    @Test
    public void testInsert_SingleCondition(){
        //Arrange
        insertBuilder.newTransaction();
        //Act
        insertBuilder.ofEntityType("User");
        insertBuilder.insertKeyValuePair("firstName", "Gabby", true);
        //Assert
        assertEquals("INSERT INTO User ( firstName) VALUES  ( 'Gabby') ",
                insertBuilder.getTransaction());
    }

    @Test
    public void testInsert_WithoutInsert(){
        //Arrange
        insertBuilder.newTransaction();
        //Act
        insertBuilder.insertKeyValuePair("firstName", "Gabby", true);
        //Assert
        assertEquals("", insertBuilder.getTransaction());
    }

}
