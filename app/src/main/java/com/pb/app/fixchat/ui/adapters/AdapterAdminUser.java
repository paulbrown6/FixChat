package com.pb.app.fixchat.ui.adapters;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.entity.User;
import com.pb.app.fixchat.ui.HomeActivity;
import com.pb.app.fixchat.ui.fragments.dialogs.DialogAddServers;
import com.pb.app.fixchat.ui.fragments.dialogs.DialogEditUser;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String userName = contents.get(position).getName();
        String userEmail = contents.get(position).getEmail();
        holder.email.setText(userEmail);
        holder.name.setText(userName);
        holder.type.setChecked(contents.get(position).getRole()==1);
        holder.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomeActivity.getActivity(), v);
                if (contents.get(position).getRole()==1){
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu_user_user, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_user_edit){
                                DialogEditUser.getInstance().createAlertDialog(HomeActivity.getActivity(),
                                        contents.get(position), HomeActivity.getOwner());
                            }
                            return true;
                        }
                    });
                } else {
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu_user_admin, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_user_edit){
                                DialogEditUser.getInstance().createAlertDialog(HomeActivity.getActivity(),
                                        contents.get(position), HomeActivity.getOwner());
                            } else if (item.getItemId() == R.id.menu_user_servers){
                                DialogAddServers.getInstance().createAlertDialog(HomeActivity.getActivity(),
                                        contents.get(position), HomeActivity.getOwner());
                            }
                            return true;
                        }
                    });
                }

                popup.show();
            }
        });
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
        Button settings;

        public ViewHolder(View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.admin_name_user);
            name = itemView.findViewById(R.id.admin_subname_user);
            type = itemView.findViewById(R.id.button_usertype_toggle);
            settings = itemView.findViewById(R.id.admin_user_button);
        }
    }
}
