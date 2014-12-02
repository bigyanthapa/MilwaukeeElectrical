package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cent146 on 12/2/14.
 */
public class MTItemSearchRequest {

    @SerializedName("SearchTerm")
    private String searchTerm;

    @SerializedName("Skip")
    private String skipCount;

    @SerializedName("Take")
    private String takeCount;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(String skipCount) {
        this.skipCount = skipCount;
    }

    public String getTakeCount() {
        return takeCount;
    }

    public void setTakeCount(String takeCount) {
        this.takeCount = takeCount;
    }
}
