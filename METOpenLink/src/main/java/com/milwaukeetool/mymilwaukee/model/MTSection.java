package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTSection {
    @SerializedName("sectionId")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("items")
    private List<MTUserItem> items;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MTUserItem> getItems() {
        return items;
    }

    public void setItem(List<MTUserItem> items) {
        this.items = items;
    }
}
