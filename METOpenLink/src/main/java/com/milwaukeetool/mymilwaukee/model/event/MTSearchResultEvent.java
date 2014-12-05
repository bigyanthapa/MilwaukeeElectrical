package com.milwaukeetool.mymilwaukee.model.event;

import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;

import java.util.ArrayList;

/**
 * Created by cent146 on 12/2/14.
 */
public class MTSearchResultEvent extends MTResponseEvent {

    private ArrayList<MTItemSearchResult> searchResults;

    private String lastSearchTerm = null;

    private int lastSkipCount = 0;

    private int lastSearchResultCount = 0;

    public MTSearchResultEvent(Object _originatedFrom, ArrayList<MTItemSearchResult> _searchResults) {
        super.MTEvent(_originatedFrom);
        setSearchResults(_searchResults);
    }

    public ArrayList<MTItemSearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ArrayList<MTItemSearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public String getLastSearchTerm() {
        return lastSearchTerm;
    }

    public void setLastSearchTerm(String lastSearchTerm) {
        this.lastSearchTerm = lastSearchTerm;
    }

    public int getLastSearchResultCount() {
        return lastSearchResultCount;
    }

    public void setLastSearchResultCount(int lastSearchResultCount) {
        this.lastSearchResultCount = lastSearchResultCount;
    }

    public int getLastSkipCount() {
        return lastSkipCount;
    }

    public void setLastSkipCount(int lastSkipCount) {
        this.lastSkipCount = lastSkipCount;
    }
}
