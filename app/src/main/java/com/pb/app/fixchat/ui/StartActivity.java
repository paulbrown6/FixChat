package com.pb.app.fixchat.ui;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.pb.app.fixchat.R;
import com.pb.app.fixchat.api.CallV2;
import com.pb.app.fixchat.api.RetrofitCall;
import com.pb.app.fixchat.api.entity.AuthorisationEntity;
import com.pb.app.fixchat.api.entityV2.User;
import com.pb.app.fixchat.data.database.DatabaseSQL;
import com.pb.app.fixchat.data.model.LoggedInUser;
import com.pb.app.fixchat.data.model.TokenDatabase;
import com.pb.app.fixchat.ui.login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        intent = new Intent(StartActivity.this, LoginActivity.class);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TokenDatabase td = DatabaseSQL.getInstance().writeTokenFromDB(this);
        if (td.getRefToken().equals(" ")) launchActivity(intent);

        ImageView logo = findViewById(R.id.start_logo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        logo.startAnimation(anim);

//        RetrofitCall.getInstance().refresh(td.getRefToken());
        CallV2.getInstance().refreshToken(td.getRefToken());
        CallV2.getInstance().getSignState().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d("START ACTIVITY", "Ответ: " + user.toString());
                if (user.getId() != null) {
                    intent = new Intent(StartActivity.this, HomeActivity.class);
                    intent.putExtra("role", user.getRole());

                    DatabaseSQL.getInstance().deleteEntityFromDb(getApplicationContext());
                    DatabaseSQL.getInstance().readTokenToDb(getApplicationContext(), CallV2.getRefreshToken());
                }
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        launchActivity(intent);
                    }
                }, 2000);
            }
        });
//        RetrofitCall.getInstance().getAuthorisationState().observe(this, new Observer<AuthorisationEntity>() {
//            @Override
//            public void onChanged(AuthorisationEntity authorisationEntity) {
//                Log.d("START ACTIVITY", "Ответ: " + authorisationEntity);
//                if (authorisationEntity.getTokenEntity() != null) {
//                    intent = new Intent(StartActivity.this, HomeActivity.class);
//                    intent.putExtra("role", authorisationEntity.getTokenEntity().getRole());
//
//                    DatabaseSQL.getInstance().deleteEntityFromDb(getApplicationContext());
//                    DatabaseSQL.getInstance().readTokenToDb(getApplicationContext(), authorisationEntity.getTokenEntity().getRefToken());
//                }
//                new Handler().postDelayed(new Runnable(){
//                    public void run() {
//                        launchActivity(intent);
//                    }
//                }, 2000);
//            }
//        });
    }

    private void launchActivity(final Intent intent){
        startActivity(intent);
        finish();
    }
}