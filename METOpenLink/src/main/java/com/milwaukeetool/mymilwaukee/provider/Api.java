package com.milwaukeetool.mymilwaukee.provider;

import com.milwaukeetool.mymilwaukee.model.request.MTUserRegistrationRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTTokenResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by scott.hopfensperger on 11/11/2014.
 */
public interface Api {
    @POST("/accounts")
    void createUserAccount(@Header("Authorization") String authorization, @Body MTUserRegistrationRequest request,
                           Callback<Response> callback);


    @FormUrlEncoded
    @POST("/accounts/login")
    void login(@Header("Authorization") String authorization,
               @Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType,
               Callback<MTTokenResponse> callback);
}
