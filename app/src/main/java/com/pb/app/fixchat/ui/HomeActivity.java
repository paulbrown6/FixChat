package com.pb.app.fixchat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pb.app.fixchat.R;
import com.pb.app.fixchat.data.database.DatabaseSQL;
import com.pb.app.fixchat.ui.fragments.servers.ServersFragment;
import com.pb.app.fixchat.ui.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    private NavigationView navigationView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static Integer role;
    private static LifecycleOwner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        role = getIntent().getExtras().getInt("role");
        owner = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (role == 1) {
            onCreateAdministrator(savedInstanceState);
        } else {
            onCreateUser(savedInstanceState);
        }

    }

    protected void onCreateAdministrator(Bundle savedInstanceState) {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_servers, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
    }

    protected void onCreateUser(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final ImageButton exit = findViewById(R.id.button_exit);
        exit.setVisibility(View.VISIBLE);
        TextView toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setVisibility(View.VISIBLE);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, new ServersFragment());
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (navController.getPreviousBackStackEntry() != null) {
                navController.popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    public void onExit(MenuItem item) {
        exit();
    }

    private void exit() {
        DatabaseSQL.getInstance().deleteEntityFromDb(getApplicationContext());
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    public static LifecycleOwner getOwner() {
        return owner;
    }

    public static Integer getRole(){
        return role;
    }
}