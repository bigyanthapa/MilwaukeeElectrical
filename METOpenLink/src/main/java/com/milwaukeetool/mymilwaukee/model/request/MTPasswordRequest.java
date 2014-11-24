package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scott.hopfensperger on 11/24/2014.
 */
public class MTPasswordRequest {
    @SerializedName("currentPassword")
    private String current;

    @SerializedName("updatedPassword")
    private String updated;

    @SerializedName("confirmPassword")
    private String confirm;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
