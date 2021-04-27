package com.example.eating_food;

public class admins {

    private String username="admin";
    private String password="1234";

    public admins() {
    }

    public admins(String username, String password) {
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
