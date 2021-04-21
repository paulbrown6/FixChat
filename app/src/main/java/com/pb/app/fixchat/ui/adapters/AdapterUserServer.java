package com.pb.app.fixchat.ui.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.kyleduo.switchbutton.SwitchButton;
import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.ApiCall;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.ui.HomeActivity;
import com.pb.app.fixchat.ui.fragments.dialogs.DialogServerForce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterUserServer extends RecyclerView.Adapter<AdapterUserServer.ViewHolder> {

    private ArrayList<Server> contents;
    private Activity activity;
    private LifecycleOwner owner;

    public AdapterUserServer(ArrayList<Server> contents, Activity activity) {
        this.contents = contents;
        this.activity = activity;
        owner = HomeActivity.getOwner();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_server, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Server server = contents.get(position);
        String serverName = server.getName();
        String serverName2 = server.getOut_addr().isEmpty()?server.getOut_addr():"null";
        String power = server.getState();
        String network = server.getNetwork();

        holder.name.setText(serverName);
        holder.port.setText(serverName2);
        holder.power.setChecked(power.equals(Server.STATE_RUNNING));
        holder.network.setChecked(network.equals(Server.NETWORK_RUNNING));

        holder.power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CompoundButton button = (CompoundButton) view;
                    button.toggle();
                    Log.d("SERVER_POWER", "Click " + button.isPressed());
                    button.setVisibility(View.INVISIBLE);
                    if (button.isChecked()) {
                        DialogServerForce.getInstance().createAlertDialog(activity, server, button);
                    } else {
                        ApiCall.getInstance().controlServer(server.getId(), Server.START_POWER, button);
                    }
            }
        });
        holder.network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompoundButton button = (CompoundButton) view;
                button.toggle();
                Log.d("SERVER_NETWORK", "Click " + button.isPressed());
                button.setVisibility(View.INVISIBLE);
                if (button.isChecked()) {
                    ApiCall.getInstance().controlServer(server.getId(), Server.STOP_NETWORK, button);
                } else {
                    ApiCall.getInstance().controlServer(server.getId(), Server.START_NETWORK, button);
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
