package com.pb.app.fixchat.ui.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entity.Server;

public class DialogServerForce {

    private static DialogServerForce instance;
    private DialogServerForce(){}

    private static AlertDialog alert;
    private CheckBox checkForce;
    private Button buttonCancel;
    private Button buttonPowerOff;

    public static DialogServerForce getInstance() {
        if (instance == null){
            instance = new DialogServerForce();
        }
        return instance;
    }

    public AlertDialog createAlertDialog(Activity activity, final Server server) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_server_force, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        checkForce = alert.findViewById(R.id.checkBox_dialog_force);
        buttonCancel = alert.findViewById(R.id.button_dialog_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        buttonPowerOff = alert.findViewById(R.id.button_dialog_power_off);
        buttonPowerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean force = false;
                if (checkForce.isChecked()){
                    force = true;
                }
                RetrofitCall.getInstance().serverPowerOff(server, force);
                alert.dismiss();
            }
        });
        return alert;
    }
}
