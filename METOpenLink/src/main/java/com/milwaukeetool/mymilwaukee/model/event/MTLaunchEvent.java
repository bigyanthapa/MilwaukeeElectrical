package com.milwaukeetool.mymilwaukee.model.event;

import com.milwaukeetool.mymilwaukee.view.MTLaunchableFieldView;

import java.util.EventObject;

/**
 * Created by scott.hopfensperger on 11/24/2014.
 */
public class MTLaunchEvent extends EventObject {
    private MTLaunchableFieldView view;

    public MTLaunchEvent(MTLaunchableFieldView source) {
        super(source);
        this.view = source;
    }

    public MTLaunchableFieldView getView() {
        return view;
    }

    public void setView(MTLaunchableFieldView view) {
        this.view = view;
    }
}
