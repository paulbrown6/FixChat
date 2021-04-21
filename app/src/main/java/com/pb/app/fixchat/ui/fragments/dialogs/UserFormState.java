package com.pb.app.fixchat.ui.fragments.dialogs;

import androidx.annotation.Nullable;

class UserFormState {
    @Nullable
    private Integer companyError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    UserFormState(@Nullable Integer companyError, @Nullable Integer usernameError,
                  @Nullable Integer emailError, @Nullable Integer passwordError) {
        this.companyError = companyError;
        this.usernameError = passwordError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    UserFormState(boolean isDataValid) {
        this.companyError = null;
        this.usernameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getCompanyError() {
        return companyError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}