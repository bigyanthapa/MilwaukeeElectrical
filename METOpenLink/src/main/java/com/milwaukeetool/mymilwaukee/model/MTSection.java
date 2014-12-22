package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTSection {
    @SerializedName("sectionId")
    private String sectionId;

    @SerializedName("title")
    private String title;

    @SerializedName("items")
    private ArrayList<MTUserItem> items;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String id) {
        this.sectionId = id;
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

    public void setItems(ArrayList<MTUserItem> items) {
        this.items = items;
    }

    public boolean containsItems() {
        if (this.items != null && !this.items.isEmpty()) {
            return true;
        }
        return false;
    }

    public void addItems(ArrayList<MTUserItem> items) {
        this.items.addAll(items);
    }

    public int getItemCount() {
        if (containsItems()) {
            return getItems().size();
        }
        return 0;
    }
}
