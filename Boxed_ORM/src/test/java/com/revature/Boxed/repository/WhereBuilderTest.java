package com.revature.Boxed.repository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WhereBuilderTest {
    WhereBuilder whereBuilder = new WhereBuilder();

    @Test
    public void testWhere_Like(){
        //Arrange
        whereBuilder.newTransaction();
        //Act
        whereBuilder.addCondition_Like("firstName", "%s");
        //Assert
        assertEquals("WHERE firstName LIKE '%s' ",
                whereBuilder.getTransaction());

    }

    @Test
    public void testWhere_InList(){
        //Arrange
        whereBuilder.newTransaction();
        //Act
        String[] list = {"Carl", "Chris", "Carrie"};
        whereBuilder.addCondition_In("firstName", list);
        //Assert
        assertEquals("WHERE firstName IN ('Carl', 'Chris', 'Carrie') ",
                whereBuilder.getTransaction());

    }

    @Test
    public void testWhere_InSubQuery(){
        //Arrange
        whereBuilder.newTransaction();
        //Act
        whereBuilder.addCondition_In("firstName", "subQuery");
        //Assert
        assertEquals("WHERE firstName IN (subQuery) ",
                whereBuilder.getTransaction());

    }

    @Test
    public void testWhere_WithoutList(){
        //Arrange
        whereBuilder.newTransaction();
        //Act
        //Assert
        assertEquals("", whereBuilder.getTransaction());
    }
}
