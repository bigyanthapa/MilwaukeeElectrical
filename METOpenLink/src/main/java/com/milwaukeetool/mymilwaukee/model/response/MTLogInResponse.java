package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 11/12/2014.
 */
public class MTLogInResponse {
    @SerializedName("access_token")
    public String token;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("expiresIn")
    public Integer expiration;
}
