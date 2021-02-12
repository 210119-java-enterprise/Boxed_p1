package com.revature.service;

public class TestQueryBuilder {

    public static void main(String[] args) {
        //Query calls
        QueryBuilder builder = new QueryBuilder();

        System.out.println(builder.craftQuery()
                .returnAllFields()
                .ofClassType("User")
                .getQuery());

        System.out.println(builder.craftQuery()
                .returnFields("username")
                .ofClassType("User")
                .getQuery());

        System.out.println(builder.craftQuery()
                .returnFields("username", "password")
                .ofClassType("User")
                .addCondition_Operator("username", "==", "Gabby")
                .getQuery());
        System.out.println(builder.craftQuery()
                .returnAllFields()
                .ofClassType("Users_demo")
                .addCondition_Operator("first_name", "=", "'Gabby'")
                .getQuery());
    }
}
