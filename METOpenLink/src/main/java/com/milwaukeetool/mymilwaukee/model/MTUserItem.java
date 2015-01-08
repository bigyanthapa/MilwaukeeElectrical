package com.milwaukeetool.mymilwaukee.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTUserItem implements Parcelable {

    @SerializedName("itemId")
    private Integer itemId;

    @SerializedName("category")
    private MTCategory category;

    @SerializedName("manufacturer")
    private MTManufacturer manufacturer;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("isBluetoothCapable")
    private boolean bluetoothEnabled;

    @SerializedName("serialNumber")
    private String serialNumber;

    @SerializedName("serialNumberId")
    private Integer serialNumberId;

    @SerializedName("dateAdded")
    private String dateAdded;

    @SerializedName("modelNumber")
    private String modelNumber;

    @SerializedName("itemDescription")
    private String itemDescription;

    @SerializedName("customIdentifier")
    private String customIdentifier;

    @SerializedName("notes")
    private String notes;

    @SerializedName("purchaseLocation")
    private String purchaseLocation;

    @SerializedName("orderInformationImageUrl")
    private String orderInformationImageUrl;

    @SerializedName("itemizationImageUrl")
    private String itemizationImageUrl;

    public Integer getId() {
        return itemId;
    }

    public void setId(Integer id) {
        this.itemId = id;
    }

    public MTCategory getCategory() {
        return category;
    }

    public void setCategory(MTCategory category) {
        this.category = category;
    }

    public MTManufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(MTManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothEnabled;
    }

    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        this.bluetoothEnabled = bluetoothEnabled;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
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

    public String getCustomIdentifier() {
        return customIdentifier;
    }

    public void setCustomIdentifier(String customIdentifier) {
        this.customIdentifier = customIdentifier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPurchaseLocation() {
        return purchaseLocation;
    }

    public void setPurchaseLocation(String purchaseLocation) {
        this.purchaseLocation = purchaseLocation;
    }

    public String getOrderInformationImageUrl() {
        return orderInformationImageUrl;
    }

    public void setOrderInformationImageUrl(String orderInformationImageUrl) {
        this.orderInformationImageUrl = orderInformationImageUrl;
    }

    public String getItemizationImageUrl() {
        return itemizationImageUrl;
    }

    public void setItemizationImageUrl(String itemizationImageUrl) {
        this.itemizationImageUrl = itemizationImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSerialNumberId() {
        return serialNumberId;
    }

    public void setSerialNumberId(Integer serialNumberId) {
        this.serialNumberId = serialNumberId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(new Gson().toJson(this));
    }

    public static final Creator<MTUserItem> CREATOR = new Creator<MTUserItem>() {
        public MTUserItem createFromParcel(Parcel in) {
            return new MTUserItem(in);
        }
        public MTUserItem[] newArray(int size) {
            return new MTUserItem[size];
        }
    };

    private MTUserItem(Parcel in) {
        String serializedJson = in.readString();
        Gson gson = new Gson();
        MTUserItem tempResult = gson.fromJson(serializedJson, MTUserItem.class);
        if (tempResult != null) {
            this.itemId = tempResult.itemId;
            this.category = tempResult.category;
            this.manufacturer = tempResult.manufacturer;
            this.imageUrl = tempResult.imageUrl;
            this.bluetoothEnabled = tempResult.bluetoothEnabled;
            this.serialNumber = tempResult.serialNumber;
            this.serialNumberId = tempResult.serialNumberId;
            this.dateAdded = tempResult.dateAdded;
            this.modelNumber = tempResult.modelNumber;
            this.itemDescription = tempResult.itemDescription;
            this.customIdentifier = tempResult.customIdentifier;
            this.notes = tempResult.notes;
            this.purchaseLocation = tempResult.purchaseLocation;
            this.orderInformationImageUrl = tempResult.orderInformationImageUrl;
            this.itemizationImageUrl = tempResult.itemizationImageUrl;
        }
    }
}
