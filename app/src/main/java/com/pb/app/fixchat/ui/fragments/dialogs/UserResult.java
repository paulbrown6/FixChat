package com.pb.app.fixchat.ui.fragments.dialogs;

import androidx.annotation.Nullable;

class UserResult {
    @Nullable
    private Integer success;
    @Nullable
    private String error;

    UserResult(@Nullable String error) {
        this.error = error;
    }

    UserResult(@Nullable Integer success) {
        this.success = success;
    }

    @Nullable
    Integer getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}