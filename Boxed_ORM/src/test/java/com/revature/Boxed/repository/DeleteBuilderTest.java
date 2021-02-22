package com.revature.Boxed.repository;

import org.junit.Test;
import static org.junit.Assert.*;

public class DeleteBuilderTest {
    DeleteBuilder deleteBuilder = new DeleteBuilder();

    @Test
    public void TestDelete(){
        //Arrange
        deleteBuilder.newTransaction();
        //Act
        deleteBuilder.ofEntityType("User");
        //Assert
        assertEquals("DELETE FROM User ",
                    deleteBuilder.getTransaction());
    }

    @Test
    public void TestDeleteWith_Condition(){
        //Arrange
        deleteBuilder.newTransaction();
        //Act
        deleteBuilder.ofEntityType("User");
        deleteBuilder.addCondition_Operator("firstName", "=", "Gandalf", true);
        //Assert
        assertEquals("DELETE FROM User WHERE firstName = 'Gandalf' ",
                deleteBuilder.getTransaction());
    }

    @Test
    public void TestDeleteInValid_WithoutWhere(){
        //Arrange
        deleteBuilder.newTransaction();
        //Act
        deleteBuilder.newTransaction();
        //Assert
        assertEquals("", deleteBuilder.getTransaction());
    }
}