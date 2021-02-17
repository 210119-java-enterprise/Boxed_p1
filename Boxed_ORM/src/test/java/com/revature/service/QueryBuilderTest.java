package com.revature.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class QueryBuilderTest {

    QueryBuilder sut;

    @Before
    public void setUpTest() { sut = new QueryBuilder();}

    @After
    public void tearDownTest(){ sut = null; }

    @Test
    public void test_isValid_whenNotGiven_anySetup(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.getQuery();

        //Assert
        Assert.assertEquals("", result);

    }

    @Test
    public void test_isValid_whenOnlyGiven_Where(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.addCondition_Between("this", "0", "100")
                            .getQuery();

        //Assert
        Assert.assertEquals("", result);

    }

    @Test
    public void test_isValid_whenOnlyGiven_FromAndWhere(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.ofClassType("User")
                .addCondition_Between("this", "==", "that")
                .getQuery();

        //Assert
        Assert.assertEquals("", result);

    }

    @Test
    public void test_isValid_whenOnlyGiven_SelectAndWhere(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .addCondition_Between("this", "==", "that")
                .getQuery();

        //Assert
        Assert.assertEquals("", result);

    }

    @Test
    public void test_isValid_whenQuery_SelectAll(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .ofClassType("User")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 ", result);

    }

    @Test
    public void test_isValid_whenQuery_SelectField(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields("username")
                .ofClassType("User")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT username FROM User T1 ", result);

    }

    @Test
    public void test_isValid_whenQuery_SelectFieldsTrue(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields("username", "password")
                .ofClassType("User")
                .addCondition_Operator("username", "=", "Gabby", true)
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT username, password FROM User T1 WHERE username = 'Gabby' ", result);

    }

    @Test
    public void test_isValid_whenQuery_SelectFieldsFalse(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields("username", "password")
                .ofClassType("User")
                .addCondition_Operator("username", "=", "Gabby", false)
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT username, password FROM User T1 WHERE username = Gabby ", result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_isValid_whenSelectFields_givenEmpty(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String[] list = new String[0];
        String result = sut.returnFields(list)
                .ofClassType("User")
                .getQuery();
    }

    @Test
    public void test_isValid_whenQuery_Between(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .ofClassType("User")
                .addCondition_Between("id", "0", "50")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE id BETWEEN 0 AND 50 ", result);

    }

    @Test
    public void test_isValid_whenQuery_Like(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .ofClassType("User")
                .addCondition_Like("username", "Ga%")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE username LIKE 'Ga%' ", result);
    }

    @Test
    public void test_isValid_whenQuery_InList(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String[] list = {"a", "b", "c"};
        String result = sut.returnFields()
                .ofClassType("User")
                .addCondition_In("firstName", list)
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE firstName IN ('a', 'b', 'c') ", result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void test_exceptionThrown_whenQuery_InList(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String[] list = {};
        String result = sut.returnFields()
                .ofClassType("User")
                .addCondition_In("firstName", list)
                .getQuery();

        //Assert
    }

    @Test
    public void test_isValid_whenQuery_InSubQuery(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .ofClassType("User")
                .addCondition_In("firstName", "query")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE firstName IN (query) ", result);
    }

    @Test
    public void test_isValid_whenQuery_JOIN(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .ofClassType("User")
                .joinWith("type", QueryBuilder.JoinType.INNER)
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 INNER JOIN type T2 ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_exceptionThrown_whenQuery_JOIN(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .joinWith("type", QueryBuilder.JoinType.INNER)
                .getQuery();

        //Assert
    }

    @Test
    public void test_exceptionThrown_whenQuery_JOINON(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields()
                .ofClassType("User")
                .joinOn("type", QueryBuilder.JoinType.LEFT, "this", "that")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 LEFT JOIN type T2 ON T1.this = T2.that ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_stringValidation_whenGiven_specialChar(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields("Wall$").getQuery();

        //Assert

    }
    @Test(expected = IllegalArgumentException.class)
    public void test_stringValidation_whenGiven_numericStart(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields("0Wall").getQuery();

        //Assert

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_stringValidation_whenGiven_numericStartempty(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.returnFields("").getQuery();

        //Assert

    }

    @Test (expected = IllegalArgumentException.class)
    public void test_conditionOperatorValidation_whenGiven_badOperator(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.addCondition_Operator("this", "=>", "that", false).getQuery();

        //Assert
    }

    @Test (expected = IllegalArgumentException.class)
    public void test_conditionOperatorValidation_whenGiven_empty(){
        //Arrange
        sut.craftNewTransaction();

        //Act
        String result = sut.addCondition_Operator("this", "", "that", false).getQuery();

        //Assert
    }
}
