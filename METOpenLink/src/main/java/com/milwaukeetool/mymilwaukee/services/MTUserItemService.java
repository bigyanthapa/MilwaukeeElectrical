package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.model.request.MTItemDetailRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by cent146 on 12/10/14.
 */
public interface MTUserItemService {

    @POST("/accounts/me/items")
    void addItem(@Header("Authorization") String authorization, @Body MTItemDetailRequest request, Callback<Response> callback);

    @GET("/accounts/me/items")
    void getItems(@Header("Authorization") String authorization, Callback<MTUserItemResponse> callback);

}
