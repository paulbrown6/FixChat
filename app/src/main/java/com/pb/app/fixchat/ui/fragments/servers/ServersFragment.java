package com.pb.app.fixchat.ui.fragments.servers;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.ApiCall;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.ui.HomeActivity;
import com.pb.app.fixchat.ui.adapters.AdapterAdminServer;
import com.pb.app.fixchat.ui.adapters.AdapterUserServer;

import java.util.ArrayList;
import java.util.Map;

public class ServersFragment extends Fragment {

    private static LifecycleOwner owner;
    private ServersViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    public static ServersFragment newInstance() {
        return new ServersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_servers, container, false);
        recyclerView = root.findViewById(R.id.servers_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        owner = HomeActivity.getOwner();
        final Activity activity = this.getActivity();
        ApiCall.getInstance().getServers();
        ApiCall.getInstance().getServersState().observe(owner, new Observer<ArrayList<Server>>() {
            @Override
            public void onChanged(ArrayList<Server> servers) {
                if (ApiCall.getUser().getRole() == 1) { adapter = new AdapterAdminServer(servers); }
                else {adapter = new AdapterUserServer(servers, activity);}
                recyclerView.setAdapter(adapter);
            }
        });
        ApiCall.getInstance().getControlState().observe(owner, new Observer<Map<CompoundButton, Server>>() {
            @Override
            public void onChanged(Map<CompoundButton, Server> map) {
                CompoundButton button = null;
                Server server = null;
                for (Map.Entry<CompoundButton, Server> entry : map.entrySet()) {
                    button = entry.getKey();
                    server = entry.getValue();
                }
                if (button.getVisibility() == View.INVISIBLE) {
                    if (server != null && server.getState().equals("accepted")) {
                        Log.d("SERVER_COMMAND", "Операция выполнена");
                        Toast.makeText(activity, "Операция выполнена", Toast.LENGTH_SHORT).show();
                        button.toggle();
                    } else {
                        Log.e("SERVER_COMMAND", "Ошибка на сервере");
                        Toast.makeText(activity, "Ошибка на сервере", Toast.LENGTH_SHORT).show();
                    }
                }
                button.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServersViewModel.class);
        // TODO: Use the ViewModel
    }

}