package com.pb.app.fixchat.api;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pb.app.fixchat.api.entity.ResponseApi;
import com.pb.app.fixchat.api.entity.Server;
import com.pb.app.fixchat.api.entity.ValueConverter;
import com.pb.app.fixchat.api.entity.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {

    private static ApiCall instance;
    private static String TAG = "APIv2";
    private static String ACCESS_TOKEN;
    private static String REFRESH_TOKEN;
    private static User user;
    private static ArrayList<Server> allServers;
    private static ArrayList<Server> userServers;

    private final MutableLiveData<User> signLive = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Server>> serversLive = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Server>> userServersLive = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<User>> usersLive = new MutableLiveData<>();
    private final MutableLiveData<Map<String,Server>> serverLive = new MutableLiveData<>();
    private final MutableLiveData<Map<CompoundButton,Server>> commandLive = new MutableLiveData<>();
    private final MutableLiveData<User> userLive = new MutableLiveData<>();

    public static ApiCall getInstance(){
        if(instance == null) {
            instance = new ApiCall();
            allServers = new ArrayList<>();
            userServers = new ArrayList<>();
        }
        return instance;
    }

    //Call

    public void signIn(String email, String password){
        App.getInstance().getApi().signin(JSONMethods.createRequest(
                "email==" + email + "::password==" + password)
                .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        ACCESS_TOKEN = response.body().getMessage().get("access_token");
                        REFRESH_TOKEN = response.body().getMessage().get("refresh_token");
                        user = JSONMethods.parsTokenToUser(ACCESS_TOKEN);
                        signLive.postValue(user);
                        logMessage(true, "Авторизация",
                                response.body().toString() + " || Role: " +
                                        user.getRole(), response.code());
                    } else {
                        signLive.postValue(new User());
                        logMessage(false, "Авторизация", response.body().toString(),
                                response.code());
                    }
                } else {
                    signLive.postValue(new User());
                    logMessage(false, "Авторизация", response.toString(),
                            response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Авторизация", "запрос не прошел || " +
                        t.toString(), 0);
                signLive.postValue(new User());
            }
        });
    }

    public void refreshToken(String token){
        App.getInstance().getApi().refresh(JSONMethods.createRequest(
                "refresh_token==" + token)
                .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        ACCESS_TOKEN = response.body().getMessage().get("access_token");
                        REFRESH_TOKEN = response.body().getMessage().get("refresh_token");
                        user = JSONMethods.parsTokenToUser(ACCESS_TOKEN);
                        signLive.postValue(user);
                        logMessage(true, "Рефрэш",
                                response.body().toString(), response.code());
                    } else {
                        logMessage(false, "Рефрэш",
                                response.body().toString(), response.code());
                        signLive.postValue(new User());
                    }
                } else {
                    logMessage(false, "Рефрэш",
                            response.toString(), response.code());
                    signLive.postValue(new User());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Рефрэш", "запрос не прошел || " +
                        t.toString(), 0);
                signLive.postValue(new User());
            }
        });
    }

    public void getServers(){
        App.getInstance().getApi().getServers("Bearer " + ACCESS_TOKEN).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        ArrayList<Server> servers = new ArrayList<>();
                        if (response.body().getMessage().containsKey("servers")){
                            servers = ValueConverter.convertValueToServers(response.body().
                                    getMessage().get("servers"));
                        }
                        serversLive.postValue(servers);
                        allServers = servers;
                        logMessage(true, "Сервера",
                                response.body().toString(), response.code());
                    } else {
                        serversLive.postValue(new ArrayList<Server>());
                        logMessage(false, "Сервера",
                                response.body().toString(), response.code());
                    }
                } else {
                    serversLive.postValue(new ArrayList<Server>());
                    logMessage(false, "Сервера",
                            response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Сервера", "запрос не прошел || " +
                        t.toString(), 0);
                serversLive.postValue(new ArrayList<Server>());
            }
        });
    }

    public void editServer(@NotNull final Server server, String user, String password){
        App.getInstance().getApi().editServer("Bearer " + ACCESS_TOKEN,
                JSONMethods.createRequest("company==" + server.getCompany() + "::description==" + server.getDescription()
                        + "::out_addr==" + server.getOut_addr() + "::user==" + user + "::password==" + password)
                .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        ArrayMap<String, Server> map = new ArrayMap<>();
                        map.put(Server.EDIT_SERVER, server);
                        serverLive.postValue(map);
                        logMessage(true, "Редактирование сервера",
                                response.body().toString(), response.code());
                    } else {
                        serverLive.postValue(new ArrayMap<String ,Server>());
                        logMessage(false, "Редактирование сервера",
                                response.body().toString(), response.code());
                    }
                } else {
                    serverLive.postValue(new ArrayMap<String ,Server>());
                    logMessage(false, "Редактирование сервера",
                            response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Редактирование сервера", "запрос не прошел || " +
                        t.toString(), 0);
                serverLive.postValue(new ArrayMap<String ,Server>());
            }
        });
    }

    public void deleteServer(String id){
        App.getInstance().getApi().deleteServer("Bearer " + ACCESS_TOKEN,
                JSONMethods.createRequest("id==" + id)
                        .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        Server server = new Server();
                        server.setState("delete");
                        ArrayMap<String, Server> map = new ArrayMap<>();
                        map.put(Server.DELETE_SERVER, server);
                        serverLive.postValue(map);
                        logMessage(true, "Удаление сервера",
                                response.body().toString(), response.code());
                    } else {
                        serverLive.postValue(new ArrayMap<String ,Server>());
                        logMessage(false, "Удаление сервера",
                                response.body().toString(), response.code());
                    }
                } else {
                    serverLive.postValue(new ArrayMap<String ,Server>());
                    logMessage(false, "Удаление сервера",
                            response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Удаление сервера", "запрос не прошел || " +
                        t.toString(), 0);
                serverLive.postValue(new ArrayMap<String ,Server>());
            }
        });
    }

    public void controlServer(String id, final String command, final CompoundButton button){
        App.getInstance().getApi().controlServer("Bearer " + ACCESS_TOKEN,
                JSONMethods.createRequest("server_id==" + id + "::command==" + command)
                        .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        Server server = new Server();
                        server.setState("accepted");
                        ArrayMap<CompoundButton, Server> map = new ArrayMap<>();
                        map.put(button, server);
                        commandLive.postValue(map);
                        logMessage(true, "Контроль сервера",
                                response.body().toString(), response.code());
                    } else {
                        ArrayMap<CompoundButton, Server> map = new ArrayMap<>();
                        map.put(button, null);
                        commandLive.postValue(map);
                        logMessage(false, "Контроль сервера",
                                response.body().toString(), response.code());
                    }
                } else {
                    ArrayMap<CompoundButton, Server> map = new ArrayMap<>();
                    map.put(button, null);
                    commandLive.postValue(map);
                    logMessage(false, "Контроль сервера",
                            response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                ArrayMap<CompoundButton, Server> map = new ArrayMap<>();
                map.put(button, null);
                commandLive.postValue(map);
                logMessage(false, "Контроль сервера", "запрос не прошел || " +
                        t.toString(), 0);
            }
        });
    }

    public void getUsers(){
        App.getInstance().getApi().getUsers("Bearer " + ACCESS_TOKEN).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        ArrayList<User> users = new ArrayList<>();
                        if (response.body().getMessage().containsKey("users")){
                            users = ValueConverter.convertValueToUsers(response.body().
                                    getMessage().get("users"));
                        }
                        usersLive.postValue(users);
                        logMessage(true, "Пользователи",
                                response.body().toString(), response.code());
                    } else {
                        usersLive.postValue(new ArrayList<User>());
                        logMessage(false, "Пользователи",
                                response.body().toString(), response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Пользователи", "запрос не прошел || " +
                        t.toString(), 0);
                usersLive.postValue(new ArrayList<User>());
            }
        });
    }

    public void createUser(@NotNull final User user, String password){
        App.getInstance().getApi().createUser("Bearer " + ACCESS_TOKEN,
                JSONMethods.createRequest("name==" + user.getName() + "::company==" + user.getCompany()
                        + "::email==" + user.getEmail() + "::password==" + password + "::role==" + user.getRole())
                        .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 201 && response.body().getStatus().equals("ok")){
                        userLive.setValue(user);
                        logMessage(true, "Создание пользователя",
                                response.body().toString(), response.code());
                    } else {
                        User user = new User();
                        user.setState("Ошибка " + response.code());
                        userLive.setValue(user);
                        logMessage(false, "Создание пользователя",
                                response.body().toString(), response.code());
                    }
                }  else {
                    User user = new User();
                    user.setState("Ошибка " + response.toString());
                    userLive.setValue(user);
                    logMessage(false, "Создание пользователя",
                        response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Создание пользователя", "запрос не прошел || " +
                        t.toString(), 0);
                User user = new User();
                user.setState("Запрос не прошел");
                userLive.setValue(user);
            }
        });
    }

    public void editUser(@NotNull final User user, String password){
        App.getInstance().getApi().editUser("Bearer " + ACCESS_TOKEN,
                JSONMethods.createRequest("id==" + user.getId() + "::name==" + user.getName() +
                        "::company==" + user.getCompany() + "::email==" + user.getEmail() +
                        "::password==" + password + "::role==" + user.getRole())
                        .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 201 && response.body().getStatus().equals("ok")){
                        userLive.setValue(user);
                        logMessage(true, "Редактирование пользователя",
                                response.body().toString(), response.code());
                    } else {
                        User user = new User();
                        user.setState("Ошибка " + response.code());
                        userLive.setValue(user);
                        logMessage(false, "Редактирование пользователя",
                                response.body().toString(), response.code());
                    }
                }  else {
                    User user = new User();
                    user.setState("Ошибка " + response.toString());
                    userLive.setValue(user);
                    logMessage(false, "Редактирование пользователя",
                            response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Редактирование пользователя", "запрос не прошел || " +
                        t.toString(), 0);
                User user = new User();
                user.setState("Запрос не прошел");
                userLive.setValue(user);
            }
        });
    }

    public void deleteUser(String id){
        App.getInstance().getApi().deleteUser("Bearer " + ACCESS_TOKEN,
                JSONMethods.createRequest("id==" + id)
                        .toString()).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        User user = new User();
                        user.setState("delete");
                        userLive.postValue(user);
                        logMessage(true, "Удаление пользователя",
                                response.body().toString(), response.code());
                    } else {
                        userLive.postValue(new User());
                        logMessage(false, "Удаление пользователя",
                                response.body().toString(), response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Удаление пользователя", "запрос не прошел || " +
                        t.toString(), 0);
                userLive.postValue(new User());
            }
        });
    }

    public void changeUserServers(String userID, ArrayList<String> serversID){
        App.getInstance().getApi().changeUserServers("Bearer " + ACCESS_TOKEN,
                JSONMethods.createRequestArray("user_id." + userID, "servers", serversID)
                        .toString().replace("\\", "").replace("\"[", "[")
                        .replace("]\"", "]")).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        User user = new User();
                        user.setState("accepted");
                        userLive.postValue(user);
                        logMessage(true, "Изменение серверов пользователя",
                                response.body().toString(), response.code());
                    } else {
                        userLive.postValue(new User());
                        logMessage(false, "Изменение серверов пользователя",
                                response.body().toString(), response.code());
                    }
                } else {
                    userLive.postValue(new User());
                    logMessage(false, "Изменение серверов пользователя",
                            response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Изменение серверов пользователя", "запрос не прошел || " +
                        t.toString(), 0);
                userLive.postValue(new User());
            }
        });
    }

    public void getUserServers(String userID){
        App.getInstance().getApi().getUserServers("Bearer " + ACCESS_TOKEN, userID).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().getStatus().equals("ok")){
                        ArrayList<Server> servers = new ArrayList<>();
                        if (response.body().getMessage().containsKey("servers")){
                            servers = ValueConverter.convertValueToServers(response.body().
                                    getMessage().get("servers"));
                        }
                        userServersLive.postValue(servers);
                        logMessage(true, "Сервера пользователя",
                                response.body().toString(), response.code());
                    } else {
                        userServersLive.postValue(new ArrayList<Server>());
                        logMessage(false, "Сервера пользователя",
                                response.body().toString(), response.code());
                    }
                } else {
                    userServersLive.postValue(new ArrayList<Server>());
                    logMessage(false, "Сервера пользователя",
                            response.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                logMessage(false, "Сервера пользователя", "запрос не прошел || " +
                        t.toString(), 0);
                userServersLive.postValue(new ArrayList<Server>());
            }
        });
    }

    //Others

    public LiveData<User> getSignState(){return signLive;}
    public LiveData<ArrayList<Server>> getServersState() {return serversLive;}
    public LiveData<ArrayList<Server>> getUserServersState() {return userServersLive;}
    public LiveData<ArrayList<User>> getUsersState() {return usersLive;}
    public LiveData<Map<String, Server>> getServerState() {return serverLive;}
    public LiveData<Map<CompoundButton, Server>> getControlState() {return commandLive;}
    public LiveData<User> getUserState() {return userLive;}

    private void logMessage(@NotNull Boolean isOk, String method, String message, Integer code){
        if (isOk) {
            Log.d(TAG, method + " || " + message);
        } else {
            Log.e(TAG, method + " || " + code + " || " + message);
            FirebaseCrashlytics.getInstance().log(method + " || " + code + " || " + message);
        }
    }

    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    public static String getRefreshToken() {
        return REFRESH_TOKEN;
    }

    public static User getUser() {
        return user;
    }

    public static ArrayList<Server> getAllServers() {
        return allServers;
    }

    public static ArrayList<Server> getUserServers() {
        return userServers;
    }

    public static void clear(){
        instance = new ApiCall();
    }
}
