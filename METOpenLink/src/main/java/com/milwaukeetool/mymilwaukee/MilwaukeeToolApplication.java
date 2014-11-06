package com.milwaukeetool.mymilwaukee;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.milwaukeetool.mymilwaukee.config.MTConfig;

/**
 * Created by cent146 on 10/24/14.
 */
public class MilwaukeeToolApplication extends Application {
    private static Context _context;
    private Tracker tracker;

    public static Context getAppContext() {
        return _context;
    }

    public static String getApplicationVersionName(Context context) {
        PackageManager packageManager = _context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ex) {
        } catch (Exception e) {
        }
        return "";
    }

    public Tracker getTracker() {

        if (this.tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if (MTConfig.isExternalRelease()) {
                analytics.getLogger()
                        .setLogLevel(Logger.LogLevel.ERROR);
            } else {
                analytics.getLogger()
                        .setLogLevel(Logger.LogLevel.VERBOSE);
            }
            this.tracker = analytics.newTracker(MTConfig.getGoogleAnalyticsId());
        }

        return this.tracker;
    }

    public static int getApplicationVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
        } catch (Exception e) {
        }

        return 0;
    }

    public void onCreate() {
        _context = getApplicationContext();
    }
}
