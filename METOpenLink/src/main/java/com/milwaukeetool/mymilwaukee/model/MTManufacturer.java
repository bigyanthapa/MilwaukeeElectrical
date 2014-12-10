package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTManufacturer {
    @SerializedName("manufacturerId")
    private Integer manufacturerId;

    @SerializedName("isPrimary")
    private boolean primary;

    @SerializedName("manufacturerName")
    private String manufacturerName;

    @SerializedName("itemCount")
    private String itemCount;

    public Integer getId() {
        return manufacturerId;
    }

    public void setId(Integer id) {
        manufacturerId = id;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getName() {
        return manufacturerName;
    }

    public void setName(String name) {
        this.manufacturerName = name;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}
