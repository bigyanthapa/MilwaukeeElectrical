package com.milwaukeetool.mymilwaukee.model.event;

/**
 * Created by cent146 on 12/3/14.
 */
public class MTResponseEvent extends MTEvent {
    public boolean isSuccessful = true;
//    public boolean isUnauthorized = false;
//    public boolean connected = true;

    public void MTResponseEvent(Object _originatedFrom) {
        super.MTEvent(_originatedFrom);
    }
}
