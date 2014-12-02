package com.milwaukeetool.mymilwaukee.model.response;

import com.google.gson.annotations.SerializedName;
import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;

import java.util.ArrayList;

/**
 * Created by cent146 on 12/2/14.
 */
public class MTItemSearchResponse {

    @SerializedName("items")
    private ArrayList<MTItemSearchResult> itemSearchResults;

    public ArrayList<MTItemSearchResult> getItemSearchResults() {
        return itemSearchResults;
    }

    public void setItemSearchResults(ArrayList<MTItemSearchResult> itemSearchResults) {
        this.itemSearchResults = itemSearchResults;
    }

}
