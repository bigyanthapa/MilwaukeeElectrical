package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 10/24/14.
 */
public class RouterActivity extends Activity {
    private static final String TAG = makeLogTag(RouterActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_router);

        AnalyticUtils.init(this);

        checkForUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkForCrashes();

        Intent intent = new Intent(this, LandingActivity.class);

        // TODO: Add any extras???? Anything to pass
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();

        // TODO: Check if we have logged in already, if not launch Login Activity instead

    }

    private void checkForCrashes() {

        LOGD(TAG,"Checking for HockeyApp Crashes...");

        CrashManager.register(this, MTConfig.getHockeyAppID(), new CrashManagerListener() {
            public boolean shouldAutoUploadCrashes() {
                // Always upload automatically for ALL release builds
                return MTConfig.isExternalRelease();
            }
        });
    }

    private void checkForUpdates() {
        if (!MTConfig.isProduction() && !MTConfig.isBeta()) {

            LOGD(TAG,"Checking for HockeyApp Updates...");

            // Include for hockey app builds
            UpdateManager.register(this, MTConfig.getHockeyAppID());
        }
    }
}
