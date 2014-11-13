package com.milwaukeetool.mymilwaukee.model.event;

/**
 * Created by cent146 on 11/11/14.
 */
public class MTKeyboardEvent extends MTEvent {
    public boolean keyboardDisplayed;

    public MTKeyboardEvent(Object _originatedFrom, boolean _keyboardDisplayed) {
        super.MTEvent(_originatedFrom);
        keyboardDisplayed = _keyboardDisplayed;
    }
}
