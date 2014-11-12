package com.milwaukeetool.mymilwaukee.model.event;

import android.app.Activity;

/**
 * Created by cent146 on 11/11/14.
 */
public class MTimeActionEvent extends MTEvent {
    public Activity callingActivity = null;
    public String fieldName = null;
    public int action = 0;

    public MTimeActionEvent(Object _originatedFrom, int _action, Activity _callingActivity, String _fieldName) {
        super.MTEvent(_originatedFrom);
        this.fieldName = _fieldName;
        this.callingActivity = _callingActivity;
        this.action = _action;
    }
}
