package com.milwaukeetool.mymilwaukee.model.event;

/**
 * Created by cent146 on 11/12/14.
 */
public class MTNetworkAvailabilityEvent extends MTEvent {

    public boolean isNetworkAvailable;

    public MTNetworkAvailabilityEvent(Object _originatedFrom, boolean _isNetworkAvailable) {
        super.MTEvent(_originatedFrom);
        this.isNetworkAvailable = _isNetworkAvailable;
    }

}
