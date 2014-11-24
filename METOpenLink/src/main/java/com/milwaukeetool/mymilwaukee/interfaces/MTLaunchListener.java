package com.milwaukeetool.mymilwaukee.interfaces;

import com.milwaukeetool.mymilwaukee.model.event.MTLaunchEvent;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Created by scott.hopfensperger on 11/24/2014.
 */
public interface MTLaunchListener extends EventListener {
    void launched(MTLaunchEvent event);
}
