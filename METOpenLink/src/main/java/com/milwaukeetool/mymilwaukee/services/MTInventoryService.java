package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.model.response.MTItemSearchResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.QueryMap;

/**
 * Created by cent146 on 12/2/14.
 */
public interface MTInventoryService {

    public static final int DEFAULT_SKIP_COUNT = 10;

    @GET("/items")
    void  getItems(@Header("Authorization") String authorization,
                     @QueryMap Map<String, String> searchParams,
                     Callback<MTItemSearchResponse> callback);

}
