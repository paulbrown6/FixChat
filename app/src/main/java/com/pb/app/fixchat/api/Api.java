package com.pb.app.fixchat.api;

import com.pb.app.fixchat.api.entity.ResponseApi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    //Authorisation
    @Headers("Content-Type: application/json")
    @POST ("signin")
    Call<ResponseApi> signin(@Body String body);

    @POST ("refresh")
    Call<ResponseApi> refresh(@Body String body);

    //Servers
    @GET ("servers")
    Call<ResponseApi> getServers(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @PATCH ("servers")
    Call<ResponseApi> editServer(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @DELETE("servers")
    Call<ResponseApi> deleteServer(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("servers/control")
    Call<ResponseApi> controlServer(@Header("Authorization") String token, @Body String body);

    //Users
    @GET ("users")
    Call<ResponseApi> getUsers(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST ("users")
    Call<ResponseApi> createUser(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @PATCH ("users")
    Call<ResponseApi> editUser(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @DELETE("users")
    Call<ResponseApi> deleteUser(@Header("Authorization") String token, @Body String body);

    @Headers("Content-Type: application/json")
    @POST ("users/servers")
    Call<ResponseApi> changeUserServers(@Header("Authorization") String token, @Body String body);

    @GET ("users/{id}/servers")
    Call<ResponseApi> getUserServers(@Header("Authorization") String token, @Path("id") String id);
}
