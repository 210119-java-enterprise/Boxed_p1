package com.revature.model;

import com.revature.annotations.*;

/**
 * Demo class for testing purposes, shouldn't be in final package
 *
 * @author Wezley Singleton
 * @author Gabrielle Luna
 */
@Entity(tableName = "users")
public class User {

    @Column_PK(columnName = "id")
    @Default
    private int id;

    @Column(columnName = "first_name")
    private String firstName;

    @Column(columnName = "last_name")
    private String lastName;

    @Column(columnName = "email_address")
    private String emailAddress;

//    @Column_FK(columnName = "test_relation")
//    private Test testRelation;

    public User() {
        super();
    }

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress=" + emailAddress +
                '}';
    }
}
