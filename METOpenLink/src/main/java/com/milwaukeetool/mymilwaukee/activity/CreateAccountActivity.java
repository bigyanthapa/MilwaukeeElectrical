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
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;

import java.util.LinkedList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;

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
                .setFieldType(MTSimpleFieldView.FieldType.FIELD_TYPE_EMAIL).updateFocus();
        views.add(mEmailFieldView);

        mPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this,"Password")
                .setFieldType(MTSimpleFieldView.FieldType.FIELD_TYPE_PASSWORD);
        views.add(mPasswordFieldView);

        mConfirmPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this, "Confirm Password")
                .setFieldType(MTSimpleFieldView.FieldType.FIELD_TYPE_PASSWORD);
        views.add(mConfirmPasswordFieldView);

        mFirstNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, "First Name");
        views.add(mFirstNameFieldView);

        mLastNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, "Last Name");
        views.add(mLastNameFieldView);

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

    public void postCreateAccount() {

        // TODO: Run validation, show error


        // Validation passed, continue with request

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
                LOGD(TAG, "Successfully registered user: " + result.getBody().toString());
                Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                LOGD(TAG, "Failed to register user");
                retrofitError.printStackTrace();
                Toast.makeText(CreateAccountActivity.this, MTWebInterface.getErrorMessage(retrofitError), Toast.LENGTH_SHORT).show();
            }
        };

        MTWebInterface.sharedInstance().getUserService().registerUser(request, responseCallback);
    }
}
