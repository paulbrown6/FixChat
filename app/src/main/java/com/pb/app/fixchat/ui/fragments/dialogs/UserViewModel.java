package com.pb.app.fixchat.ui.fragments.dialogs;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.ApiCall;
import com.pb.app.fixchat.api.entity.User;

public class UserViewModel extends ViewModel {

    private MutableLiveData<UserFormState> userFormState = new MutableLiveData<>();
    private MutableLiveData<UserResult> userResult = new MutableLiveData<>();

    UserViewModel() {}

    LiveData<UserFormState> getUserFormState() {
        return userFormState;
    }

    LiveData<UserResult> getUserResult() {
        return userResult;
    }

    public void call(LifecycleOwner owner, User user, String password) {
        if (user.getId() == null) {
            ApiCall.getInstance().createUser(user, password);
        } else {
            ApiCall.getInstance().editUser(user, password);
        }
        ApiCall.getInstance().getUserState().observe(owner, new Observer<User>() {
            @Override
            public void onChanged(User us) {
                if (us.getId() != null) {
                    userResult.setValue(new UserResult(1));
                } else {
                    userResult.setValue(new UserResult(us.getState()));
                }
                Log.d("USER", "User: " + us.toString());
            }
        });
    }

    public void loginDataChanged(String company, String username,
                                 String email, String password) {
        if (!isCompanyValid(company)) {
            userFormState.setValue(new UserFormState(R.string.invalid_username, null,
                    null, null));
        } else if (!isUserNameValid(username)) {
            userFormState.setValue(new UserFormState(null, R.string.invalid_username,
                    null, null));
        } else if (!isEmailValid(email)) {
            userFormState.setValue(new UserFormState(null, null,
                    R.string.fui_invalid_email_address, null));
        } else if (!isPasswordValid(password)) {
            userFormState.setValue(new UserFormState(null, null,
                    null, R.string.invalid_password));
        } else {
            userFormState.setValue(new UserFormState(true));
        }
    }

    private boolean isCompanyValid(String company) {
        return company != null && company.trim().length() > 3;
    }

    private boolean isUserNameValid(String username) {
        return username != null && username.trim().length() > 3;
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }
}