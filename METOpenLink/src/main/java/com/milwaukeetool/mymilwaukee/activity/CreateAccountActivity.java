package com.milwaukeetool.mymilwaukee.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.model.event.MTKeyboardEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTUserRegistrationRequest;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
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

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this,"Email Address")
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true);//.updateFocus();
        views.add(mEmailFieldView);

        mPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this,"Password")
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        views.add(mPasswordFieldView);

        mConfirmPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this, "Confirm Password")
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        views.add(mConfirmPasswordFieldView);

        mFirstNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, "First Name").setRequired(true);
        views.add(mFirstNameFieldView);

        mLastNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, "Last Name").setRequired(true);
        views.add(mLastNameFieldView);

        String[] selectableOptionArray = this.getResources().getStringArray(R.array.trade_occupation_array);
        mTradeOccupationFieldView = MTSelectableFieldView.createSelectableFieldView(this,"Trade/Occupation",selectableOptionArray).setRequired(true);
        mTradeOccupationFieldView.setNextActionDone();
        views.add(mTradeOccupationFieldView);

        mFooterView = new MTCreateAccountFooterView(this);
        mListView.addFooterView(mFooterView, null, false);

        mCreateAccountAdapter = new CreateAccountAdapter(views);

        if (mListView != null) {
            mListView.setAdapter(mCreateAccountAdapter);
            mListView.setFocusable(true);
        }
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

    protected String getLogTag() {
        return TAG;
    }

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
        this.getTradeOccupationFieldView().setFieldValue(option.toString());
    }

    public void postCreateAccount() {

        // Hide the keyboard, if shown
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        // Run validation, show error
        if (!this.isTextFieldsValid()) {
            return;
        }

        // Check if the confirmation password matches the given password
        if (!mPasswordFieldView.getFieldValue().equals(mConfirmPasswordFieldView.getFieldValue())) {
            //Toast.makeText(CreateAccountActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            PostOffice.newMail(CreateAccountActivity.this)
                    .setTitle("Error Registering")
                    .setMessage("Passwords don't match.")
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
        MTUserRegistrationRequest request = new MTUserRegistrationRequest();
        request.userFirstName = mFirstNameFieldView.getFieldValue();
        request.userLastName = mLastNameFieldView.getFieldValue();
        request.userOccupation = mTradeOccupationFieldView.getFieldValue();
        request.userEmail = mEmailFieldView.getFieldValue();
        request.userPassword = mPasswordFieldView.getFieldValue();
        request.userConfirmPassword = mConfirmPasswordFieldView.getFieldValue();
        request.userOptIn = mFooterView.userOptedIn();

        // Test
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressView.stopProgress();
            }
        }, 5000);

        Callback<Response> responseCallback = new Callback<Response>() {

            @Override
            public void success(Response result, Response response) {

                // Hide progress indicator
                mProgressView.stopProgress();

                LOGD(TAG, "Successfully registered user: " + result.getBody().toString());
                Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                LOGD(TAG, "Failed to register user");
                retrofitError.printStackTrace();

                // Hide progress indicator
                mProgressView.stopProgress();

                // Handle timeout

                // Handle standard error
                PostOffice.newMail(CreateAccountActivity.this)
                        .setTitle("Error Registering")
                        .setMessage(MTWebInterface.getErrorMessage(retrofitError))
                        .setThemeColor(getResources().getColor(R.color.mt_red))
                        .setDesign(Design.HOLO_LIGHT)
                        .show(getFragmentManager());
            }
        };

        MTWebInterface.sharedInstance().getUserService().registerUser(request, responseCallback);
    }

    public MTSelectableFieldView getTradeOccupationFieldView() {
        return mTradeOccupationFieldView;
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

}
