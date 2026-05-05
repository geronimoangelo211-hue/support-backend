package com.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AdminAccount {
    
    @Id
    private String username;
    private String password;
    private String role; 
    private Long lastOnline; 

    public AdminAccount() {}

    public AdminAccount(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.lastOnline = System.currentTimeMillis(); 
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getLastOnline() { return lastOnline; }
    public void setLastOnline(Long lastOnline) { this.lastOnline = lastOnline; }
}
