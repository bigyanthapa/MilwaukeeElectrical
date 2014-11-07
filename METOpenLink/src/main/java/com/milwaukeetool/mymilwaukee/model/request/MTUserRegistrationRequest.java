package com.milwaukeetool.mymilwaukee.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cent146 on 11/5/14.
 */
public class MTUserRegistrationRequest {
//public class MTUserRegistrationRequest implements Parcelable {

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

//    public int describeContents() {
//        return 0;
//    }
//
//    // write your object's data to the passed-in Parcel
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeString(new Gson().toJson(this));
//    }
//
//    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
//    public static final Parcelable.Creator<MTUserRegistrationRequest> CREATOR = new Parcelable.Creator<MTUserRegistrationRequest>() {
//        public MTUserRegistrationRequest createFromParcel(Parcel in) {
//            return new MTUserRegistrationRequest(in);
//        }
//
//        public MTUserRegistrationRequest[] newArray(int size) {
//            return new MTUserRegistrationRequest[size];
//        }
//    };
//
//    private MTUserRegistrationRequest(Parcel in) {
//        String serializedJson = in.readString();
//        Gson gson = new Gson();
//        MTUserRegistrationRequest request = gson.fromJson(serializedJson, MTUserRegistrationRequest.class);
//        if (request != null) {
//            this.userPassword = request.userPassword;
//            this.userConfirmPassword = request.userConfirmPassword;
//            this.userFirstName = request.userFirstName;
//            this.userLastName = request.userLastName;
//            this.userEmail = request.userEmail;
//            this.userOccupation = request.userOccupation;
//            this.userOptIn = request.userOptIn;
//        }
//    }
}
