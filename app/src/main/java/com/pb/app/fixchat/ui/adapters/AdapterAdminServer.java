package com.pb.app.fixchat.ui.adapters;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.ApiCall;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class AdapterAdminServer extends RecyclerView.Adapter<AdapterAdminServer.ViewHolder> {

    private ArrayList<Server> contents;

    public AdapterAdminServer(ArrayList<Server> contents) {
        this.contents = contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_admin_server, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Server server = contents.get(position);
        String serverName = server.getName();
        String serverName2 = server.getOut_addr().isEmpty()?server.getOut_addr():"null";
        String power = server.getState();

        holder.name.setText(serverName);
        holder.name2.setText(serverName2);
        holder.power.setChecked(power.equals(Server.STATE_RUNNING));
        final CompoundButton button = holder.power;
        holder.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomeActivity.getActivity(), v);
                if (button.isChecked()){
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu_server_on, popup.getMenu());
                } else {
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu_server_off, popup.getMenu());
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case (R.id.menu_server_start):
                                ApiCall.getInstance().controlServer(server.getId(), Server.START_POWER, button);
                                button.setVisibility(View.INVISIBLE);
                                break;
                            case (R.id.menu_server_cancel):
                                ApiCall.getInstance().controlServer(server.getId(), Server.STOP_POWER, button);
                                button.setVisibility(View.INVISIBLE);
                                break;
                            case (R.id.menu_server_off):
                                ApiCall.getInstance().controlServer(server.getId(), Server.STOP_POWER_FORCE, button);
                                button.setVisibility(View.INVISIBLE);
                                break;
                            case (R.id.menu_server_properties):
                                break;
                        }
                        return true;
                    }
                });
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

    public void setItems(List<Server> prod) {
        contents.clear();
        contents.addAll(prod);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView name2;
        ToggleButton power;
        Button settings;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.admin_name_server);
            name2 = itemView.findViewById(R.id.admin_name_host);
            power = itemView.findViewById(R.id.button_server_toggle);
            settings = itemView.findViewById(R.id.admin_server_button);
        }
    }
}
