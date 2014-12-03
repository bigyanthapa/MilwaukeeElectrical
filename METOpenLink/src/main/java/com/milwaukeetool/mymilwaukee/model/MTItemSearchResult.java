package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cent146 on 12/2/14.
 */
public class MTItemSearchResult {

    @SerializedName("modelNumber")
    private String modelNumber;

    @SerializedName("itemDescription")
    private String itemDescription;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("children")
    private ArrayList<MTItemSearchResult> children;

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<MTItemSearchResult> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<MTItemSearchResult> children) {
        this.children = children;
    }
}
