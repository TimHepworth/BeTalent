package com.betalent.betalent.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private int userId;
    private int companyId;
    private String emailAddress;
    private String password;
    private String forename;
    private String surname;
    private String employeeLevel;
    private Boolean loggedIn;

    public User() {
    }

    public User(int userId, int companyId, String emailAddress, String password, String forename, String surname, String employeeLevel, boolean loggedIn) {
        this.userId = userId;
        this.companyId = companyId;
        this.emailAddress = emailAddress;
        this.password = password;
        this.forename = forename;
        this.surname = surname;
        this.employeeLevel = employeeLevel;
        this.loggedIn = loggedIn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getEmployeeLevel() {
        return employeeLevel;
    }

    public void setEmployeeLevel(String employeeLevel) {
        this.employeeLevel = employeeLevel;
    }

}
