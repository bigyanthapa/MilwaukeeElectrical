package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.model.event.MTSearchResultEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTItemSearchRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTItemSearchResponse;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/3/14.
 */
public class MTInventoryHelper {

    public static final int INVENTORY_REQUEST_BUFFER = 3;
    public static final int INVENTORY_ITEM_REQUEST_COUNT = 10;
    public static final int INVENTORY_INITIAL_SKIP_INDEX = 0;

    private static final String TAG = makeLogTag(MTInventoryHelper.class);

    private int mCurrentPosition = INVENTORY_INITIAL_SKIP_INDEX + INVENTORY_ITEM_REQUEST_COUNT;
    private boolean mIsEndOfResults = false;

    private static MTInventoryHelper instance;

    // MTInventoryHelper prevents any other class from instantiating
    private MTInventoryHelper() {

    }

    // Providing Global point of access
    public static MTInventoryHelper sharedInstance() {

        if (null == instance) {
            instance = new MTInventoryHelper();
        }
        return instance;
    }

    public void searchForResults(final String searchTerm, final int skipCount, final boolean showProgress, final MTActivity activity) {

        Callback<MTItemSearchResponse> responseCallback = new Callback<MTItemSearchResponse>() {
            @Override
            public void success(MTItemSearchResponse result, Response response) {

                if (showProgress) {
                    activity.getProgressView().stopProgress();
                }

                LOGD(TAG, "Successfully retrieved search item results");

                MTSearchResultEvent event = new MTSearchResultEvent(activity,result.getItemSearchResults());
                event.setLastSearchResultCount(result.getItemSearchResults().size());
                event.setLastSearchTerm(searchTerm);
                event.setLastSkipCount(skipCount);
                EventBus.getDefault().post(event);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                if (showProgress) {
                    activity.getProgressView().stopProgress();
                }

                LOGD(TAG, "Failed to retrieve search item results");

                MTUtils.handleRetrofitError(retrofitError, activity,
                        MiscUtils.getString(R.string.add_item_search_error));

                EventBus.getDefault().post(new MTSearchResultEvent(activity, null));
            }
        };

        MTItemSearchRequest request = new MTItemSearchRequest();
        request.setSearchTerm(searchTerm);
        request.setSkipCount(skipCount);
        request.setTakeCount(INVENTORY_ITEM_REQUEST_COUNT);

        if (showProgress) {
            activity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_searching));
        }

        MTWebInterface.sharedInstance().getInventoryService().getItems(MTUtils.getAuthHeaderForBearerToken(),
                request.getAsQueryParameters(), responseCallback);
    }

}
