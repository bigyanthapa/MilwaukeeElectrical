package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 12/4/2014.
 */
public class MTItemDetailRequest {
    @SerializedName("manufacturerId")
    private Integer manufacturerId = -1;

    @SerializedName("categoryId")
    private Integer categoryId = -1;

    @SerializedName("modelNumber")
    private String modelNumber;

    @SerializedName("itemDescription")
    private String itemDescription;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("serialNumber")
    private String serialNumber;

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

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
}
