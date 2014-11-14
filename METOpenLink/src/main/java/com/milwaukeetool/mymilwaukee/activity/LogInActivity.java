package com.milwaukeetool.mymilwaukee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.interfaces.ConnectivityAware;
import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.model.response.MTLogInResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTLoginFooterView;
import com.milwaukeetool.mymilwaukee.view.MTLoginHeaderView;
import com.milwaukeetool.mymilwaukee.view.MTProgressView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Design;

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

    private MTProgressView mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        this.setupViews();
    }

    protected void setupViews() {
        ListView listView = (ListView)findViewById(R.id.login_header);
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        LinkedList<View> views = new LinkedList<View>();

        mProgressView = (MTProgressView)findViewById(R.id.progressIndicatorView);
        mProgressView.setVisibility(View.GONE);

        mHeaderView = new MTLoginHeaderView(this);
        listView.addHeaderView(mHeaderView);

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.sign_in_field_title_email))
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true).updateFocus();
        views.add(mEmailFieldView);

        mPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this,MiscUtils.getString(R.string.sign_in_field_title_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        views.add(mPasswordFieldView);

        mFooterView = new MTLoginFooterView(this);
        listView.addFooterView(mFooterView);

        mLoginAdapter = new LogInAdapter(views);

        if (listView != null) {
            listView.setAdapter(mLoginAdapter);
            listView.setFocusable(true);
        }
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

                Intent mainIntent = new Intent(LogInActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                LOGD(TAG, "Failed to log in for user");
                retrofitError.printStackTrace();

                // Hide progress indicator
                mProgressView.stopProgress();

                // Handle timeout


//                String errorMsg = MTWebInterface.getErrorMessage(retrofitError);

//                if (TextUtils.isEmpty(errorMsg)) {
//                    errorMsg = MiscUtils.getString(R.string.text_sign_in_failure);
//                }

                // Handle standard error
                PostOffice.newMail(LogInActivity.this)
                        .setTitle(MiscUtils.getString(R.string.dialog_title_sign_in_failure))
                        .setMessage(MTWebInterface.getErrorMessage(retrofitError))
                        .setThemeColor(MiscUtils.getAppResources().getColor(R.color.mt_red))
                        .setDesign(Design.HOLO_LIGHT)
                        .show(getFragmentManager());
            }
        };

        MTWebInterface.sharedInstance().getUserService().login(MTConfig.getServerToken(),
                this.mEmailFieldView.getFieldValue(),
                this.mPasswordFieldView.getFieldValue(),
                MTConstants.LOG_IN_GRANT_TYPE_PASSWORD,
                responseCallback);
    }

    public void onEvent(MTNetworkAvailabilityEvent event) {
        if (!event.isNetworkAvailable) {
            ConnectivityAware view = (ConnectivityAware) mFooterView;
            view.connectionDestroyed();
        } else {
            ConnectivityAware view = (ConnectivityAware) mFooterView;
            view.connectionEstablished();
        }
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

}
