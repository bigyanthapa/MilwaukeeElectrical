package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTManufacturer {
    @SerializedName("manufacturerId")
    private Integer id;

    @SerializedName("isPrimary")
    private boolean primary;

    @SerializedName("manufacturerName")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
