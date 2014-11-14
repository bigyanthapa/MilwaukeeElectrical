package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.CreateAccountActivity;
import com.milwaukeetool.mymilwaukee.activity.LogInActivity;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTLoginFooterView extends RelativeLayout {

    private LogInActivity mLoginActivity;
    private MTButton mLogInBtn;
    private MTTextView mNoNetworkConnectivity;

    public MTLoginFooterView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_login_footer, this);

        if (activity instanceof LogInActivity) {
            mLoginActivity = (LogInActivity) activity;
        }

        mNoNetworkConnectivity = (MTTextView)findViewById(R.id.loginNoNetworkConnectivity);
        this.setConnectivityDisplay();

        mLogInBtn = (MTButton) findViewById(R.id.footerLoginButton);

        mLogInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginActivity != null) {
                    mLoginActivity.postLogIn();
                }
            }
        });
    }

    public void setConnectivityDisplay() {
        NetworkUtil.NetworkType type = NetworkUtil.getConnectivityStatus(mNoNetworkConnectivity.getContext());

        switch (type) {
            case TYPE_NOT_CONNECTED:
                mNoNetworkConnectivity.setVisibility(View.VISIBLE);
                break;
            default:
                mNoNetworkConnectivity.setVisibility(View.INVISIBLE);
        }
    }

    public void showNoNetworkMessage() {
        mNoNetworkConnectivity.setVisibility(View.VISIBLE);
    }

    public void hideNoNetworkMessage() {
        mNoNetworkConnectivity.setVisibility(View.INVISIBLE);
    }
}