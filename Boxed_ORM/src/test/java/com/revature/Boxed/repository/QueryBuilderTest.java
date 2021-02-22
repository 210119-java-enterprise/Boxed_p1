package com.revature.Boxed.repository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {
    QueryBuilder queryBuilder = new QueryBuilder();

    @Test
    public void testQuery_All(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.ofEntityType("User");
        queryBuilder.returnFields();
        //Assert
        assertEquals("SELECT * FROM User T1 ",
                queryBuilder.getTransaction());
    }

    @Test
    public void testQuery_SelectField(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.ofEntityType("User");
        queryBuilder.returnFields("UserName");
        //Assert
        assertEquals("SELECT UserName FROM User T1 ",
                queryBuilder.getTransaction());
    }

    @Test
    public void testQuery_SelectFields(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.ofEntityType("User");
        queryBuilder.returnFields("UserName", "Password");
        //Assert
        assertEquals("SELECT UserName, Password FROM User T1 ",
                queryBuilder.getTransaction());
    }

    @Test
    public void testQuery_OuterJoin(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.ofEntityType("User");
        queryBuilder.returnFields();
        queryBuilder.joinWith("Account", QueryBuilder.JoinType.OUTER);
        //Assert
        assertEquals("SELECT * FROM User T1 OUTER JOIN Account T2 ",
                queryBuilder.getTransaction());
    }

    @Test
    public void testQuery_WhereBetween(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.ofEntityType("User");
        queryBuilder.returnFields();
        queryBuilder.addCondition_Between("age", "21", "31");
        //Assert
        assertEquals("SELECT * FROM User T1 WHERE age BETWEEN 21 AND 31 ",
                queryBuilder.getTransaction());
    }

    @Test
    public void testQuery_MultiWhere(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.ofEntityType("User");
        queryBuilder.returnFields();
        queryBuilder.addCondition_Between("age", "21", "31");
        queryBuilder.addCondition_Operator("age", "<>", "25", false);
        //Assert
        assertEquals("SELECT * FROM User T1 WHERE age BETWEEN 21 AND 31 AND age <> 25 ",
                queryBuilder.getTransaction());
    }

    @Test
    public void testQuery_WithoutFrom(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.returnFields();
        queryBuilder.addCondition_Between("age", "21", "31");
        queryBuilder.addCondition_Operator("age", "<>", "25", false);
        //Assert
        assertEquals("", queryBuilder.getTransaction());
    }

    @Test
    public void testQuery_WithoutSelect(){
        //Arrange
        queryBuilder.newTransaction();
        //Act
        queryBuilder.ofEntityType("User");
        queryBuilder.addCondition_Between("age", "21", "31");
        queryBuilder.addCondition_Operator("age", "<>", "25", false);
        //Assert
        assertEquals("", queryBuilder.getTransaction());
    }
}
