package com.pb.app.fixchat.ui.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.CallV2;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entityV2.Server;

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

    public AlertDialog createAlertDialog(Activity activity, final Server server, final CompoundButton button, final ProgressBar progressBar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_server_force, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                button.setChecked(!button.isChecked());
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        checkForce = alert.findViewById(R.id.checkBox_dialog_force);
        buttonCancel = alert.findViewById(R.id.button_dialog_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.cancel();
                }
            });
        buttonPowerOff = alert.findViewById(R.id.button_dialog_power_off);
        buttonPowerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForce.isChecked()){
                    CallV2.getInstance().controlServer(server.getId(), Server.STOP_POWER_FORCE);
                } else {
                    CallV2.getInstance().controlServer(server.getId(), Server.STOP_POWER);
                }
//                RetrofitCall.getInstance().serverPowerOff(server, force);
                alert.dismiss();
            }
        });
        return alert;
    }


}
