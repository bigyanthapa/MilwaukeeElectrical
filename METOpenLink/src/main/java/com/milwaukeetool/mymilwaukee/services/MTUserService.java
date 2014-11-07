package com.milwaukeetool.mymilwaukee.services;

import com.google.gson.Gson;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.model.request.MTUserRegistrationRequest;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by cent146 on 11/5/14.
 */
public interface MTUserService {

    Converter DATA_CONVERTER = new GsonConverter(new Gson());
    String SERVICE_ENDPOINT = MTConfig.getWebServicesBaseURL();

    @POST("/accounts")
    void registerUser(@Body MTUserRegistrationRequest request, Callback<Response> callback);

//    @GET("/api/v1/user")
//    void logIn(@Query("name") String name, Callback callback);
//
//    @POST("/api/v1/user/{id}/posts")
//    void addPost(
//            @Path("id") int id,
//            @Body UserPostRequestBody request,
//            Callback callback);
//
//    @DELETE("/api/v1/user/{id}")
//    void deleteUser(@Path("id") int id, Callback callback);

}
