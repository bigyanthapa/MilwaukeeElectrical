package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cent146 on 11/5/14.
 */
public class MTUserRegistrationRequest {

    @SerializedName("password")
    public String userPassword;

    @SerializedName("confirmPassword")
    public String userConfirmPassword;

    @SerializedName("firstName")
    public String userFirstName;

    @SerializedName("lastName")
    public String userLastName;

    @SerializedName("email")
    public String userEmail;

    @SerializedName("occupation")
    public String userOccupation;

    @SerializedName("optInForCommunications")
    public boolean userOptIn;
}
