package com.milwaukeetool.mymilwaukee.model.event;

/**
 * Created by cent146 on 11/11/14.
 */
public class MTEvent {

    public Object originatedFrom = null;

    public void MTEvent(Object _originatedFrom) {
        this.originatedFrom = _originatedFrom;
    }
}
