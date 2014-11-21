package com.milwaukeetool.mymilwaukee.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 11/20/2014.
 */
public class MTUserProfile {

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String LastName;

    @SerializedName("occupation")
    private String occupation;

    @SerializedName("optInForCommunication")
    private boolean optInForCommunication;

    @SerializedName("title")
    private String title;

    @SerializedName("companyName")
    private String companyName;

    @SerializedName("address")
    private String address;

    @SerializedName("address2")
    private String address2;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip")
    private String zip;

    @SerializedName("country")
    private String country;

    @SerializedName("phone")
    private String phone;

    @SerializedName("cellPhone")
    private String cellPhone;

    @SerializedName("fax")
    private String fax;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public boolean isOptInForCommunication() {
        return optInForCommunication;
    }

    public void setOptInForCommunication(boolean optInForCommunication) {
        this.optInForCommunication = optInForCommunication;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
