package com.friday20.isyiar.model;

public class SignUp {
    private String name, username, email, password, usertype;

    public SignUp(String name, String username, String email, String password, String usertype){
        this.name=name;
        this.username=username;
        this.email=email;
        this.password=password;
        this.usertype=usertype;
    }

    public SignUp(){}

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsertype() {
        return usertype;
    }
}
