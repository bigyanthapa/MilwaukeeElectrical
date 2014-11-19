package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

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

        MiscUtils.checkForUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MiscUtils.checkForCrashes(this);

        Intent intent = null;

        // Check if we have logged in already, if not launch Login Activity instead
        if (MTUtils.isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LandingActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
        finish();
    }
}
