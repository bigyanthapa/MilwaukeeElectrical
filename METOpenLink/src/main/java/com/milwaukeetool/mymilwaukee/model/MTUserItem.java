package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/9/2014.
 */
public class MTUserItem {

    @SerializedName("itemId")
    private Integer id;

    @SerializedName("category")
    private MTCategory category;

    @SerializedName("manufacturer")
    private MTManufacturer manufacturer;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("isBluetoothCapable")
    private boolean bluetoothEnabled;

    @SerializedName("serialNumerId")
    private Integer serialNumber;

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
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
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
}
