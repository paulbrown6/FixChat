package com.pb.app.fixchat.ui.fragments.users;

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

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.CallV2;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entity.ServersEntity;
import com.pb.app.fixchat.api.entity.UsersEntity;
import com.pb.app.fixchat.api.entityV2.User;
import com.pb.app.fixchat.ui.HomeActivity;
import com.pb.app.fixchat.ui.adapters.AdapterAdminServer;
import com.pb.app.fixchat.ui.adapters.AdapterAdminUser;
import com.pb.app.fixchat.ui.adapters.AdapterUserServer;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private static LifecycleOwner owner;
    private UsersViewModel mViewModel;
    private AdapterAdminUser adapter;
    private RecyclerView recyclerView;

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = root.findViewById(R.id.users_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        owner = HomeActivity.getOwner();

//        RetrofitCall.getInstance().getAllUsers();
        CallV2.getInstance().getUsers();
        CallV2.getInstance().getUsersState().observe(owner, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                adapter = new AdapterAdminUser(users);
                recyclerView.setAdapter(adapter);
            }
        });
//        RetrofitCall.getInstance().getUsersState().observe(owner, new Observer<UsersEntity>(){
//            @Override
//            public void onChanged(UsersEntity usersEntity) {
//                adapter = new AdapterAdminUser(usersEntity.getUsers());
//                recyclerView.setAdapter(adapter);
//            }
//        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        // TODO: Use the ViewModel
    }

}