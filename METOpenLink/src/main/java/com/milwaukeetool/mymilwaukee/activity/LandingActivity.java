package com.milwaukeetool.mymilwaukee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;
import com.milwaukeetool.mymilwaukee.view.MTButton;
import com.milwaukeetool.mymilwaukee.view.MTTextView;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 10/24/14.
 */
public class LandingActivity extends MTActivity {

    private static final String TAG = makeLogTag(LandingActivity.class);

    private MTButton mCreateAccountBtn;
    private MTButton mLogInBtn;
    private MTTextView mVersionTypeDistributionTextView;
    private MTTextView mNoNetworkConnectivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mEnabledNoNetworkView = false;
        super.onCreate(savedInstanceState);
        Log.i(TAG, "PACKAGE NAME: " + getApplicationContext().getPackageName());

        this.setupView();
        MiscUtils.checkForUpdates(this);
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_landing);
    }

    private void setupView() {
        mNoNetworkConnectivityTextView = (MTTextView)findViewById(R.id.noNetworkConnectivityTextView);

        mCreateAccountBtn = (MTButton)findViewById(R.id.createAccountButton);
        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAccountIntent = new Intent(LandingActivity.this, CreateAccountActivity.class);
                startActivityForResult(createAccountIntent, MTConstants.CREATE_ACCOUNT_REQUEST);
            }
        });

        mLogInBtn = (MTButton)findViewById(R.id.logInButton);
        mLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(LandingActivity.this, LogInActivity.class);
                startActivityForResult(logInIntent, MTConstants.LOGIN_REQUEST);
            }
        });

        mVersionTypeDistributionTextView = (MTTextView)findViewById(R.id.versionTypeDistributionTextView);

        if (MTConfig.isExternalRelease()) {
            // Hide the version label
            mVersionTypeDistributionTextView.setVisibility(View.GONE);
        } else {
            mVersionTypeDistributionTextView.setVisibility(View.VISIBLE);
            mVersionTypeDistributionTextView.setText(MTConfig.getDistributionTypeVersionString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MiscUtils.checkForCrashes(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_landing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void finishFromChild(Activity child) {
//        super.finishFromChild(child);
//        finish();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == MTConstants.LOGIN_REQUEST) {
                finish();
            } else if (requestCode == MTConstants.CREATE_ACCOUNT_REQUEST) {
                finish();
            }
        }
    }

    public void onEvent(MTNetworkAvailabilityEvent event) {
        if (!event.isNetworkAvailable) {
            connectionDestroyed();
        } else {
            connectionEstablished();
        }
    }

    public void connectionEstablished() {
        NetworkUtil.hideConnectivityDisplayAnimated(mNoNetworkConnectivityTextView);
    }
    public void connectionDestroyed() {
        NetworkUtil.showConnectivityDisplayAnimated(mNoNetworkConnectivityTextView);
    }
}
