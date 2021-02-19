package com.pb.app.fixchat.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.kyleduo.switchbutton.SwitchButton;
import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.entity.Server;

import java.util.List;

public class AdapterUserServer extends RecyclerView.Adapter<AdapterUserServer.ViewHolder> {

    private List<Server> contents;

    public AdapterUserServer(List<Server> contents) {
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_server, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String serverName = contents.get(position).getName();
        String serverName2 = contents.get(position).getHv();
        String power = contents.get(position).getPower();
        String network = contents.get(position).getNetwork();

//        final ProgressBar progressBar = holder.progress;

        holder.name.setText(serverName);
        holder.port.setText(serverName2);
//        holder.power.setChecked(power.equals("Running"));
//        holder.power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
//                if (isChecked){
//                    button.setVisibility(View.INVISIBLE);
//                    progressBar.setVisibility(View.VISIBLE);
//
//                }
//            }
//        });
//        holder.settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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
