package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemDetailActivity;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.model.MTSection;
import com.milwaukeetool.mymilwaukee.model.MTUserItem;
import com.milwaukeetool.mymilwaukee.model.event.MTAddItemEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTChangeInventoryEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTSearchResultEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTUserItemResultEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTUserItemRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NamedObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/17/14.
 */
public class MTUserItemManager {

    public enum UserItemFilterType {
        FILTER_TYPE_NONE, FILTER_TYPE_MANUFACTURER, FILTER_TYPE_CATEGORY, FILTER_TYPE_ALL
    }

    public static final int USER_ITEM_REQUEST_COUNT = 10;
    public static final int USER_ITEM_INITIAL_SKIP_INDEX = 0;
    public static final int USER_ITEM_BUFFER_SIZE = 3;

    private static final String TAG = makeLogTag(MTUserItemManager.class);

    public static String NAMED_OBJECT_USER_ITEM = "MTUserItem";
    public static String NAMED_OBJECT_SECTION = "MTSection";

    private boolean mIsEndOfResults = false;
    private boolean mIsDirty = true;

    private ArrayList<MTSection> mInventorySections;

//    private MTUserItemResponse mLastResponse;

    public MTUserItemManager() {
        mInventorySections = new ArrayList<>();
        mIsEndOfResults = true;

        EventBus.getDefault().register(this);
//        mLastResponse = null;
    }

    public ArrayList<MTSection> getInventorySections() {
        return mInventorySections;
    }

    public final boolean isDirty() {
        return mIsDirty;
    }

//    public MTUserItemResponse getLastResponse() {
//        return mLastResponse;
//    }
//
//    public void setLastResponse(MTUserItemResponse mLastResponse) {
//        this.mLastResponse = mLastResponse;
//    }

    public boolean isEndOfResults() {
        return mIsEndOfResults;
    }

    public int getTotalNumberOfItems() {
        int itemTotal = 0;
        for (MTSection currentSection : mInventorySections) {
            if (currentSection != null && currentSection.getItems() != null) {
                itemTotal += currentSection.getItemCount();
            }
        }
        return itemTotal;
    }

    public void getItems(final MTActivity activity, final int skipCount, final boolean showProgress) {
        getItems(activity,skipCount,showProgress,-1,-1,null);
    }

    public void getItems(final MTActivity activity, final int skipCount, final boolean showProgress, final int filterId, final UserItemFilterType filterType) {

        switch (filterType) {
            case FILTER_TYPE_CATEGORY:
                getItems(activity, skipCount, showProgress, -1, filterId, null);
                break;
            case FILTER_TYPE_MANUFACTURER:
                getItems(activity, skipCount, showProgress, filterId, -1, null);
                break;
            case FILTER_TYPE_NONE:
            default:
                getItems(activity, skipCount, showProgress, -1, -1, null);
                break;
        }
    }

    public void getItems(final MTActivity activity, final int skipCount, final boolean showProgress, final int manufacturerId, final int categoryId, final String searchTerm) {

        if (skipCount == 0) {
            mInventorySections.clear();
            mIsDirty = false;
        }

        Callback<MTUserItemResponse> responseCallback = new Callback<MTUserItemResponse>() {
            @Override
            public void success(MTUserItemResponse result, Response response) {

//                mLastResponse = result;

                if (showProgress && activity != null) {
                    activity.getProgressView().stopProgress();
                }

                LOGD(TAG, "Successfully retrieved search item results");

                mInventorySections = MTUserItemManager.sectionsWithAppendedResponse(mInventorySections, result);

                MTUserItemResultEvent event = new MTUserItemResultEvent(activity,result);
                event.setLastSearchTerm(searchTerm);
                event.setLastSkipCount(skipCount);
                event.setLastCategoryId(categoryId);
                event.setLastManufacturerId(manufacturerId);
                event.setUserItemResponse(result);

                if (event.getLastResultCount() < USER_ITEM_REQUEST_COUNT) {
                    mIsEndOfResults = true;
                } else {
                    mIsEndOfResults = false;
                }

                EventBus.getDefault().post(event);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

//                mLastResponse = null;

                if (showProgress && activity != null) {
                    activity.getProgressView().stopProgress();
                }

                LOGD(TAG, "Failed to retrieve search item results");

                MTUtils.handleRetrofitError(retrofitError, activity,
                        MiscUtils.getString(R.string.add_item_search_error));

                MTSearchResultEvent event = new MTSearchResultEvent(activity, null);
                event.isSuccessful = false;
                EventBus.getDefault().post(event);
            }
        };

        LOGD(TAG, "Skip: " + skipCount + " Take: " + USER_ITEM_REQUEST_COUNT);

        MTUserItemRequest request = new MTUserItemRequest();
        request.setSearchTerm(searchTerm);
        request.setSkipCount(skipCount);
        request.setTakeCount(USER_ITEM_REQUEST_COUNT);
        if (categoryId != -1) {
            request.setCategoryId(categoryId);
        }
        if (manufacturerId != -1) {
            request.setManufacturerId(manufacturerId);
        }

        if (showProgress && activity != null) {
            activity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_default_message));
        }

        MTWebInterface.sharedInstance().getUserItemService().getItems(MTUtils.getAuthHeaderForBearerToken(),
                request.getAsQueryParameters(), responseCallback);
    }

    public static ArrayList<NamedObject> getAllListItemsForResponse(ArrayList<MTSection> sections) {

        ArrayList<NamedObject> listItems = new ArrayList<>();

        int currentIndex = 0;

        if (sections != null) {
            for(MTSection section : sections) {
                if (section != null && section.getItems() != null && section.getItems().size() > 0) {

                    LOGD(TAG, "Adding item at index: " + currentIndex + " for type: " + NAMED_OBJECT_SECTION);
                    listItems.add(new NamedObject(NAMED_OBJECT_SECTION, section));
                    ++currentIndex;

                    for (MTUserItem item : section.getItems()) {
                        if (item != null) {
                            LOGD(TAG, "Adding item at index: " + currentIndex + " for type: " + NAMED_OBJECT_USER_ITEM);
                            listItems.add(new NamedObject(NAMED_OBJECT_USER_ITEM, item));
                            ++currentIndex;
                        }
                    }
                }
            }
        }
        return listItems;
    }

    public static boolean isSection(NamedObject namedObject) {
        return namedObject.name.equalsIgnoreCase(NAMED_OBJECT_SECTION);
    }

    public static boolean isUserItem(NamedObject namedObject) {
        return namedObject.name.equalsIgnoreCase(NAMED_OBJECT_USER_ITEM);
    }

    public static ArrayList<MTSection> sectionsWithAppendedResponse(ArrayList<MTSection> sections, MTUserItemResponse response) {

        ArrayList<MTSection> finalSections = new ArrayList<>();

        boolean foundSection;

        // Cycle through all the new sections
        for(MTSection newSection : response.getSections()) {

            // Assume not found until we do
            foundSection = false;

            for (MTSection currentSection : sections) {

                if (newSection.getSectionId().equalsIgnoreCase(currentSection.getSectionId())) {

                    // Add the items to the current section
                    currentSection.addItems(newSection.getItems());
                    foundSection = true;
                    break;
                }
            }

            if (!foundSection) {
                finalSections.add(newSection);
            }
        }

        finalSections.addAll(sections);

        return finalSections;
    }

    public void onEvent(MTChangeInventoryEvent event) {
        LOGD("MTUserItemManager", "***** Received MTChangeInventoryEvent ***** for EventBus");
        if (event != null) {
            mIsDirty = true;
        }
    }

    public void onEvent(MTAddItemEvent event) {
        LOGD("MTUserItemManager", "***** Received MTAddItemEvent ***** for EventBus");
        if (event != null && event.originatedFrom instanceof AddItemDetailActivity) {
            mIsDirty = true;
        }
    }
}
