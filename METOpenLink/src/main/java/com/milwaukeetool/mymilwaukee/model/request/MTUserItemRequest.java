package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cent146 on 12/18/14.
 */
public class MTUserItemRequest {
    @SerializedName("SearchTerm")
    private String searchTerm;

    @SerializedName("Skip")
    private int skipCount;

    @SerializedName("Take")
    private int takeCount;

    @SerializedName("CategoryId")
    private int categoryId;

    @SerializedName("ManufacturerId")
    private int manufacturerId;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getTakeCount() {
        return takeCount;
    }

    public void setTakeCount(int takeCount) {
        this.takeCount = takeCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Map<String, String> getAsQueryParameters() {
        Map<String, String> map = new HashMap<String,String>();
        map.put("Skip", Integer.toString(getSkipCount()));
        map.put("Take", Integer.toString(getTakeCount()));
        if (getSearchTerm() != null) {
            map.put("SearchTerm", getSearchTerm());
        }
        if (getManufacturerId() != -1) {
            map.put("ManufacturerId", Integer.toString(getManufacturerId()));
        }
        if (getCategoryId() != -1) {
            map.put("CategoryId", Integer.toString(getCategoryId()));
        }
        return map;
    }
}