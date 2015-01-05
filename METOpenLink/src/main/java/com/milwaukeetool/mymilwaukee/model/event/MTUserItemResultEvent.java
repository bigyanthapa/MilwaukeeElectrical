package com.milwaukeetool.mymilwaukee.model.event;

import com.milwaukeetool.mymilwaukee.model.MTSection;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;

/**
 * Created by cent146 on 12/18/14.
 */
public class MTUserItemResultEvent extends MTResponseEvent {

    private MTUserItemResponse userItemResponse = null;

    private boolean singleRequest = false;

    private int lastCategoryId = 0;

    private int lastManufacturerId = 0;

    private String lastSearchTerm = null;

    private int lastSkipCount = 0;

    private int lastResultCount = -1;

    public MTUserItemResultEvent(Object _originatedFrom, MTUserItemResponse _userItemResponse) {
        super.MTEvent(_originatedFrom);
        setUserItemResponse(_userItemResponse);
    }

    public String getLastSearchTerm() {
        return lastSearchTerm;
    }

    public void setLastSearchTerm(String lastSearchTerm) {
        this.lastSearchTerm = lastSearchTerm;
    }

    public int getLastSkipCount() {
        return lastSkipCount;
    }

    public void setLastSkipCount(int lastSkipCount) {
        this.lastSkipCount = lastSkipCount;
    }

    public MTUserItemResponse getUserItemResponse() {
        return userItemResponse;
    }

    public void setUserItemResponse(MTUserItemResponse userItemResponse) {
        this.userItemResponse = userItemResponse;
    }

    public int getLastCategoryId() {
        return lastCategoryId;
    }

    public void setLastCategoryId(int lastCategoryId) {
        this.lastCategoryId = lastCategoryId;
    }

    public int getLastManufacturerId() {
        return lastManufacturerId;
    }

    public void setLastManufacturerId(int lastManufacturerId) {
        this.lastManufacturerId = lastManufacturerId;
    }

    public int getLastResultCount() {

        if (userItemResponse == null && userItemResponse.getSections() == null) {
            return 0;
        }

        if (lastResultCount == -1) {
            int count = 0;
            for(MTSection currentSection : userItemResponse.getSections()) {
                if (currentSection != null && currentSection.getItems() != null) {
                    count += currentSection.getItemCount();
                }
            }
            lastResultCount = count;
        }
        return lastResultCount;
    }

    public boolean isSingleRequest() {
        return singleRequest;
    }

    public void setSingleRequest(boolean singleRequest) {
        this.singleRequest = singleRequest;
    }
}
