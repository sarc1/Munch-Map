package com.example.munch_map;

public class User {
    // For the Purpose of Logging Usernames and Active Emails
    private int id;
    private String username;
    private String email;
    public static boolean isActive = false;

    public User(int id, String username, String email, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        User.isActive = isActive;
    }

    public User() {}

    public int getId() {
        return id;
    }

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
