package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.model.MTUserProfile;
import com.milwaukeetool.mymilwaukee.model.request.MTPasswordRequest;
import com.milwaukeetool.mymilwaukee.model.request.MTUserRegistrationRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTLogInResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by cent146 on 11/5/14.
 */
public interface MTUserService {

    @POST("/accounts")
    void registerUser(@Body MTUserRegistrationRequest request, Callback<Response> callback);

    @FormUrlEncoded
    @POST("/accounts/login")
    void login(@Header("Authorization") String authorization,
               @Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType,
               Callback<MTLogInResponse> callback);

    @GET("/accounts/me")
    void  getProfile(@Header("Authorization") String authorization, Callback<MTUserProfile> callback);

    @PUT("/accounts/me")
    void updateProfile(@Header("Authorization") String authorization, @Body MTUserProfile userProfile, Callback<MTUserProfile> callback);

    @PUT("/accounts/me/password")
    void updatePassword(@Header("Authorization") String authorization, @Body MTPasswordRequest passwordRequest, Callback<Response> callback);

}
