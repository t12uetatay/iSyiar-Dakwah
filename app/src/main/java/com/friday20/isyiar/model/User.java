package com.friday20.isyiar.model;

public class User {
    private String username, nama, email;
    private String userType;

    public User (String username, String nama, String email, String userType){
        this.username=username;
        this.nama=nama;
        this.email=email;
        this.userType=userType;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }
}
