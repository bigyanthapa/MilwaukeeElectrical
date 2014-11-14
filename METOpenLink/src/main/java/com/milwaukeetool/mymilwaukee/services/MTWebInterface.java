package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.model.response.MTCreateAccountErrorResponse;
import com.milwaukeetool.mymilwaukee.model.response.MTErrorResponse;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/6/14.
 */
public class MTWebInterface {

    private static final String TAG = makeLogTag(MTWebInterface.class);

    private static MTWebInterface instance;

    private RestAdapter mRestAdapter = null;

    private MTUserService mUserService = null;

    //private ArrayList<Class> mWebServices = new ArrayList<>();

    // MTWebInterface prevents any other class from instantiating
    private MTWebInterface() {
        mRestAdapter = new RestAdapter.Builder()
                .setConverter(MTUserService.DATA_CONVERTER)
                .setEndpoint(MTUserService.SERVICE_ENDPOINT)
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

    public static String getCreateAccountErrorMessage(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            if (isValidJSON(retrofitError.getResponse().getBody().toString())) {
                MTCreateAccountErrorResponse body = (MTCreateAccountErrorResponse) retrofitError.getBodyAs(MTCreateAccountErrorResponse.class);
                return body.errorMessage;
            } else {
                return "Unknown Server Error";
            }
        }
        return "Unknown Error";
    }

    public static String getErrorMessage(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            if (isValidJSON(retrofitError.getResponse().getBody().toString())) {
                MTErrorResponse body = (MTErrorResponse) retrofitError.getBodyAs(MTErrorResponse.class);
                return body.errorDescription;
            } else {
                return "Unknown Server Error";
            }
        }
        return "Unknown Error";
    }

    private static boolean isValidJSON(String responseString) {
//        try {
//            new JSONObject(responseString);
//        } catch (JSONException ex) {
//            try {
//                new JSONArray(responseString);
//            } catch (JSONException ex1) {
//                return false;
//            }
//        }
        return true;
    }
}
