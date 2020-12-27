package com.pb.app.fixchat.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.app.fixchat.R;

import java.util.List;

public class AdapterContent extends RecyclerView.Adapter<com.pb.app.fixchat.ui.adapters.AdapterContent.ViewHolder> {

    private List<String> contents;

    public AdapterContent(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_servers, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String post = contents.get(position);
        holder.name.setText(post);
        holder.network.setChecked(true);
    }

    @Override
    public int getItemCount() {
        if (contents == null)
            return 0;
        return contents.size();
    }

    public void setItems(List<String> prod) {
        contents.clear();
        contents.addAll(prod);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Switch network;
        Switch connection;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_server);
            network = (Switch) itemView.findViewById(R.id.network_switch);
            connection = (Switch) itemView.findViewById(R.id.connection_swicth);
        }
    }
}
