package com.pb.app.fixchat.data.model;

public class TokenDatabase {

    private String refToken;

    private String id;

    public TokenDatabase(String refToken, String id){
        this.refToken = refToken;
        this.id = id;
    }

    public String getRefToken() {
        return refToken;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TokenDatabase{" +
                "refToken='" + refToken + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
