package com.rkhs.c_andorid.facebookintegration.Pojo;

/**
 * Created by Admin on 16-01-2018.
 */

public class LoginDetails {

    public String username;
    public String password;

    public LoginDetails(String[] loginDetails) {
        username = loginDetails[0];
        password = loginDetails[1];
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
