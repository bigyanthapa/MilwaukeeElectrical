package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.request.MTUserCategoryRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTUserCategoryResponse;

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
 * Created by scott.hopfensperger on 12/17/2014.
 */
public interface MTUserCategoryService {
    @GET("/accounts/me/categories")
    void  getCategories(@Header("Authorization") String authorization,
                        @Query("includeUncategorized") boolean includeUncategorized,
                        Callback<MTUserCategoryResponse> callback);

    @POST("/accounts/me/categories")
    void addCategory(@Header("Authorization") String authorization,
                         @Body MTUserCategoryRequest request,
                         Callback<Response> callback);

    @GET("/accounts/me/categories/{categoryId}")
    void getCategory(@Header("Authorization") String authorization,
                         @Path("categoryId") int categoryId,
                         Callback<MTCategory> callback);

    @PUT("/accounts/me/categories/{categoryId}")
    void updateCategory(@Header("Authorization") String authorization,
                            @Path("categoryId") int categoryId,
                            @Body MTUserCategoryRequest request,
                            Callback<Response> callback);

    @DELETE("/accounts/me/categories/{categoryId}")
    void deleteCategory(@Header("Authorization") String authorization,
                            @Path("categoryId") int categoryId,
                            Callback<Response> callback);


}
