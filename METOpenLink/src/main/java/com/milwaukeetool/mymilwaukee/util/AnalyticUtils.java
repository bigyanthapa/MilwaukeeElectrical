package com.milwaukeetool.mymilwaukee.util;

import android.app.Activity;

import com.google.android.gms.analytics.Tracker;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;

/**
 * Created by cent146 on 10/24/14.
 */
public class AnalyticUtils {

    public enum TrackerName {
        APP_TRACKER
    }

    public static void init(Activity activity) {
        MilwaukeeToolApplication mtApp = (MilwaukeeToolApplication) activity.getApplication();
        Tracker tracker = mtApp.getTracker();
        //tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }
}
