package com.pb.app.fixchat.api;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class App extends Application {

    private Api api;
    private Retrofit retrofit;

    private static App instance;

    public App(){
        retrofit = new Retrofit.Builder().baseUrl("https://dc.kmsys.ru:53324/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public static App getInstance(){
        if(instance == null) instance = new App();
        return instance;
    }

    public Api getApi() {
        return api;
    }
}
