package com.pb.app.fixchat.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.api.entity.User;

import java.util.List;

public class AdapterAdminUser extends RecyclerView.Adapter<AdapterAdminUser.ViewHolder> {

    private List<User> contents;

    public AdapterAdminUser(List<User> contents) {
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_admin_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String serverName = contents.get(position).getName();
        String serverName2 = contents.get(position).getEmail();
        holder.name.setText(serverName);
        holder.name2.setText(serverName2);
        holder.type.setChecked(contents.get(position).getRole()==1);
    }

    @Override
    public int getItemCount() {
        if (contents == null)
            return 0;
        return contents.size();
    }

    public void setItems(List<User> prod) {
        contents.clear();
        contents.addAll(prod);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView name2;
        ToggleButton type;
        Spinner spinnerSettings;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.admin_name_user);
            name2 = itemView.findViewById(R.id.admin_subname_user);
            type = itemView.findViewById(R.id.button_usertype_toggle);
            spinnerSettings = itemView.findViewById(R.id.admin_server_spinner);
            spinnerSettings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView)view).setText(null);
                    }
                public void onNothingSelected(AdapterView<?> arg0) {}
            });
        }
    }
}
