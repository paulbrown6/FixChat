package com.pb.app.fixchat.ui.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.ApiCall;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.api.entity.User;
import com.pb.app.fixchat.ui.adapters.AdapterAddServers;
import com.pb.app.fixchat.ui.adapters.AdapterAdminServer;
import com.pb.app.fixchat.ui.adapters.AdapterUserServer;

import java.util.ArrayList;

public class DialogAddServers {

    private static DialogAddServers instance;
    private DialogAddServers(){}

    private static AlertDialog alert;
    private static ArrayList<Server> servers;
    private static AdapterAddServers adapter;
    private static RecyclerView recyclerView;

    public static DialogAddServers getInstance() {
        if (instance == null){
            instance = new DialogAddServers();
        }
        return instance;
    }

    public AlertDialog createAlertDialog(Activity activity, final User user, LifecycleOwner owner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_add_servers, null)).setCancelable(true);
        alert = builder.create();
        alert.show();
        servers = new ArrayList<>();
        servers.addAll(ApiCall.getAllServers());
        recyclerView = alert.findViewById(R.id.servers_recycle_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        ApiCall.getInstance().getUserServers(user.getId());
        ApiCall.getInstance().getUserServersState().observe(owner, new Observer<ArrayList<Server>>() {
            @Override
            public void onChanged(ArrayList<Server> newServers) {
                if (!newServers.isEmpty()){
                    servers = newServers;
                    adapter = new AdapterAddServers(servers);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        Button buttonCancel = alert.findViewById(R.id.button_dialog_servers_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.cancel();
                }
            });
        Button buttonAdd = alert.findViewById(R.id.button_dialog_servers_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> changeServers = new ArrayList<>();
                for (int i = 0; i < adapter.getContents().size(); i++){
                    if (adapter.getContents().get(i).isHaveUser()){
                        changeServers.add(adapter.getContents().get(i).getId());
                    }
                }
                ApiCall.getInstance().changeUserServers(user.getId(), changeServers);
                alert.dismiss();
            }
        });
        TabLayout tabLayout = alert.findViewById(R.id.tab_servers);
        tabLayout.setSelected(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!tab.getText().equals("Все")){
                    ArrayList<Server> userServers = new ArrayList<>();
                    for (Server server : servers) {
                        if (server.isHaveUser()){
                            userServers.add(server);
                        }
                    }
                    adapter = new AdapterAddServers(userServers);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter = new AdapterAddServers(servers);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return alert;
    }


}
