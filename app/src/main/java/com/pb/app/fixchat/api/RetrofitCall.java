package com.pb.app.fixchat.api;

import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pb.app.fixchat.api.entity.AuthorisationEntity;
import com.pb.app.fixchat.api.entity.ResponseEntity;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.api.entity.ServerEntity;
import com.pb.app.fixchat.api.entity.ServersEntity;
import com.pb.app.fixchat.api.entity.User;
import com.pb.app.fixchat.api.entity.UserEntity;
import com.pb.app.fixchat.api.entity.UsersEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCall {

    private static RetrofitCall instance;

    private final MutableLiveData<AuthorisationEntity> authorisationData = new MutableLiveData<>();
    private final MutableLiveData<UsersEntity> usersData = new MutableLiveData<>();
    private final MutableLiveData<UserEntity> userData = new MutableLiveData<>();
    private final MutableLiveData<ServersEntity> serversData = new MutableLiveData<>();
    private final MutableLiveData<ServerEntity> serverData = new MutableLiveData<>();

    private static String token;
    private static Integer role;

    private ServersEntity allServers;
    private ServersEntity userServers;
    private UsersEntity allUsers;
    private UsersEntity serverUsers;
    private ServerEntity server;
    private UserEntity user;

    public static RetrofitCall getInstance(){
        if(instance == null) instance = new RetrofitCall();
        return instance;
    }

    // Authorisation

    public void authorisation(String email, String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("API", "Передача в json: " + jsonObject.toString());
        App.getInstance().getApi().authorisation(jsonObject.toString()).enqueue(new Callback<AuthorisationEntity>() {
            @Override
            public void onResponse(Call<AuthorisationEntity> call, Response<AuthorisationEntity> response) {
                if (response.code() == 200){
                    if (response.body() != null) {
                        token = response.body().getTokenEntity().getToken();
                        AuthorisationEntity authorisation = response.body();
                        role = Integer.parseInt(parsRole(token));
                        authorisation.getTokenEntity().setRole(role);
                        authorisationData.postValue(response.body());
                    }
                    Log.d("API", "Авторизация: " + response.code() + " || " + response.body().toString() + " || " + response.message());
                } else {
                    authorisationData.postValue(new AuthorisationEntity());
                    Log.d("API", "Авторизация не прошла: " + response.message() + " || код " + response.code());
                    FirebaseCrashlytics.getInstance().log("Авторизация не прошла: " + response.message() + " || код " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthorisationEntity> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.toString());
                authorisationData.postValue(new AuthorisationEntity());
            }
        });
    }

    public void refresh(String refToken){
        App.getInstance().getApi().refresh("Bearer " + refToken).enqueue(new Callback<AuthorisationEntity>() {
            @Override
            public void onResponse(Call<AuthorisationEntity> call, Response<AuthorisationEntity> response) {
                if (response.code() == 200){
                    if (response.body() != null) {
                        token = response.body().getTokenEntity().getToken();
                        AuthorisationEntity authorisation = response.body();
                        role = Integer.parseInt(parsRole(token));
                        authorisation.getTokenEntity().setRole(role);
                        authorisationData.postValue(authorisation);
                    }
                    Log.d("API", "Вход: " + response.code() + " || " + response.body().toString() + " || " + response.message());
                } else {
                    Log.d("API", "Вход не прошел: " + response.message() + " || код " + response.code());
                    FirebaseCrashlytics.getInstance().log("Вход не прошел: " + response.message() + " || код " + response.code());
                    authorisationData.postValue(new AuthorisationEntity());
                }
            }

            @Override
            public void onFailure(Call<AuthorisationEntity> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.toString());
                authorisationData.postValue(new AuthorisationEntity());
            }
        });
    }

    // Servers
    // Методы для работы из вне

    public void getServers(){
        if (role == 1){
            getAdminServers();
        } else {
            getUserServers();
        }
    }

    public void serverPowerOn(Server server){
        if (role == 1){
            adminServerPowerOn();
        } else {
            userServerPowerOn(server);
        }
    }

    public void serverPowerOff(Server server){
        if (role == 1){
            adminServerPowerOff();
        } else {
            userServerPowerOff();
        }
    }

    // Методы для вызова

    private void getAdminServers(){
        App.getInstance().getApi().getAllServers("Bearer " + token).enqueue(new Callback<ServersEntity>() {
            @Override
            public void onResponse(Call<ServersEntity> call, Response<ServersEntity> response) {
                allServers = new ServersEntity();
                if (response.body() != null){
                    allServers = response.body();
                    serversData.postValue(allServers);
                    Log.d("API", "сервера получены " + response.code() + " || " + response.body().toString() + " || " + response.message());
                } else {
//                    servers.setServers(new ArrayList<Server>());
                    Log.d("API", "сервера не получены" + response.message() + " || код " + response.code());
                    FirebaseCrashlytics.getInstance().log("сервера не получены" + response.message() + " || код " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServersEntity> call, Throwable t) {
                Log.d("API", "сервера не получены");
                System.out.println(t.getMessage());
                System.out.println(call.toString());
            }
        });
    }

    private void getUserServers(){
        App.getInstance().getApi().getUserServers("Bearer " + token).enqueue(new Callback<ServersEntity>() {
            @Override
            public void onResponse(Call<ServersEntity> call, Response<ServersEntity> response) {
                allServers = new ServersEntity();
                if (response.body() != null){
                    allServers = response.body();
                    serversData.postValue(allServers);
                    Log.d("API", "сервера получены " + response.code() + " || " + response.body().toString() + " || " + response.message());
                } else {
                    Log.d("API", "сервера не получены" + response.message() + " || код " + response.code());
                    FirebaseCrashlytics.getInstance().log("сервера не получены" + response.message() + " || код " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServersEntity> call, Throwable t) {
                Log.d("API", "сервера не получены, ошибка с сервера");
                System.out.println(t.getMessage());
                System.out.println(call.toString());
            }
        });
    }

    private void adminServerPowerOn(){

    }

    private void adminServerPowerOff(){

    }

    private void userServerPowerOn(Server server){
        App.getInstance().getApi().powerStart("Bearer " + token, server.getId()).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                allServers = new ServersEntity();
                if (response.body() != null){
                    if (response.body().isOk())
                    serversData.postValue(allServers);
                    Log.d("API", "сервера получены " + response.code() + " || " + response.body().toString() + " || " + response.message());
                } else {
//                    servers.setServers(new ArrayList<Server>());
                    Log.d("API", "сервера не получены" + response.message() + " || код " + response.code());
                    FirebaseCrashlytics.getInstance().log("сервера не получены" + response.message() + " || код " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                Log.d("API", "сервера не получены, ошибка с сервера");
                System.out.println(t.getMessage());
                System.out.println(call.toString());
            }
        });
    }

    private void userServerPowerOff(){

    }

    public void getServer(String id){
        App.getInstance().getApi().getServer("Bearer " + token, id).enqueue(new Callback<ServerEntity>() {
            @Override
            public void onResponse(Call<ServerEntity> call, Response<ServerEntity> response) {
                if (response.code() == 200){
                    server = new ServerEntity();
                    if (response.body().isOk()){
                        server.setServer(response.body().getServer());
                    }
                    serverData.setValue(server);
                    Log.d("API", "сервер получен " + response.code() + " || " + response.body().toString() + " || " + response.message());
                } else {
                    Log.d("API", "сервер не получен" + response.message() + " || код " + response.code());
                    FirebaseCrashlytics.getInstance().log("сервер не получен" + response.message() + " || код " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServerEntity> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.toString());
            }
        });
    }

    // Users

    // Методы для работы из вне

    // Методы для вызова

//    public void getServerUsers(String id){
//        App.getInstance().getApi().getServerUsers("Bearer " + token, id).enqueue(new Callback<UsersEntity>() {
//            @Override
//            public void onResponse(Call<UsersEntity> call, Response<UsersEntity> response) {
//                if (response.code() == 200){
//                    serverUsers = new UsersEntity();
//                    if (response.body().isOk()){
//                        serverUsers = response.body();
//                    }
//                    serverData.setValue(serverUsers);
//                    Log.d("API", "сервер получен " + response.code() + " || " + response.body().toString() + " || " + response.message());
//                } else {
//                    Log.d("API", "сервер не получен" + response.message() + " || код " + response.code());
//                    FirebaseCrashlytics.getInstance().log("сервер не получен" + response.message() + " || код " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UsersEntity> call, Throwable t) {
//                System.out.println(t.getMessage());
//                System.out.println(call.toString());
//            }
//        });
//    }

    public void getAllUsers(){
        App.getInstance().getApi().getAllUsers("Bearer " + token).enqueue(new Callback<UsersEntity>() {
            @Override
            public void onResponse(Call<UsersEntity> call, Response<UsersEntity> response) {
                if (response.code() == 200){
                    allUsers = new UsersEntity();
                    Log.d("API", "ответ на пользователей" + response.code() + " || " + response.body().toString() + " || " + response.message());
                    if (response.body() != null) {
                        allUsers.setUsers(response.body().getUsers());
                        usersData.setValue(allUsers);
                        Log.d("API", "пользователи получены " + response.code() + " || " + response.body().toString() + " || " + response.message());
                    }
                } else {
                    Log.d("API", "пользователи не получены" + response.message() + " || код " + response.code());
                    FirebaseCrashlytics.getInstance().log("пользователи не получены " + response.code() + " || " + response.body().toString() + " || " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UsersEntity> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.toString());
            }
        });
    }

    // Callback methods

    public LiveData<UsersEntity> getUsersState(){
        return usersData;
    }

    public LiveData<ServersEntity> getServersState(){
        return serversData;
    }

    public LiveData<AuthorisationEntity> getAuthorisationState(){
        return authorisationData;
    }

    // Parsing and Decode refresh token

    protected String parsRole(String token){
        String s = " ";
        String[] parts = token.split("[.]");
        try {
            String f = getJson(parts[1]);
            Log.d("TOKEN_DECODED", "Body: " + f);
            s = f.split("role")[1].substring(2,3);
            Log.d("TOKEN_DECODED", "Role: " + s);
        } catch (Exception e) {
            Log.d("TOKEN_DECODED", e.toString());
        }
        return s;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static Integer getRole() {
        return role;
    }

    public static void clear(){
        instance = new RetrofitCall();
    }
}
