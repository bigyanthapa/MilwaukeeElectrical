package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cent146 on 12/2/14.
 */
public class MTItemSearchRequest {

    @SerializedName("SearchTerm")
    private String searchTerm;

    @SerializedName("Skip")
    private int skipCount;

    @SerializedName("Take")
    private int takeCount;

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

    public Map<String, String> getAsQueryParameters() {
        Map<String, String> map = new HashMap<String,String>();
        map.put("SearchTerm", getSearchTerm());
        map.put("Skip", Integer.toString(getSkipCount()));
        map.put("Take", Integer.toString(getTakeCount()));
        return map;
    }
}
