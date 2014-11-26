package com.milwaukeetool.mymilwaukee.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;

import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.config.MTConfig;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;

/**
 * Created by cent146 on 11/11/14.
 */
public class MiscUtils {

    public interface RunDelayedCallback {
        void onFinished();
    }

    public static void runDelayed(int milliseconds, final RunDelayedCallback callbackInterface) {
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callbackInterface != null) {
                    callbackInterface.onFinished();
                }
            }
        }, milliseconds);
    }

    public static Resources getAppResources() {
        return MilwaukeeToolApplication.getAppContext().getResources();
    }

    public static String getString(int id) {
        return getAppResources().getString(id);
    }

    public static float getDimension(int id) {
        return getAppResources().getDimension(id);
    }


    public static void checkForCrashes(Context context) {

        LOGD("MiscUtils","Checking for HockeyApp Crashes...");

        CrashManager.register(context, MTConfig.getHockeyAppID(), new CrashManagerListener() {
            public boolean shouldAutoUploadCrashes() {
                // Always upload automatically for ALL release builds
                return MTConfig.isExternalRelease();
            }
        });
    }

    public static void checkForUpdates(final Activity activity) {
        if (!MTConfig.isProduction() && !MTConfig.isBeta()) {

            LOGD("MiscUtils","Checking for HockeyApp Updates...");

            // Include for hockey app builds
            UpdateManager.register(activity, MTConfig.getHockeyAppID(), new UpdateManagerListener() {
//                public void onUpdateAvailable() {
//                    Toast.makeText(activity, "Update is available!", Toast.LENGTH_SHORT).show();
//                    super.onUpdateAvailable();
//                }
//                public void onNoUpdateAvailable() {
//                    Toast.makeText(activity, "No updates found.", Toast.LENGTH_SHORT).show();
//                }
            });
        }
    }

    public static void disableKeepScreenOn(Activity activity) {
        // Clear the keep screen on flag
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
