package com.milwaukeetool.mymilwaukee.util;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
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
        tracker.enableAutoActivityTracking(true);
    }

    public static void logScreenView(Activity activity, String screenName) {
        // Get tracker.
        Tracker t = ((MilwaukeeToolApplication)activity.getApplication()).getTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
