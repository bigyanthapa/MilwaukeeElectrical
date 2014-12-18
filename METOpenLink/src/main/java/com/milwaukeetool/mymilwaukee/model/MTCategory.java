package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTCategory {
    @SerializedName("categoryId")
    private Integer id;

    @SerializedName("categoryName")
    private String name;

    @SerializedName("itemCount")
    private Integer itemCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }
}
