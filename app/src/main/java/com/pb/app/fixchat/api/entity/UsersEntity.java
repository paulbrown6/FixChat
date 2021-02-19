package com.pb.app.fixchat.api.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersEntity {

    @SerializedName("data")
    @Expose
    private List<User> users;
    @SerializedName("ok")
    @Expose
    private boolean ok;
    @SerializedName("error")
    @Expose
    private String error;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

    @Override
    public String toString() {
        return "UsersEntity{" +
                "servers='" + users + '\'' +
                ", ok=" + ok +
                ", error='" + error + '\'' +
                '}';
    }
}