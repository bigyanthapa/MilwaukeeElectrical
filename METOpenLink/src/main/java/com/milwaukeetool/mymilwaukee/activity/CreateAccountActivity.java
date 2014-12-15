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
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.model.event.MTKeyboardEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTUserRegistrationRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTLogInResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTCreateAccountFooterView;
import com.milwaukeetool.mymilwaukee.view.MTCreateAccountHeaderView;
import com.milwaukeetool.mymilwaukee.view.MTSelectableFieldView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.milwaukeetool.mymilwaukee.view.MTTextView;
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
 * Created by cent146 on 10/24/14.
 */
public class CreateAccountActivity extends MTActivity implements Postable {

    private static final String TAG = makeLogTag(CreateAccountActivity.class);

    private ListView mListView;
    private CreateAccountAdapter mCreateAccountAdapter;

    private MTCreateAccountHeaderView mHeaderView;
    private MTCreateAccountFooterView mFooterView;

    private MTSimpleFieldView mEmailFieldView;
    private MTSimpleFieldView mPasswordFieldView;
    private MTSimpleFieldView mConfirmPasswordFieldView;
    private MTSimpleFieldView mFirstNameFieldView;
    private MTSimpleFieldView mLastNameFieldView;
    private MTSelectableFieldView mTradeOccupationFieldView;

    private MTTextView mNoNetworkConnectivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mEnabledNoNetworkView = false;
        super.onCreate(savedInstanceState);
        this.setupViews();
    }

    protected void setupViews() {

        mNoNetworkConnectivityTextView = (MTTextView)findViewById(R.id.noNetworkConnectivityTextView);

        mListView = (ListView)findViewById(R.id.createAccountListView);
//        mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
//        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//        mListView.setStackFromBottom(true);

        LinkedList<View> views = new LinkedList<View>();

        mHeaderView = new MTCreateAccountHeaderView(this);
        mListView.addHeaderView(mHeaderView, null, false);

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_email))
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true);
        mEmailFieldView.setTextColorResource(R.color.mt_white);
        mEmailFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        views.add(mEmailFieldView);

        mPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mPasswordFieldView.setTextColorResource(R.color.mt_white);
        mPasswordFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        views.add(mPasswordFieldView);

        mConfirmPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_confirm_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mConfirmPasswordFieldView.setTextColorResource(R.color.mt_white);
        mConfirmPasswordFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        views.add(mConfirmPasswordFieldView);

        mFirstNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_first_name)).setRequired(true);
        mFirstNameFieldView.setTextColorResource(R.color.mt_white);
        mFirstNameFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        views.add(mFirstNameFieldView);

        mLastNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_last_name)).setRequired(true);
        mLastNameFieldView.setTextColorResource(R.color.mt_white);
        mLastNameFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        views.add(mLastNameFieldView);

        String[] selectableOptionArray = this.getResources().getStringArray(R.array.trade_occupation_array);
        mTradeOccupationFieldView = MTSelectableFieldView.createSelectableFieldView(this, MiscUtils.getString(R.string.create_account_field_trade),selectableOptionArray).setRequired(true);
        mTradeOccupationFieldView.setNextActionDone();
        mTradeOccupationFieldView.setTextColorResource(R.color.mt_white);
        mTradeOccupationFieldView.setHintColorTextResource(R.color.mt_very_light_gray);
        views.add(mTradeOccupationFieldView);

        mFooterView = new MTCreateAccountFooterView(this);
        mListView.addFooterView(mFooterView, null, false);

        mCreateAccountAdapter = new CreateAccountAdapter(views);

        if (mListView != null) {
            mListView.setAdapter(mCreateAccountAdapter);
            mListView.setFocusable(true);
        }
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_create_account);
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

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_create_account);
    }

    protected boolean isTextFieldsValid() {
        if (mEmailFieldView.isValid() &&
                mPasswordFieldView.isValid() &&
                mConfirmPasswordFieldView.isValid() &&
                mFirstNameFieldView.isValid() &&
                mLastNameFieldView.isValid()) {
            return true;
        }

        return false;
    }

    public void post(CharSequence option) {
        mTradeOccupationFieldView.setFieldValue(option.toString());
    }

    public void postCreateAccount() {

        UIUtils.hideKeyboard(this);

        // Run validation, show error
        if (!this.isTextFieldsValid()) {
            return;
        }

        // Check if the confirmation password matches the given password
        if (!mPasswordFieldView.getFieldValue().equals(mConfirmPasswordFieldView.getFieldValue())) {
            //Toast.makeText(CreateAccountActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            PostOffice.newMail(CreateAccountActivity.this)
                    .setTitle(getResources().getString(R.string.dialog_title_register_failure))
                    .setMessage(getResources().getString(R.string.create_account_no_password_match))
                    .setThemeColor(getResources().getColor(R.color.mt_red))
                    .setDesign(Design.HOLO_LIGHT)
                    .show(getFragmentManager());
            return;
        }

        // Validation passed, continue with request

        // Show progress indicator
        mProgressView.updateMessage(this.getResources().getString(R.string.progress_bar_registering));
        mProgressView.startProgress();

        // Create the request for user registration
        final MTUserRegistrationRequest request = new MTUserRegistrationRequest();
        request.userFirstName = mFirstNameFieldView.getFieldValue();
        request.userLastName = mLastNameFieldView.getFieldValue();
        request.userOccupation = mTradeOccupationFieldView.getFieldValue();
        request.userEmail = mEmailFieldView.getFieldValue();
        request.userPassword = mPasswordFieldView.getFieldValue();
        request.userConfirmPassword = mConfirmPasswordFieldView.getFieldValue();
        request.userOptIn = mFooterView.userOptedIn();

        Callback<Response> responseCallback = new Callback<Response>() {

            @Override
            public void success(Response result, Response response) {

                // Hide progress indicator
                mProgressView.stopProgress();

                LOGD(TAG, "Successfully registered user: " + result.getBody().toString());

                performLogin(request);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                LOGD(TAG, "Failed to register user");

                // Hide progress indicator
                mProgressView.stopProgress();

                MTUtils.handleRetrofitError(retrofitError, CreateAccountActivity.this,
                        MiscUtils.getString(R.string.dialog_title_register_failure));
            }
        };

        MTWebInterface.sharedInstance().getUserService().registerUser(request, responseCallback);
    }

    public void onEvent(MTKeyboardEvent event) {
        if (event.keyboardDisplayed) {
            LOGD(TAG, "Keyboard listener: Shown");

        } else {
            LOGD(TAG, "Keyboard listener: Hidden");

        }
    }

    public void onEvent(MTimeActionEvent event) {
        if (event.callingActivity == this) {
            if (event.action == EditorInfo.IME_ACTION_NEXT && event.fieldName.equalsIgnoreCase(mLastNameFieldView.getFieldName())) {
                mTradeOccupationFieldView.showSelectableOptions();
            }
        }
    }

    private void performLogin(MTUserRegistrationRequest request) {
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

                MTUtils.launchMainActivityAndFinishCurrent(CreateAccountActivity.this);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                LOGD(TAG, "Failed to log in for user");

                // Hide progress indicator
                mProgressView.stopProgress();

                MTUtils.handleRetrofitError(retrofitError, CreateAccountActivity.this,
                        MiscUtils.getString(R.string.dialog_title_sign_in_failure));
            }
        };

        MTWebInterface.sharedInstance().getUserService().login(MTConfig.getServerToken(),
                request.userEmail,
                request.userPassword,
                MTConstants.LOG_IN_GRANT_TYPE_PASSWORD,
                responseCallback);
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

    private class CreateAccountAdapter extends SackOfViewsAdapter {

        public CreateAccountAdapter(List<View> views) {
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
