package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.gson.Gson;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.model.request.MTLogInRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTTokenResponse;
import com.milwaukeetool.mymilwaukee.provider.RestClient;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.view.MTLoginFooterView;
import com.milwaukeetool.mymilwaukee.view.MTLoginHeaderView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Design;

import java.util.LinkedList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 10/30/2014.
 */
public class LogInActivity extends Activity {

    private static final String TAG = makeLogTag(LogInActivity.class);

    private ListView mListView;
    private LogInAdapter mLoginAdapter;

    private MTLoginHeaderView mHeaderView;
    private MTLoginFooterView mFooterView;

    private MTSimpleFieldView mEmailFieldView;
    private MTSimpleFieldView mPasswordFieldView;

    private SmoothProgressBar mProgressBar;

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

        mProgressBar = (SmoothProgressBar)findViewById(R.id.loginProgressBar);
        mProgressBar.setVisibility(View.GONE);

        mHeaderView = new MTLoginHeaderView(this);
        listView.addHeaderView(mHeaderView);

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this,"Email Address")
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true).updateFocus();
        views.add(mEmailFieldView);

        mPasswordFieldView = MTSimpleFieldView.createSimpleFieldView(this,"Password")
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        // Analytics
        AnalyticUtils.logScreenView(this,"Sign In");AnalyticUtils.logScreenView(this,"Sign In");
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);

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
        if (!this.isTextFieldsValid()) {
            PostOffice.newMail(this)
                    .setTitle("Account cannot be created")
                    .setMessage("Please correct any errors indicated.")
                    .setThemeColor(getResources().getColor(R.color.mt_red))
                    .setDesign(Design.HOLO_LIGHT)
                    .show(getFragmentManager());

            return;
        }

        MTLogInRequest request = new MTLogInRequest();
        request.setGrantType("password");
        request.setPassword(this.mPasswordFieldView.getFieldValue());
        request.setUserName(this.mEmailFieldView.getFieldValue());

        // Show progress indicator
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.progressiveStart();

        //String token = "bWlsd2F1a2VlX2FuZHJvaWQ6bmg1c2Z2dHgwcHF1bTM1cnpzNWdkNnJ4M3Q2bXQ1czN4d2JpaXRmeQ==";
        String token = "Basic bWlsd2F1a2VlX2FuZHJvaWQ6bmg1c2Z2dHgwcHF1bTM1cnpzNWdkNnJ4M3Q2bXQ1czN4d2JpaXRmeQ==";

        Gson gson = new Gson();
        TypedString typedString = new TypedString(gson.toJson(request));
        RestClient.get().login(token, this.mEmailFieldView.getFieldValue(), this.mPasswordFieldView.getFieldValue(),
                "password", new Callback<MTTokenResponse>() {
            @Override
            public void success(MTTokenResponse result, Response response) {

                // Hide progress indicator
                mProgressBar.progressiveStop();
                mProgressBar.setVisibility(View.GONE);

                //LOGD(TAG, "Successfully registered user: " + result.getBody().toString());
                //Toast.makeText(LogInActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                String a = "failure";
            }
        });
    }
}
