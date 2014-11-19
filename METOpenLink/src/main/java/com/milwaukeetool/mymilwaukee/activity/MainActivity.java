package com.milwaukeetool.mymilwaukee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;
import com.milwaukeetool.mymilwaukee.view.MTTextView;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/12/14.
 */
public class MainActivity extends MTActivity {

    private static final String TAG = makeLogTag(CreateAccountActivity.class);

    private MTTextView mVersionTypeDistributionTextView;
    private MTButton mLogOutButton;
    private MTButton mUpdateProfileButton;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_main);
        this.setupView();
    }

    protected void setupView() {
        mVersionTypeDistributionTextView = (MTTextView) findViewById(R.id.versionTypeDistributionTextView);

        if (MTConfig.isExternalRelease()) {
            // Hide the version label
            mVersionTypeDistributionTextView.setVisibility(View.INVISIBLE);
        } else {
            mVersionTypeDistributionTextView.setVisibility(View.VISIBLE);
            mVersionTypeDistributionTextView.setText(MTConfig.getDistributionTypeVersionString());
        }

        mLogOutButton = (MTButton) findViewById(R.id.logOutButton);
        mLogOutButton.setOnTouchListener(new MTTouchListener(this) {
            @Override
            public void didTapView(MotionEvent event) {
                MTUtils.clearLoginInfo();

                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        mUpdateProfileButton = (MTButton) this.findViewById(R.id.updateProfileButton);
        mUpdateProfileButton.setOnTouchListener(new MTTouchListener(this) {
            @Override
            public void didTapView(MotionEvent event) {
                Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Log analytics
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
