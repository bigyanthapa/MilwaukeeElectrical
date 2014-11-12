package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by cent146 on 11/11/14.
 */
public abstract class MTActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        AnalyticUtils.logScreenView(this, getScreenName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    protected abstract String getLogTag();
    protected abstract String getScreenName();

}
