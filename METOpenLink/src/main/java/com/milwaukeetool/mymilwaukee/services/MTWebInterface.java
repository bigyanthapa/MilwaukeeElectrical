package com.milwaukeetool.mymilwaukee.services;

import com.google.gson.Gson;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.model.response.MTCreateAccountErrorResponse;
import com.milwaukeetool.mymilwaukee.model.response.MTErrorResponse;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/6/14.
 */
public class MTWebInterface {

    private static final String TAG = makeLogTag(MTWebInterface.class);

    private static MTWebInterface instance;

    private RestAdapter mRestAdapter = null;

    private MTUserService mUserService = null;

    private MTInventoryService mInventoryService = null;

    private MTUserItemService mUserItemService = null;

    private MTUserManufacturerService mUserManufacturerService = null;

    private MTUserCategoryService mtUserCategoryService;

    Converter DATA_CONVERTER = new GsonConverter(new Gson());
    String SERVICE_ENDPOINT = MTConfig.getWebServicesBaseURL();

    //private ArrayList<Class> mWebServices = new ArrayList<>();

    // MTWebInterface prevents any other class from instantiating
    private MTWebInterface() {
        mRestAdapter = new RestAdapter.Builder()
                .setConverter(DATA_CONVERTER)
                .setEndpoint(SERVICE_ENDPOINT)
                .setLogLevel(MTConfig.isExternalRelease() ? RestAdapter.LogLevel.NONE : RestAdapter.LogLevel.FULL)
                .build();
    }

    // Providing Global point of access
    public static MTWebInterface sharedInstance() {

        if (null == instance) {
            instance = new MTWebInterface();
        }
        return instance;
    }

    public MTUserService getUserService() {

        if (mUserService == null) {
            // Create the user service
            mUserService = mRestAdapter.create(MTUserService.class);
        }
        return mUserService;
    }

    public MTInventoryService getInventoryService() {

        if (mInventoryService == null) {
            // Create the user service
            mInventoryService = mRestAdapter.create(MTInventoryService.class);
        }
        return mInventoryService;
    }

    public MTUserItemService getUserItemService() {

        if (mUserItemService == null) {
            // Create the user service
            mUserItemService = mRestAdapter.create(MTUserItemService.class);
        }
        return mUserItemService;
    }

    public MTUserManufacturerService getUserManufacturerService() {

        if (mUserManufacturerService == null) {
            // Create the user service
            mUserManufacturerService = mRestAdapter.create(MTUserManufacturerService.class);
        }
        return mUserManufacturerService;
    }

    public MTUserCategoryService getUserCategoryService() {
        if (this.mtUserCategoryService == null) {
            // Create the user service
            this.mtUserCategoryService = mRestAdapter.create(MTUserCategoryService.class);
        }
        return this.mtUserCategoryService;
    }

    public static String getErrorMessage(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            if (isJSONValid((TypedByteArray)retrofitError.getResponse().getBody())) {
                MTCreateAccountErrorResponse body = (MTCreateAccountErrorResponse) retrofitError.getBodyAs(MTCreateAccountErrorResponse.class);
                return body.errorMessage;
            } else {
                return "Unknown Server Error";
            }
        }
        return "Unknown Error";
    }

    public static String getLogInErrorMessage(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            if (isJSONValid((TypedByteArray)retrofitError.getResponse().getBody())) {
                MTErrorResponse body = (MTErrorResponse) retrofitError.getBodyAs(MTErrorResponse.class);
                return body.errorDescription;
            } else {
                return "Unknown Server Error";
            }
        }
        return "Unknown Error";
    }

    private static final Gson gson = new Gson();

    public static boolean isJSONValid(TypedByteArray byteArray) {
        String json =  new String(byteArray.getBytes());
        try {
            gson.fromJson(json, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }
}
