package com.revature.Boxed.repository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UpdateBuilderTest {
    UpdateBuilder updateBuilder = new UpdateBuilder();

    @Test
    public void testUpdate_SingleField(){
        //Arrange
        updateBuilder.newTransaction();
        //Act
        updateBuilder.ofEntityType("User");
        updateBuilder.updateKeyValuePair("type", "basic", true);
        //Assert
        assertEquals("UPDATE User SET type = 'basic' ",
                updateBuilder.getTransaction());
    }

    @Test
    public void testUpdate_MultFields(){
        //Arrange
        updateBuilder.newTransaction();
        //Act
        updateBuilder.ofEntityType("User");
        updateBuilder.updateKeyValuePair("type", "basic", true);
        updateBuilder.updateKeyValuePair("lastName", "NULL", false);
        //Assert
        assertEquals("UPDATE User SET type = 'basic', lastName = NULL ",
                updateBuilder.getTransaction());
    }

    @Test
    public void testUpdate_Where(){
        //Arrange
        updateBuilder.newTransaction();
        //Act
        updateBuilder.ofEntityType("User");
        updateBuilder.updateKeyValuePair("firstName", "Gabby", true);
        updateBuilder.addCondition_Operator("lastName", "=",
                "Luna", true);
        //Assert
        assertEquals("UPDATE User SET firstName = 'Gabby' WHERE lastName = 'Luna' ",
                updateBuilder.getTransaction());
    }

    @Test
    public void testUpdate_WithoutUpdate(){
        //Arrange
        updateBuilder.newTransaction();
        //Act
        updateBuilder.updateKeyValuePair("firstName", "Gabby", true);
        updateBuilder.addCondition_Operator("lastName", "=",
                "Luna", true);
        //Assert
        assertEquals("",
                updateBuilder.getTransaction());
    }

    @Test
    public void testUpdate_WithoutSet(){
        //Arrange
        updateBuilder.newTransaction();
        //Act
        updateBuilder.ofEntityType("User");
        updateBuilder.addCondition_Operator("lastName", "=",
                "Luna", true);
        //Assert
        assertEquals("",
                updateBuilder.getTransaction());
    }
}
