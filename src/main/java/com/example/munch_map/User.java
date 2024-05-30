package com.example.munch_map;

public class User {
    private String username;
    private String email;
    public static boolean isActive = false;

    private static UserSession userSession;

    public User(String username, String email, boolean isActive) {
        this.username = username;
        this.email = email;
        User.isActive = isActive;
        initializeUserSession(this);
    }

    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        if (userSession != null) {
            userSession.setUsername(username);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        if (userSession != null) {
            userSession.setEmail(email);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static void initializeUserSession(User user) {
        if (userSession == null) {
            userSession = new UserSession(user);
        }
    }

    public static UserSession getUserSession() {
        if (userSession == null) {
            throw new IllegalStateException("UserSession not initialized");
        }
        return userSession;
    }

    public static class UserSession {
        private static UserSession instance;
        private String username;
        private String email;
        private int userId;

        private UserSession(User user) {
            this.username = user.getUsername();
            this.email = user.getEmail();
        }

        public static void initialize(User user) {
            if (instance == null) {
                instance = new UserSession(user);
            }
        }

        public static UserSession getInstance() {
            if (instance == null) {
                throw new IllegalStateException("UserSession not initialized");
            }
            return instance;
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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
