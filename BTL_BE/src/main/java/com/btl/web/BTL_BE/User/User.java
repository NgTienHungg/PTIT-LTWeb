package com.btl.web.BTL_BE.User;

public class User {

    public int id;
    public String username;
    public String password;
    public int role;
    public String email;

    public User(int id, String userName, String password, int role, String email) {
        this.id = id;
        this.username = userName;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public User(String userName, String password, String email) {
        this.username = userName;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
