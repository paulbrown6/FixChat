package com.pb.app.fixchat.api.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserEntity {

    @SerializedName("data")
    @Expose
    private User user;
    @SerializedName("ok")
    @Expose
    private boolean ok;
    @SerializedName("error")
    @Expose
    private String error;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}