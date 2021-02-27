package com.pb.app.fixchat.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.kyleduo.switchbutton.SwitchButton;
import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entity.ResponseEntity;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.ui.HomeActivity;
import com.pb.app.fixchat.ui.fragments.dialogs.DialogServerForce;

import java.util.List;

public class AdapterUserServer extends RecyclerView.Adapter<AdapterUserServer.ViewHolder> {

    private List<Server> contents;
    private Activity activity;

    public AdapterUserServer(List<Server> contents, Activity activity) {
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
        String power = server.getPower();
        String network = server.getNetwork();

        final ProgressBar progressBarPower = holder.progressPower;
        final ProgressBar progressBarNetwork = holder.progressNetwork;
        holder.name.setText(serverName);
        holder.port.setText(serverName2);
        holder.power.setChecked(power.equals("Running"));
        holder.power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton button, boolean isChecked) {
                if (isChecked){
                    button.setVisibility(View.INVISIBLE);
                    progressBarPower.setVisibility(View.VISIBLE);
                    if (button.isChecked()){
                        DialogServerForce.getInstance().createAlertDialog(activity, server);
                    } else {
                        RetrofitCall.getInstance().serverPowerOn(server);
                    }
                    RetrofitCall.getInstance().getServerPower().observe(HomeActivity.getOwner(), new Observer<ResponseEntity>() {
                        @Override
                        public void onChanged(ResponseEntity responseEntity) {
                            button.setChecked(responseEntity.isOk());
                            button.setVisibility(View.VISIBLE);
                            progressBarPower.setVisibility(View.INVISIBLE);
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
                        RetrofitCall.getInstance().serverNetworkStop(server);
                    } else {
                        RetrofitCall.getInstance().serverNetworkStart(server);
                    }
                    RetrofitCall.getInstance().getServerNetwork().observe(HomeActivity.getOwner(), new Observer<ResponseEntity>() {
                        @Override
                        public void onChanged(ResponseEntity responseEntity) {
                            button.setChecked(responseEntity.isOk());
                            button.setVisibility(View.VISIBLE);
                            progressBarNetwork.setVisibility(View.INVISIBLE);
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
