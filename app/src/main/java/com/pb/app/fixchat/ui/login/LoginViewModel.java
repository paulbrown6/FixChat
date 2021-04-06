package com.pb.app.fixchat.ui.login;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.pb.app.fixchat.api.CallV2;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entity.AuthorisationEntity;
import com.pb.app.fixchat.data.model.LoggedInUser;
import com.pb.app.fixchat.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LoginViewModel() {}

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(LifecycleOwner owner,String email, String password) {
        RetrofitCall.getInstance().authorisation(email, password);
        CallV2.getInstance().signIn(email, password);
        RetrofitCall.getInstance().getAuthorisationState().observe(owner, new Observer<AuthorisationEntity>() {
                    @Override
                    public void onChanged(AuthorisationEntity authorisationEntity) {
                        if (authorisationEntity != null && authorisationEntity.isOk()) {
                            LoggedInUser user =
                                    new LoggedInUser(
                                            authorisationEntity.getTokenEntity().getRefToken(),
                                            authorisationEntity.getTokenEntity().getRole());
                            LoggedInUserView loggedInUserView = new LoggedInUserView(user.getRole());
                            loggedInUserView.setRefToken(authorisationEntity.getTokenEntity().getRefToken());
                            loginResult.setValue(new LoginResult(loggedInUserView));
                        } else {
                            loginResult.setValue(new LoginResult(R.string.login_failed));
                        }
                        Log.d("LOGIN", "AuthEntity: " + authorisationEntity);
                    }
                });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        return username != null && username.trim().length() > 3;
//        if (username == null) {
//            return false;
//        }
//        if (username.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
//        } else {
//            return !username.trim().isEmpty();
//        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }
}