package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.model.response.MTUserManufacturerDetailsResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

/**
 * Created by cent146 on 12/10/14.
 */
public interface MTUserManufacturerService {

    /*

API	Description

POST api/v1/accounts/me/manufacturers
Create a new item manufacturer.

GET api/v1/accounts/me/manufacturers/{manufacturerId}
Get an item manufacturer.

PUT api/v1/accounts/me/manufacturers/{manufacturerId}
Edit an existing item manufacturer.

DELETE api/v1/accounts/me/manufacturers/{manufacturerId}
Delete an item manufacturer.

     */

    @GET("/accounts/me/manufacturers")
    void  getManufacturers(@Header("Authorization") String authorization,
                   @Query("includeGlobalManufacturers") boolean includeGlobalManufacturers,
                   Callback<MTUserManufacturerDetailsResponse> callback);

//    @POST("/accounts/me/manufacturers")
//    void
}
