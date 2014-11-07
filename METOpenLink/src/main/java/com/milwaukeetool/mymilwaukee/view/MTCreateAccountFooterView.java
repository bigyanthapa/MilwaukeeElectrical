package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.CreateAccountActivity;
import com.milwaukeetool.mymilwaukee.config.MTConstants;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTCreateAccountFooterView extends RelativeLayout {

    private CreateAccountActivity mCreateAccountActivity;
    private MTCheckBox mOptInCheckBox;
    private MTButton mCreateAccountBtn;
    private MTTextView mLegalText;

    public MTCreateAccountFooterView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_create_account_footer, this);

        if (activity instanceof CreateAccountActivity) {
            mCreateAccountActivity = (CreateAccountActivity)activity;
        }

        mOptInCheckBox = (MTCheckBox)findViewById(R.id.emailCommunicationCheckbox);
        mCreateAccountBtn = (MTButton)findViewById(R.id.footerCreateAccountButton);
        mLegalText = (MTTextView)findViewById(R.id.privacyPolicyTextView);

        this.setLegalText();

        mCreateAccountBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCreateAccountActivity != null) {
                    mCreateAccountActivity.postCreateAccount();
                }
            }
        });
    }

    private void setLegalText() {
        String privacyString = "<a href=\"" + MTConstants.PRIVACY_POLICY_URL + "\">" +
                getResources().getString(R.string.title_privacy_policy) + "</a>";
        mLegalText.setText(Html.fromHtml(privacyString));
        mLegalText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public boolean userOptedIn() {
        return mOptInCheckBox.isChecked();
    }
}