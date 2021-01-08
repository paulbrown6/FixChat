package com.pb.app.fixchat.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private Integer displayName;

    public LoggedInUser(String token, Integer role) {
        this.userId = token;
        this.displayName = role;
    }

    public String getToken() {
        return userId;
    }

    public Integer getRole() {
        return displayName;
    }
}