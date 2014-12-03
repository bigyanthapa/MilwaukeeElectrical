package com.milwaukeetool.mymilwaukee.model.event;

import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;

import java.util.ArrayList;

/**
 * Created by cent146 on 12/2/14.
 */
public class MTSearchResultEvent extends MTResponseEvent {

    private ArrayList<MTItemSearchResult> searchResults;

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
}
