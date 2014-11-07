package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.CreateAccountActivity;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTCreateAccountFooterView extends RelativeLayout {

    private CreateAccountActivity mCreateAccountActivity;
    private MTCheckBox mOptInCheckBox;
    private MTButton mCreateAccountBtn;

    public MTCreateAccountFooterView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_create_account_footer, this);

        if (activity instanceof CreateAccountActivity) {
            mCreateAccountActivity = (CreateAccountActivity)activity;
        }

        mOptInCheckBox = (MTCheckBox)findViewById(R.id.emailCommunicationCheckbox);
        mCreateAccountBtn = (MTButton)findViewById(R.id.footerCreateAccountButton);

        mCreateAccountBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCreateAccountActivity != null) {
                    mCreateAccountActivity.postCreateAccount();
                }
            }
        });
    }

    public boolean userOptedIn() {
        return mOptInCheckBox.isChecked();
    }
}