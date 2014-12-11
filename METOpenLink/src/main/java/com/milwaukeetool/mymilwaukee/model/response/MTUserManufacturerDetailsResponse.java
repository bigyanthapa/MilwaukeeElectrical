package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;

import java.util.ArrayList;

/**
 * Created by cent146 on 12/10/14.
 */
public class MTUserManufacturerDetailsResponse {

    @SerializedName("items")
    public ArrayList<MTManufacturer> items;

    public ArrayList<MTManufacturer> getItems() {
        return items;
    }

    public void setItems(ArrayList<MTManufacturer> items) {
        this.items = items;
    }
}
