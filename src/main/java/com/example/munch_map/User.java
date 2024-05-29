package com.example.munch_map;

public class User {
    // For the Purpose of Logging Usernames and Active Emails
    private String username;
    private String email;
    private boolean isActive = false;

    public User(String username, String email, boolean isActive) {
        this.username = username;
        this.email = email;
        this.isActive = isActive;
    }

    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
