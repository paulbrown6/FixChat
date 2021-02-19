package com.pb.app.fixchat.ui.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.entity.Server;

public class DialogServerSettingsFragment {

    private static DialogServerSettingsFragment instance;
    private DialogServerSettingsFragment(){}

    private static AlertDialog alert;
    private TextView nameServer;
    private Button clickButton;

    public static DialogServerSettingsFragment getInstance() {
        if (instance == null){
            instance = new DialogServerSettingsFragment();
        }
        return instance;
    }

    public AlertDialog createAlertDialog(Activity activity, final Server server) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_server_settings, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        nameServer = alert.findViewById(R.id.settings_name_server);
        nameServer.setText(server.getName());
        clickButton = alert.findViewById(R.id.settings_button_click);
        clickButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        return alert;
    }
}
