package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.config.MTConfig;
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

    public static String getErrorMessage(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            MTErrorResponse body = (MTErrorResponse) retrofitError.getBodyAs(MTErrorResponse.class);
            return body.errorMessage;
        }
        return "";
    }

}
