package com.pb.app.fixchat.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class AdapterAddServers extends RecyclerView.Adapter<AdapterAddServers.ViewHolder> {

    private ArrayList<Server> contents;

    public AdapterAddServers(ArrayList<Server> contents) {
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_servers, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.checkBox.setChecked(contents.get(position).isHaveUser());
        holder.checkBox.setText(contents.get(position).getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contents.get(position).setHaveUser(isChecked);
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

    public ArrayList<Server> getContents() {
        return contents;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox_server);
        }
    }
}
