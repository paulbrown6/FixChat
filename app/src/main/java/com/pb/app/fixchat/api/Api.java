package com.pb.app.fixchat.api;

import com.pb.app.fixchat.api.entity.AuthorisationEntity;
import com.pb.app.fixchat.api.entity.ServerEntity;
import com.pb.app.fixchat.api.entity.ResponseEntity;
import com.pb.app.fixchat.api.entity.ServersEntity;
import com.pb.app.fixchat.api.entity.UserEntity;
import com.pb.app.fixchat.api.entity.UsersEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    //Authorisation

    @Headers("Content-Type: application/json")
    @POST ("signin")
    Call<AuthorisationEntity> authorisation(@Body String body);

    @GET ("refresh")
    Call<AuthorisationEntity> refresh(@Header("Authorization") String refToken);

    //Admin

    //Servers

    @GET ("admin/servers")
    Call<ServersEntity> getAllServers(@Header("Authorization") String token);

    @GET ("admin/servers/{id}")
    Call<ServerEntity> getServer(@Header("Authorization") String token, @Path("id") String id);

    @GET ("admin/servers/{id}/users")
    Call<UsersEntity> getServerUsers(@Header("Authorization") String token, @Path("id") String id);

    @POST("admin/server/create")
    Call<ResponseEntity> createServer(@Header("Authorization") String token, @Query("id") String id,
                                      @Query("name") String name, @Query("hv") String hv);

    @POST("admin/server/edit")
    Call<ResponseEntity> editServer(@Header("Authorization") String token, @Query("id") String id,
                                    @Query("name") String name, @Query("hv") String hv);

    @POST("admin/server/delete")
    Call<ResponseEntity> deleteServer(@Header("Authorization") String token, @Query("id") String id);

    //Users

    @GET("admin/users")
    Call<UsersEntity> getAllUsers(@Header("Authorization") String token);

    @GET("admin/user/{id}")
    Call<UserEntity> getUser(@Header("Authorization") String token, @Path("id") String id);

    @GET("admin/user/{id}/servers")
    Call<ServersEntity> getUserServers(@Header("Authorization") String token, @Path("id") String id);

    @POST("admin/user/create")
    Call<ResponseEntity> createUser(@Header("Authorization") String token, @Query("name") String name,
                                    @Query("email") String email, @Query("password") String password,
                                    @Query("role") Integer role);

    @POST("admin/user/edit")
    Call<ResponseEntity> editUser(@Header("Authorization") String token, @Query("id") String id,
                                  @Query("name") String name, @Query("email") String email,
                                  @Query("role") Integer role);

    @POST("admin/user/delete")
    Call<ResponseEntity> deleteUser(@Header("Authorization") String token, @Query("id") String id);

    @POST("admin/user/addserver")
    Call<ResponseEntity> addServerToUser(@Header("Authorization") String token, @Query("user_id") String idUser,
                                         @Query("server_id") String idServer);

    @POST("admin/user/delserver")
    Call<ResponseEntity> deleteServerToUser(@Header("Authorization") String token, @Query("user_id") String idUser,
                                            @Query("server_id") String idServer);

    //User

    @GET ("user/servers")
    Call<ServersEntity> getUserServers(@Header("Authorization") String token);

    @POST ("user/server/{id}/power-start")
    Call<ResponseEntity> powerStart(@Header("Authorization") String token, @Path("id") String id);

    @POST ("user/server/{id}/power-stop")
    Call<ResponseEntity> powerStop(@Header("Authorization") String token, @Path("id") String id, @Query("force") Boolean force);

    @POST ("user/server/{id}/network-start")
    Call<ResponseEntity> networkStart(@Header("Authorization") String token, @Path("id") String id);

    @POST ("user/server/{id}/network-stop")
    Call<ResponseEntity> networkStop(@Header("Authorization") String token, @Path("id") String id);
}
