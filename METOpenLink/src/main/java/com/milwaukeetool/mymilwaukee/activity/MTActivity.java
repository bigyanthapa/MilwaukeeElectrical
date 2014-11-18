package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTLayout;
import com.milwaukeetool.mymilwaukee.view.MTNoNetworkView;
import com.milwaukeetool.mymilwaukee.view.MTProgressView;

import de.greenrobot.event.EventBus;

/**
 * Created by cent146 on 11/11/14.
 */
public abstract class MTActivity extends Activity {

    protected MTLayout mRootLayout;
    protected MTProgressView mProgressView;
    protected MTNoNetworkView mNoNetworkView;

    protected boolean mEnabledNoNetworkView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        // Clear the keep screen on flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        performSetup();
    }

    protected void performSetup() {
        setupActivityView();
        baseActivityViewSetup();
    }

    protected abstract void setupActivityView();

    protected void baseActivityViewSetup() {

        // Get base layout (which must be MTLayout)
        mRootLayout = (MTLayout)findViewById(R.id.mtBaseLayout);

        // Embed the view into a relative layout
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Add to end of the view
        mRootLayout.addView(relativeLayout, mRootLayout.getChildCount());

        // Add no network view to be used everywhere
        mNoNetworkView = new MTNoNetworkView(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                UIUtils.getPixels(80));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        mNoNetworkView.setLayoutParams(layoutParams);
        relativeLayout.addView(mNoNetworkView);
        mNoNetworkView.setVisibility(View.GONE);

        // Create progress view to be used everywhere!
        mProgressView = new MTProgressView(this);
        mProgressView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Add to the end of the layout
        mRootLayout.addView(mProgressView, mRootLayout.getChildCount());
        mProgressView.setVisibility(View.GONE);
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
        NetworkUtil.checkNetworkConnectivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    // Must implement these methods in the subclassed activity
    protected abstract String getLogTag();
    protected abstract String getScreenName();

    // Always implement at least the network availability event
    public void onEvent(MTNetworkAvailabilityEvent event) {
        //LOGE(getLogTag(), "Must implement the onEvent handling for Network Availability");
        if (!event.isNetworkAvailable) {
            if (mEnabledNoNetworkView) {
                this.mNoNetworkView.showMessage();
            }
        } else {
            if (mEnabledNoNetworkView) {
                this.mNoNetworkView.hideMessage();
            }
        }
    }
}
