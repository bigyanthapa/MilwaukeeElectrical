package com.milwaukeetool.mymilwaukee.util;

import android.app.Activity;
import android.app.Application;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;

import java.util.HashMap;

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
        tracker.enableAdvertisingIdCollection(true);
    }
}
