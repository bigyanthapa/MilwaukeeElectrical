package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cent146 on 12/10/14.
 */
public class MTUserManufacturerRequest {

    @SerializedName("manufacturerName")
    private String manufacturerName;

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
}
