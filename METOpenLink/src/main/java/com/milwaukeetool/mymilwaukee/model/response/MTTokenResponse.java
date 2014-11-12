package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 11/12/2014.
 */
public class MTTokenResponse {
    @SerializedName("access_token")
    public String token;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("expiresIn")
    public Integer expires;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }
}
