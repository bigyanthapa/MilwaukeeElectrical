package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cent146 on 11/13/14.
 */
public class MTErrorResponse {

    @SerializedName("error")
    public String error;

    @SerializedName("error_description")
    public String errorDescription;
}
