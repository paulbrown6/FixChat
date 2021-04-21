package com.pb.app.fixchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.ApiCall;
import com.pb.app.fixchat.api.entity.User;
import com.pb.app.fixchat.data.database.DatabaseSQL;
import com.pb.app.fixchat.ui.fragments.dialogs.DialogEditUser;
import com.pb.app.fixchat.ui.fragments.servers.ServersFragment;
import com.pb.app.fixchat.ui.login.LoginActivity;

public class HomeActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    private NavigationView navigationView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static LifecycleOwner owner;
    private ImageButton exit;
    private TextView toolbarName;
    private MenuItem searchItem;
    private MaterialSearchView searchView;
    private MenuItem addUser;
    private static HomeActivity activity;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        owner = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        if (getIntent().getExtras().getInt("role") == 1) {
            onCreateAdministrator(savedInstanceState);
        } else {
            onCreateUser(savedInstanceState);
        }

    }

    protected void onCreateAdministrator(Bundle savedInstanceState) {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_servers, R.id.nav_users)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_users) {
                    addUser.setVisible(true);
                }
                navigationView.setCheckedItem(item.getItemId());
                NavigationUI.onNavDestinationSelected(item, navController);
                drawer.close();
                return true;
            }
        });
    }

    protected void onCreateUser(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        exit = findViewById(R.id.button_exit);
        toolbarName = findViewById(R.id.toolbar_name);
        visibleToolbarElements(true);
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

    private void visibleToolbarElements(Boolean bool){
        if (exit == null) return;
        if (bool) {
            exit.setVisibility(View.VISIBLE);
            toolbarName.setVisibility(View.VISIBLE);
        } else {
            exit.setVisibility(View.INVISIBLE);
            toolbarName.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_user) {
            DialogEditUser.getInstance().createAlertDialog(this, null, owner);
        }
        if (item.getItemId() == android.R.id.home) {
            if (navController.getPreviousBackStackEntry() != null) {
                addUser.setVisible(false);
                navController.popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        addUser.setVisible(false);
        super.onBackPressed();
    }

    public void onExit(MenuItem item) {exit();}

    private void exit() {
        DatabaseSQL.getInstance().deleteEntityFromDb(getApplicationContext());
        ApiCall.clear();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    public static LifecycleOwner getOwner() {
        return owner;
    }
    public static HomeActivity getActivity() {return activity;}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        addUser = menu.findItem(R.id.add_user);
        addUser.setVisible(false);
        searchItem = menu.findItem(R.id.search);
        searchView = findViewById(R.id.search_view);
        searchView.setMenuItem(searchItem);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                visibleToolbarElements(false);
            }

            @Override
            public void onSearchViewClosed() {
                visibleToolbarElements(true);
            }
        });
        return true;
    }

}