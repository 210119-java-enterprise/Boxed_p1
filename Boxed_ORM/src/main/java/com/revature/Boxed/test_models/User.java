package com.revature.Boxed.test_models;

import com.revature.Boxed.annotations.*;

import static com.revature.Boxed.model.ColumnType.*;

/**
 * Demo class for testing purposes, shouldn't be in final package
 *
 * @author Wezley Singleton
 * @author Gabrielle Luna
 */
@Entity(tableName = "users_demo")
@CredentialsClass
public class User {

    @Column(type = PK, columnName = "id")
    @Generated
    private int id;

    @Column(type = DEFAULT, columnName = "first_name")
    private String firstName;

    @Column(type = DEFAULT, columnName = "last_name")
    private String lastName;

    @Column(type = DEFAULT, columnName = "email_address")
    private String emailAddress;

    //USERNAME must be listed first to work

    @Column(type = DEFAULT, columnName = "username")
    @Credential
    private String username;

    @Column(type = DEFAULT, columnName = "password")
    @Credential
    private String password;

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
