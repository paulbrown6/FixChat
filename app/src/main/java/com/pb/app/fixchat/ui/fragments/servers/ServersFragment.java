package com.pb.app.fixchat.ui.fragments.servers;

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

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.ui.adapters.AdapterContent;

import java.util.ArrayList;

public class ServersFragment extends Fragment {

    private ServersViewModel mViewModel;
    private AdapterContent adapter;
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
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Залупенец");
        adapter = new AdapterContent(arr);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServersViewModel.class);
        // TODO: Use the ViewModel
    }

}