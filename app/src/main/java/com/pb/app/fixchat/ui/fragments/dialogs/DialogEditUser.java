package com.pb.app.fixchat.ui.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.entity.User;

public class DialogEditUser {

    private static DialogEditUser instance;
    private DialogEditUser(){}

    private static AlertDialog alert;
    private static User userEdit;
    private static UserViewModel userViewModel;

    public static DialogEditUser getInstance() {
        if (instance == null){
            instance = new DialogEditUser();
        }
        return instance;
    }

    @SuppressLint("ResourceType")
    public AlertDialog createAlertDialog(Activity activity, final User user, final LifecycleOwner owner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_edit_user, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        userEdit = new User();
        userViewModel = new UserViewModel();
        TextView dialogName = alert.findViewById(R.id.dialog_name);
        final Button editUser = alert.findViewById(R.id.button_dialog_edit_user);
        Button cancel = alert.findViewById(R.id.button_dialog_cancel_edit_user);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        final Spinner userRole = alert.findViewById(R.id.add_user_role);
        final EditText userName = alert.findViewById(R.id.add_user_name);
        final EditText company = alert.findViewById(R.id.add_company_name);
        final EditText email = alert.findViewById(R.id.add_email);
        final EditText password = alert.findViewById(R.id.add_password);
        final TextView errorMessage = alert.findViewById(R.id.error_message);
        editUser.setEnabled(false);
        if (user == null){
            dialogName.setText(R.string.add_user);
            editUser.setText(R.string.add);
        } else {
            if (user.getRole() == 1) {
                userRole.setSelection(1);
            }
            userEdit.setId(user.getId());
            company.setText(user.getCompany());
            userName.setText(user.getName());
            email.setText(user.getEmail());
        }
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                userViewModel.loginDataChanged(company.getText().toString(), userName.getText().toString(),
                        email.getText().toString(), password.getText().toString());
            }
        };
        company.addTextChangedListener(afterTextChangedListener);
        userName.addTextChangedListener(afterTextChangedListener);
        email.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEdit.setCompany(company.getText().toString());
                userEdit.setName(userName.getText().toString());
                userEdit.setEmail(email.getText().toString());
                userEdit.setRole(userRole.getSelectedItemPosition());
                userViewModel.call(owner, userEdit, password.getText().toString());
            }
        });
        userViewModel.getUserFormState().observe(owner, new Observer<UserFormState>() {
            @Override
            public void onChanged(@Nullable UserFormState userFormState) {
                if (userFormState == null) {
                    return;
                }
                editUser.setEnabled(userFormState.isDataValid());
                if (userFormState.getCompanyError() != null) {
                    company.setError(userFormState.getCompanyError().toString());
                }
                if (userFormState.getUsernameError() != null) {
                    userName.setError(userFormState.getUsernameError().toString());
                }
                if (userFormState.getEmailError() != null) {
                    email.setError(userFormState.getEmailError().toString());
                }
                if (userFormState.getPasswordError() != null) {
                    password.setError(userFormState.getPasswordError().toString());
                }
            }
        });
        userViewModel.getUserResult().observe(owner, new Observer<UserResult>() {
            @Override
            public void onChanged(@Nullable UserResult userResult) {
                if (userResult == null) {
                    errorMessage.setVisibility(View.INVISIBLE);
                    return;
                }
                if (userResult.getError() != null) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(userResult.getError());
                }
                if (userResult.getSuccess() != null) {
                    errorMessage.setVisibility(View.INVISIBLE);
                    alert.cancel();
                }
            }
        });
        return alert;
    }
}
