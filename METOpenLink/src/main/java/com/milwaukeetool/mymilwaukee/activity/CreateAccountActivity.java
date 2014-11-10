package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.model.request.MTUserRegistrationRequest;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.view.MTCreateAccountFooterView;
import com.milwaukeetool.mymilwaukee.view.MTCreateAccountHeaderView;
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
public class CreateAccountActivity extends Activity {

    private static final String TAG = makeLogTag(MainActivity.class);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.setupViews();
    }

    protected void setupViews() {
        ListView listView = (ListView)findViewById(R.id.header_stuff);
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        LinkedList<View> views = new LinkedList<View>();

        mHeaderView = new MTCreateAccountHeaderView(this);
        listView.addHeaderView(mHeaderView);

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this,"Email Address")
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true).updateFocus();
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
        mLastNameFieldView.setNextActionDone();

        mTradeOccupationFieldView = MTSelectableFieldView.createSelectableFieldView(this,"Trade/Occupation").setRequired(true);
        views.add(mTradeOccupationFieldView);

        mFooterView = new MTCreateAccountFooterView(this);
        listView.addFooterView(mFooterView);

        mCreateAccountAdapter = new CreateAccountAdapter(views);

        if (listView != null) {
            listView.setAdapter(mCreateAccountAdapter);
            listView.setFocusable(true);
        }
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        AnalyticUtils.logScreenView(this, "Register");
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
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
    public void postCreateAccount() {

        // Run validation, show error
        if (!this.isTextFieldsValid()) {

            PostOffice.newMail(this)
                    .setTitle("Account cannot be created")
                    .setMessage("Please correct any errors indicated.")
//                    .setIcon(R.drawable.ic_launcher)
                    .setThemeColor(getResources().getColor(R.color.mt_red))
                    .setDesign(Design.HOLO_LIGHT)
                    .show(getFragmentManager());

            return;
        }

        // Check if the confirmation password matches the given password
        if (!mPasswordFieldView.getFieldValue().equals(mConfirmPasswordFieldView.getFieldValue())) {
            Toast.makeText(CreateAccountActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validation passed, continue with request

        // Show progress indicator

        MTUserRegistrationRequest request = new MTUserRegistrationRequest();
        request.userFirstName = mFirstNameFieldView.getFieldValue();
        request.userLastName = mLastNameFieldView.getFieldValue();
        request.userOptIn = mFooterView.userOptedIn();
        request.userOccupation = "DIY";
        request.userEmail = mEmailFieldView.getFieldValue();
        request.userPassword = mPasswordFieldView.getFieldValue();
        request.userConfirmPassword = mConfirmPasswordFieldView.getFieldValue();

        Callback<Response> responseCallback = new Callback<Response>() {

            @Override
            public void success(Response result, Response response) {

                // Hide progress indicator

                LOGD(TAG, "Successfully registered user: " + result.getBody().toString());
                Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                // Hide progress indicator

                LOGD(TAG, "Failed to register user");
                retrofitError.printStackTrace();

                // Handle timeout

                // Handle standard error


                Toast.makeText(CreateAccountActivity.this, MTWebInterface.getErrorMessage(retrofitError), Toast.LENGTH_SHORT).show();
            }
        };

        MTWebInterface.sharedInstance().getUserService().registerUser(request, responseCallback);
    }
}
