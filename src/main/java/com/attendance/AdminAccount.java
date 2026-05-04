package com.attendance;

public class AdminAccount {
    
    private String username;
    private String password;

    // Default constructor needed for JSON parsing
    public AdminAccount() {
    }

    // Constructor for creating the master account
    public AdminAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
