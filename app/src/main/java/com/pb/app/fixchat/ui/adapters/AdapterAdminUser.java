package com.pb.app.fixchat.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.entityV2.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterAdminUser extends RecyclerView.Adapter<AdapterAdminUser.ViewHolder> {

    private ArrayList<User> contents;

    public AdapterAdminUser(ArrayList<User> contents) {
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_admin_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String userName = contents.get(position).getName();
        String userEmail = contents.get(position).getEmail();
        holder.email.setText(userEmail);
        holder.name.setText(userName);
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
        TextView email;
        TextView name;
        ToggleButton type;
        Spinner spinnerSettings;

        public ViewHolder(View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.admin_name_user);
            name = itemView.findViewById(R.id.admin_subname_user);
            type = itemView.findViewById(R.id.button_usertype_toggle);
            spinnerSettings = itemView.findViewById(R.id.admin_user_spinner);
            spinnerSettings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView)view).setText(null);
                    }
                public void onNothingSelected(AdapterView<?> arg0) {}
            });
        }
    }
}
