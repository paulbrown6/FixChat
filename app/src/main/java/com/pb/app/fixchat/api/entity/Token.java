package com.pb.app.fixchat.api.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("refresh_token")
    @Expose
    private String refToken;

    private Integer role;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefToken() {
        return refToken;
    }

    public void setRefToken(String refToken) {
        this.refToken = refToken;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}