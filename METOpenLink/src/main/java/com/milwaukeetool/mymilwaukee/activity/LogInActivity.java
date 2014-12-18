package com.milwaukeetool.mymilwaukee.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.response.MTLogInResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view_custom.MTLoginFooterView;
import com.milwaukeetool.mymilwaukee.view_custom.MTLoginHeaderView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.milwaukeetool.mymilwaukee.view.MTTextView;

import java.util.LinkedList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 10/30/2014.
 */
public class LogInActivity extends MTActivity {

    private static final String TAG = makeLogTag(LogInActivity.class);

    private ListView mListView;
    private LogInAdapter mLoginAdapter;

    private MTLoginHeaderView mHeaderView;
    private MTLoginFooterView mFooterView;

    private MTSimpleFieldView mEmailFieldView;
    private MTSimpleFieldView mPasswordFieldView;

    private MTTextView mNoNetworkConnectivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mEnabledNoNetworkView = false;
        super.onCreate(savedInstanceState);
        this.setupViews();
    }

    protected void setupViews() {

        mListView = (ListView)findViewById(R.id.login_header);
        mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        mListView.setStackFromBottom(true);

        LinkedList<View> views = new LinkedList<View>();

        mHeaderView = new MTLoginHeaderView(this);
        mListView.addHeaderView(mHeaderView, null, false);

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.sign_in_field_title_email))
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true).updateFocus();
        mEmailFieldView.setTextColorResource(R.color.mt_white);
        mEmailFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        views.add(mEmailFieldView);

        mPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this,MiscUtils.getString(R.string.sign_in_field_title_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMaxLength(1024);
        views.add(mPasswordFieldView);
        mPasswordFieldView.setTextColorResource(R.color.mt_white);
        mPasswordFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        mPasswordFieldView.setNextActionGo();

        mFooterView = new MTLoginFooterView(this);
        mListView.addFooterView(mFooterView, null, false);

        mLoginAdapter = new LogInAdapter(views);

        if (mListView != null) {
            mListView.setAdapter(mLoginAdapter);
            mListView.setFocusable(true);
        }

        mNoNetworkConnectivityTextView = (MTTextView)findViewById(R.id.noNetworkConnectivityTextView);
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_log_in);
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
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return MiscUtils.getString(R.string.mt_screen_name_sign_in);
    }

    protected boolean isTextFieldsValid() {
        if (mEmailFieldView.isValid() && mPasswordFieldView.isValid()) {
            return true;
        }

        return false;
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

    public void postLogIn() {

        // Hide the keyboard, if shown
        UIUtils.hideKeyboard(this);

        if (!this.isTextFieldsValid()) {
            return;
        }

        // Show progress indicator
        mProgressView.updateMessage(MiscUtils.getString(R.string.progress_bar_logging_in));
        mProgressView.startProgress();

        Callback<MTLogInResponse> responseCallback = new Callback<MTLogInResponse>() {
            @Override
            public void success(MTLogInResponse result, Response response) {

                // Hide progress indicator
                mProgressView.stopProgress();

                if (MTConstants.TOKEN_TYPE_BEARER.equalsIgnoreCase(result.tokenType)) {
                    MTUtils.setLoginInfo(mEmailFieldView.getFieldValue(), result.token, result.tokenType);
                }

                LOGD(TAG, "Successfully logged in for user with token: " + result.token);

                MTUtils.launchMainActivityAndFinishCurrent(LogInActivity.this);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                LOGD(TAG, "Failed to log in for user");

                // Hide progress indicator
                mProgressView.stopProgress();

                MTUtils.handleRetrofitError(retrofitError, LogInActivity.this,
                        MiscUtils.getString(R.string.dialog_title_sign_in_failure));

                // On failure, reset the password field
                mPasswordFieldView.fieldRequiresReset(true);
            }
        };

        MTWebInterface.sharedInstance().getUserService().login(MTConfig.getServerToken(),
                this.mEmailFieldView.getFieldValue(),
                this.mPasswordFieldView.getFieldValue(),
                MTConstants.LOG_IN_GRANT_TYPE_PASSWORD,
                responseCallback);
    }

    public void onEvent(MTimeActionEvent event) {
        if (event.action == EditorInfo.IME_ACTION_GO) {
            postLogIn();
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

    private class LogInAdapter extends SackOfViewsAdapter {

        public LogInAdapter(List<View> views) {
            super(views);
        }

        @Override
        protected View newView(int position, ViewGroup parent) {
            View view = super.newView(position, parent);
            return view;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }

    @Override
    public void onBackPressed() {

        if (mProgressView.isDisplayed()) {
            return;
        }
        super.onBackPressed();
    }
}
