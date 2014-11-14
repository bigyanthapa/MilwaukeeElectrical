package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.LogInActivity;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTLoginFooterView extends RelativeLayout {

    private LogInActivity mLoginActivity;
    private MTButton mLogInBtn;


    public MTLoginFooterView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_login_footer, this);

        if (activity instanceof LogInActivity) {
            mLoginActivity = (LogInActivity) activity;
        }

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
}