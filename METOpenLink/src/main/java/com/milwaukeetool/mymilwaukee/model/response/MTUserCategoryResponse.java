package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;
import com.milwaukeetool.mymilwaukee.model.MTCategory;

import java.util.List;

/**
 * Created by scott.hopfensperger on 12/17/2014.
 */
public class MTUserCategoryResponse {
    @SerializedName("items")
    private List<MTCategory> items;

    public List<MTCategory> getItems() {
        return items;
    }

    public void setItems(List<MTCategory> items) {
        this.items = items;
    }
}
