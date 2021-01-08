package com.pb.app.fixchat.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String refToken;
    private Integer role;

    LoggedInUserView(Integer role) {
        this.role = role;
        if (role == 1){
            displayName = "Администратор";
        } else {
            displayName = "Пользователь";
        }
    }

    String getDisplayName() {
        return displayName;
    }

    Integer getRole(){
        return role;
    }

    public String getRefToken() {
        return refToken;
    }

    public void setRefToken(String refToken) {
        this.refToken = refToken;
    }
}