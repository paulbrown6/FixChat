package com.pb.app.fixchat.ui.fragments.servers;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entity.ServersEntity;
import com.pb.app.fixchat.ui.HomeActivity;
import com.pb.app.fixchat.ui.adapters.AdapterAdminServer;
import com.pb.app.fixchat.ui.adapters.AdapterUserServer;

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
        View root = inflater.inflate(R.layout.fragment_servers, container, false);
        recyclerView = root.findViewById(R.id.servers_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        owner = HomeActivity.getOwner();

        RetrofitCall.getInstance().getServers();
        RetrofitCall.getInstance().getServersState().observe(owner, new Observer<ServersEntity>(){
            @Override
            public void onChanged(ServersEntity serversEntity) {
                if (RetrofitCall.getRole() == 1) { adapter = new AdapterAdminServer(serversEntity.getServers()); }
                else {adapter = new AdapterUserServer(serversEntity.getServers());}
                recyclerView.setAdapter(adapter);
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