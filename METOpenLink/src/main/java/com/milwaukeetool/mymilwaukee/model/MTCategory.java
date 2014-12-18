package com.milwaukeetool.mymilwaukee.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTCategory implements Parcelable {
    @SerializedName("categoryId")
    private Integer categoryId;

    @SerializedName("categoryName")
    private String name;

    @SerializedName("itemCount")
    private Integer itemCount;

    public Integer getId() {
        return categoryId;
    }

    public void setId(Integer id) {
        this.categoryId = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(new Gson().toJson(this));
    }

    public static final Creator<MTCategory> CREATOR = new Creator<MTCategory>() {
        public MTCategory createFromParcel(Parcel in) {
            return new MTCategory(in);
        }
        public MTCategory[] newArray(int size) {
            return new MTCategory[size];
        }
    };

    private MTCategory(Parcel in) {
        String serializedJson = in.readString();
        Gson gson = new Gson();
        MTCategory tempResult = gson.fromJson(serializedJson, MTCategory.class);
        if (tempResult != null) {
            this.categoryId = tempResult.categoryId;
            this.name = tempResult.name;
            this.itemCount = tempResult.itemCount;
        }
    }
}
