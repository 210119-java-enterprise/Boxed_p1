package com.revature.Boxed.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WhereBuilderTest {
    WhereBuilder sut;
    //WhereBuilder sut = new WhereBuilder();

    @Before
    public void setUpTest() { sut = new WhereBuilder();}

    @After
    public void tearDownTest(){ sut = null; }

    @Test
    public void test_Where_Like(){
        //Arrange
        sut.newTransaction();
        //Act
        sut.addCondition_Like("firstName", "%s");
        //Assert
        assertEquals("WHERE firstName LIKE '%s' ",
                sut.getTransaction());

    }

    @Test
    public void test_Where_InList(){
        //Arrange
        sut.newTransaction();
        //Act
        String[] list = {"Carl", "Chris", "Carrie"};
        sut.addCondition_In("firstName", list);
        //Assert
        assertEquals("WHERE firstName IN ('Carl', 'Chris', 'Carrie') ",
                sut.getTransaction());

    }

    @Test
    public void test_Where_InSubQuery(){
        //Arrange
        sut.newTransaction();
        //Act
        sut.addCondition_In("firstName", "subQuery");
        //Assert
        assertEquals("WHERE firstName IN (subQuery) ",
                sut.getTransaction());

    }

    @Test
    public void test_Where_WithoutList(){
        //Arrange
        sut.newTransaction();
        //Act
        //Assert
        assertEquals("", sut.getTransaction());
    }
}
