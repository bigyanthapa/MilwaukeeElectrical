package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTSection {
    @SerializedName("sectionId")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("items")
    private ArrayList<MTUserItem> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<MTUserItem> getItems() {
        return items;
    }

    public void setItem(ArrayList<MTUserItem> items) {
        this.items = items;
    }
}
