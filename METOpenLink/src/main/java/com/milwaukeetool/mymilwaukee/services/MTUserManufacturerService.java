package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.model.MTManufacturer;
import com.milwaukeetool.mymilwaukee.model.request.MTUserManufacturerRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTUserManufacturerDetailsResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by cent146 on 12/10/14.
 */
public interface MTUserManufacturerService {

    @GET("/accounts/me/manufacturers")
    void  getManufacturers(@Header("Authorization") String authorization,
                   @Query("includeGlobalManufacturers") boolean includeGlobalManufacturers,
                   Callback<MTUserManufacturerDetailsResponse> callback);

    @POST("/accounts/me/manufacturers")
    void addManufacturer(@Header("Authorization") String authorization,
                         @Body MTUserManufacturerRequest request,
                         Callback<Response> callback);

    @PUT("/accounts/me/manufacturers/{manufacturerId}")
    void updateManufacturer(@Header("Authorization") String authorization,
                            @Path("manufacturerId") int manufacturerId,
                            @Body MTUserManufacturerRequest request,
                            Callback<Response> callback);

    @DELETE("/accounts/me/manufacturers/{manufacturerId}")
    void deleteManufacturer(@Header("Authorization") String authorization,
                            @Path("manufacturerId") int manufacturerId,
                            Callback<Response> callback);

    @GET("/accounts/me/manufacturers/{manufacturerId}")
    void getManufacturer(@Header("Authorization") String authorization,
                                   @Path("manufacturerId") int manufacturerId,
                                   Callback<MTManufacturer> callback);

}
