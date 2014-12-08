package com.milwaukeetool.mymilwaukee.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cent146 on 12/2/14.
 */
public class MTItemSearchResult implements Parcelable {

    @SerializedName("modelNumber")
    private String modelNumber;

    @SerializedName("itemDescription")
    private String itemDescription;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("children")
    private ArrayList<MTItemSearchResult> children;

    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(new Gson().toJson(this));
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<MTItemSearchResult> CREATOR = new Creator<MTItemSearchResult>() {
        public MTItemSearchResult createFromParcel(Parcel in) {
            return new MTItemSearchResult(in);
        }

        public MTItemSearchResult[] newArray(int size) {
            return new MTItemSearchResult[size];
        }
    };

    private MTItemSearchResult(Parcel in) {
        String serializedJson = in.readString();
        Gson gson = new Gson();
        MTItemSearchResult tempResult = gson.fromJson(serializedJson, MTItemSearchResult.class);
        if (tempResult != null) {
            this.modelNumber = tempResult.modelNumber;
            this.itemDescription = tempResult.itemDescription;
            this.imageUrl = tempResult.imageUrl;
            this.itemDescription = tempResult.itemDescription;
        }
    }

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
