package com.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AdminAccount {
    
    @Id
    private String username;
    private String password;
    private String role; 

    // Constructors
    public AdminAccount() {}

    public AdminAccount(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
