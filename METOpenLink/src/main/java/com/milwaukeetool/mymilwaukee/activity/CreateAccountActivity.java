package com.milwaukeetool.mymilwaukee.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.model.event.MTKeyboardEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTUserRegistrationRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTLogInResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTCreateAccountFooterView;
import com.milwaukeetool.mymilwaukee.view.MTCreateAccountHeaderView;
import com.milwaukeetool.mymilwaukee.view.MTProgressView;
import com.milwaukeetool.mymilwaukee.view.MTSelectableFieldView;
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
    private MTProgressView mProgressView;
    private ImageButton mCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.setupViews();
    }

    protected void setupViews() {
        mListView = (ListView)findViewById(R.id.header_stuff);
        mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        mListView.setStackFromBottom(true);

        LinkedList<View> views = new LinkedList<View>();

        mProgressView = (MTProgressView)findViewById(R.id.progressIndicatorView);
        mProgressView.setVisibility(View.GONE);

        mHeaderView = new MTCreateAccountHeaderView(this);
        mListView.addHeaderView(mHeaderView, null, false);

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_email))
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true);//.updateFocus();
        views.add(mEmailFieldView);

        mPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        views.add(mPasswordFieldView);

        mConfirmPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_confirm_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        views.add(mConfirmPasswordFieldView);

        mFirstNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_first_name)).setRequired(true);
        views.add(mFirstNameFieldView);

        mLastNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_last_name)).setRequired(true);
        views.add(mLastNameFieldView);

        String[] selectableOptionArray = this.getResources().getStringArray(R.array.trade_occupation_array);
        mTradeOccupationFieldView = MTSelectableFieldView.createSelectableFieldView(this, MiscUtils.getString(R.string.create_account_field_trade),selectableOptionArray).setRequired(true);
        mTradeOccupationFieldView.setNextActionDone();
        views.add(mTradeOccupationFieldView);

        mFooterView = new MTCreateAccountFooterView(this);
        mListView.addFooterView(mFooterView, null, false);

        mCreateAccountAdapter = new CreateAccountAdapter(views);

        mCloseButton = (ImageButton)findViewById(R.id.closeButton);
        mCloseButton.setImageDrawable(new IconDrawable(this, Iconify.IconValue.fa_times_circle).colorRes(R.color.mt_white));
        mCloseButton.setOnTouchListener(new MTTouchListener(this) {
            @Override
            public void didTapView(MotionEvent event) {
                finish();
            }
        });

        if (mListView != null) {
            mListView.setAdapter(mCreateAccountAdapter);
            mListView.setFocusable(true);
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

//        // Test
//        MiscUtils.runDelayed(5000,new MiscUtils.RunDelayedCallback() {
//            @Override
//            public void onFinished() {
//                // Do something
//            }
//        });

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
            //mFooterView.showExtendedView(true);
        } else {
            LOGD(TAG, "Keyboard listener: Hidden");
            //mFooterView.showExtendedView(false);
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

                MTUtils.launchMainActivity(CreateAccountActivity.this);
                finish();
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


    private class CreateAccountAdapter extends SackOfViewsAdapter {

        private EditText mText = null;
        private long mTextLostFocusTimestamp;

        public CreateAccountAdapter(List<View> views) {
            super(views);
            mTextLostFocusTimestamp = -1;
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

// implements View.OnFocusChangeListener
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            View view = super.getView(position, convertView, parent);
//
//            EditText newText = (EditText)view.findViewById(R.id.editTextField);
//            if (mText != null)
//                newText.setText(mText.getText());
//            mText = newText;
//            mText.setOnFocusChangeListener(this);
//            reclaimFocus(mText, mTextLostFocusTimestamp);
//
//            return view;
//        }
//
//
//        private void reclaimFocus(View v, long timestamp) {
//            if (timestamp == -1)
//                return;
//            if ((System.currentTimeMillis() - timestamp) < 250)
//                v.requestFocus();
//        }
//
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            if ((v == mText) && !hasFocus)
//                mTextLostFocusTimestamp = System.currentTimeMillis();
//        }
    }
}
