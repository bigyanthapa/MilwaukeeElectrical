package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 11/11/2014.
 */
public class MTLogInRequest {
    @SerializedName("grant_type")
    public String grantType;

    @SerializedName("username")
    public String userName;

    @SerializedName("password")
    public String password;
}
