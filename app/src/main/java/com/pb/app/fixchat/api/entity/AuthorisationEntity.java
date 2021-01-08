package com.pb.app.fixchat.api.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorisationEntity {

    @SerializedName("data")
    @Expose
    private TokenEntity tokenEntity;
    @SerializedName("ok")
    @Expose
    private boolean ok;
    @SerializedName("error")
    @Expose
    private String error;

    public TokenEntity getTokenEntity() {
        return tokenEntity;
    }

    public void setTokenEntity(TokenEntity tokenEntity) {
        this.tokenEntity = tokenEntity;
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
        return "AuthorisationEntity{" +
                "tokenEntity=" + tokenEntity +
                ", ok=" + ok +
                ", error='" + error + '\'' +
                '}';
    }
}