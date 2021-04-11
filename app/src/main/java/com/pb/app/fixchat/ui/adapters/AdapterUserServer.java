package com.pb.app.fixchat.ui.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.kyleduo.switchbutton.SwitchButton;
import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.CallV2;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entity.ResponseEntity;
import com.pb.app.fixchat.api.entityV2.Server;
import com.pb.app.fixchat.ui.HomeActivity;
import com.pb.app.fixchat.ui.fragments.dialogs.DialogServerForce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterUserServer extends RecyclerView.Adapter<AdapterUserServer.ViewHolder> {

    private ArrayList<Server> contents;
    private Activity activity;

    public AdapterUserServer(ArrayList<Server> contents, Activity activity) {
        this.contents = contents;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_server, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Server server = contents.get(position);
        String serverName = server.getName();
        String serverName2 = server.getHv();
        String power = server.getState();
        String network = server.getNetwork();

        final ProgressBar progressBarPower = holder.progressPower;
        final ProgressBar progressBarNetwork = holder.progressNetwork;
        holder.name.setText(serverName);
        holder.port.setText(serverName2);
        holder.power.setChecked(power.equals("Running"));
        holder.power.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        holder.power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton button, final boolean isChecked) {
                if(button.isPressed()) {
                    Log.d("SERVER_POWER_CHECKED", " " + isChecked);
                    button.setVisibility(View.INVISIBLE);
                    progressBarPower.setVisibility(View.VISIBLE);
                    if (!button.isChecked()) {
                        DialogServerForce.getInstance().createAlertDialog(activity, server, button, progressBarPower);
                    } else {
                        CallV2.getInstance().controlServer(server.getId(), Server.START_POWER);
                    }
                    CallV2.getInstance().getServerState().observe(HomeActivity.getOwner(), new Observer<Map<String, Server>>() {
                        @Override
                        public void onChanged(Map<String, Server> map) {
                            if (map.containsKey(Server.STOP_POWER) ||
                                    map.containsKey(Server.STOP_POWER_FORCE) || map.containsKey(Server.START_POWER)) {
                                button.setVisibility(View.VISIBLE);
                                progressBarPower.setVisibility(View.INVISIBLE);
                                if (isChecked != map.values().iterator().next().getState().equals("accepted")) {
                                    Toast.makeText(activity, "Ошибка на сервере", Toast.LENGTH_SHORT).show();
                                    button.setChecked(map.values().iterator().next().getState().equals("accepted"));
                                }
                            }
                        }
                    });
                }
            }
        });
        holder.network.setChecked(network.equals("Running"));
        holder.network.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton button, boolean isChecked) {
                if (isChecked){
                    button.setVisibility(View.INVISIBLE);
                    progressBarNetwork.setVisibility(View.VISIBLE);
                    if (button.isChecked()){
                        CallV2.getInstance().controlServer(server.getId(), Server.STOP_NETWORK);
                    } else {
                        CallV2.getInstance().controlServer(server.getId(), Server.START_NETWORK);
                    }
                    CallV2.getInstance().getServerState().observe(HomeActivity.getOwner(), new Observer<Map<String, Server>>() {
                        @Override
                        public void onChanged(Map<String, Server> map) {
                            if (map.containsKey(Server.START_NETWORK) || map.containsKey(Server.STOP_NETWORK)) {
                                button.setChecked(map.values().iterator().next().getState().equals("accepted"));
                                button.setVisibility(View.VISIBLE);
                                progressBarNetwork.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (contents == null)
            return 0;
        return contents.size();
    }

    public void setItems(List<Server> prod) {
        contents.clear();
        contents.addAll(prod);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView port;
        SwitchButton power;
        SwitchButton network;
        ProgressBar progressPower;
        ProgressBar progressNetwork;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name_server);
            port = itemView.findViewById(R.id.user_name_host);
            power = itemView.findViewById(R.id.user_switch_power);
            network = itemView.findViewById(R.id.user_switch_web);
            progressPower = itemView.findViewById(R.id.user_progress_power);
            progressNetwork = itemView.findViewById(R.id.user_progress_web);
        }
    }
}
