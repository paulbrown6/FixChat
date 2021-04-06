package com.pb.app.fixchat.api;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AppV2 extends Application {

    private ApiV2 api;
    private Retrofit retrofit;

    private static AppV2 instance;

    public AppV2(){
        retrofit = new Retrofit.Builder().baseUrl("https://dc.kmsys.ru:53338/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiV2.class);
    }

    public static AppV2 getInstance(){
        if(instance == null) instance = new AppV2();
        return instance;
    }

    public ApiV2 getApi() {
        return api;
    }
}
