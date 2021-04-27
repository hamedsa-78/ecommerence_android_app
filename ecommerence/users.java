package com.example.eating_food;
public class users {

    private String username="hamed";
    private String password="1234";

    public users() {
    }

    public users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
