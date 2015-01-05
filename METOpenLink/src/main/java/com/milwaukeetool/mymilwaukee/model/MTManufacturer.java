package com.milwaukeetool.mymilwaukee.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTManufacturer implements Parcelable {
    @SerializedName("manufacturerId")
    private Integer manufacturerId;

    @SerializedName("isPrimary")
    private boolean primary;

    @SerializedName("manufacturerName")
    private String manufacturerName;

    @SerializedName("itemCount")
    private int itemCount;

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

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(new Gson().toJson(this));
    }

    public static final Creator<MTManufacturer> CREATOR = new Creator<MTManufacturer>() {
        public MTManufacturer createFromParcel(Parcel in) {
            return new MTManufacturer(in);
        }
        public MTManufacturer[] newArray(int size) {
            return new MTManufacturer[size];
        }
    };

    private MTManufacturer(Parcel in) {
        String serializedJson = in.readString();
        Gson gson = new Gson();
        MTManufacturer tempResult = gson.fromJson(serializedJson, MTManufacturer.class);
        if (tempResult != null) {
            this.manufacturerId = tempResult.manufacturerId;
            this.primary = tempResult.primary;
            this.manufacturerName = tempResult.manufacturerName;
            this.itemCount = tempResult.itemCount;

        }
    }
}
