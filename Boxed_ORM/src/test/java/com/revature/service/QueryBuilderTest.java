package com.revature.service;

import com.revature.model.queries.JoinTypes;
import com.revature.model.queries.QueryString;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class QueryBuilderTest {

    QueryBuilder sut;
    QueryString mockQueryString = mock(QueryString.class);

    @Before
    public void setUpTest() { sut = new QueryBuilder();}

    @After
    public void tearDownTest(){ sut = null; }

    @Test
    public void test_isValid_whenNotGiven_anySetup(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.getQuery();

        //Assert
        Assert.assertEquals("", result);

    }

    @Test
    public void test_isValid_whenOnlyGiven_Where(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.addCondition_Between("this", "0", "100")
                            .getQuery();

        //Assert
        Assert.assertEquals("", result);

    }

    @Test
    public void test_isValid_whenOnlyGiven_FromAndWhere(){
        //Arrange
        sut.craftQuery();

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
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .addCondition_Between("this", "==", "that")
                .getQuery();

        //Assert
        Assert.assertEquals("", result);

    }

    @Test
    public void test_isValid_whenQuery_SelectAll(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .ofClassType("User")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 ", result);

    }

    @Test
    public void test_isValid_whenQuery_SelectField(){
        //Arrange
        sut.craftQuery();

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
        sut.craftQuery();

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
        sut.craftQuery();

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
        sut.craftQuery();

        //Act
        String[] list = new String[0];
        String result = sut.returnFields(list)
                .ofClassType("User")
                .getQuery();
    }

    @Test
    public void test_isValid_whenQuery_Between(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .ofClassType("User")
                .addCondition_Between("id", "0", "50")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE id BETWEEN 0 AND 50 ", result);

    }

    @Test
    public void test_isValid_whenQuery_Like(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .ofClassType("User")
                .addCondition_Like("username", "Ga%")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE username LIKE Ga% ", result);
    }

    @Test
    public void test_isValid_whenQuery_InList(){
        //Arrange
        sut.craftQuery();

        //Act
        String[] list = {"a", "b", "c"};
        String result = sut.returnAllFields()
                .ofClassType("User")
                .addCondition_In("firstName", list)
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE firstName IN ('a', 'b', 'c' ) ", result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void test_exceptionThrown_whenQuery_InList(){
        //Arrange
        sut.craftQuery();

        //Act
        String[] list = {};
        String result = sut.returnAllFields()
                .ofClassType("User")
                .addCondition_In("firstName", list)
                .getQuery();

        //Assert
    }

    @Test
    public void test_isValid_whenQuery_InSubQuery(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .ofClassType("User")
                .addCondition_In("firstName", "query")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 WHERE firstName IN (query) ", result);
    }

    @Test
    public void test_isValid_whenQuery_JOIN(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .ofClassType("User")
                .joinWith("type", JoinTypes.INNER)
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 INNER JOIN type T2 ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_exceptionThrown_whenQuery_JOIN(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .joinWith("type", JoinTypes.INNER)
                .getQuery();

        //Assert
    }

    @Test
    public void test_exceptionThrown_whenQuery_JOINON(){
        //Arrange
        sut.craftQuery();

        //Act
        String result = sut.returnAllFields()
                .ofClassType("User")
                .joinOn("type", JoinTypes.LEFT, "this", "that")
                .getQuery();

        //Assert
        Assert.assertEquals("SELECT * FROM User T1 LEFT JOIN type T2 ON T1.this = T2.that ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_stringValidation_whenGiven_specialChar(){
        //Arrange

        //Act
        QueryBuilder.stringValidation("Wall$");

        //Assert

    }
    @Test(expected = IllegalArgumentException.class)
    public void test_stringValidation_whenGiven_numericStart(){
        //Arrange

        //Act
        QueryBuilder.stringValidation("0Wall");

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_stringValidation_whenGiven_numericStartempty(){

        //Act
        QueryBuilder.stringValidation("");

    }

    @Test (expected = IllegalArgumentException.class)
    public void test_conditionOperatorValidation_whenGiven_badOperator(){
        //Arrange

        //Act
        QueryBuilder.conditionOperatorValidation("==");
    }

    @Test (expected = IllegalArgumentException.class)
    public void test_conditionOperatorValidation_whenGiven_empty(){
        //Arrange

        //Act
        QueryBuilder.conditionOperatorValidation("");
    }
}
