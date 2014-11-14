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
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTCreateAccountFooterView extends RelativeLayout {

    private CreateAccountActivity mCreateAccountActivity;
    private MTCheckBox mOptInCheckBox;
    private MTButton mCreateAccountBtn;
    private MTTextView mLegalText;
    private MTTextView mNoNetworkConnectivity;
    //private View mFooterExtendedView;

    public MTCreateAccountFooterView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_create_account_footer, this);

        if (activity instanceof CreateAccountActivity) {
            mCreateAccountActivity = (CreateAccountActivity) activity;
        }

        mOptInCheckBox = (MTCheckBox) findViewById(R.id.emailCommunicationCheckbox);
        mCreateAccountBtn = (MTButton) findViewById(R.id.footerCreateAccountButton);
        mLegalText = (MTTextView) findViewById(R.id.privacyPolicyTextView);
        mNoNetworkConnectivity = (MTTextView) findViewById(R.id.noNetworkConnectivity);
        this.setConnectivityDisplay();

        //mFooterExtendedView = (View)findViewById(R.id.footerExtendedView);

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

    private void setLegalText() {
        String privacyString = "<a href=\"" + MTConstants.PRIVACY_POLICY_URL + "\">" +
                getResources().getString(R.string.title_privacy_policy) + "</a>";
        mLegalText.setText(Html.fromHtml(privacyString));
        mLegalText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void showNoNetworkMessage() {
        mNoNetworkConnectivity.setVisibility(View.VISIBLE);
    }

    public void hideNoNetworkMessage() {
        mNoNetworkConnectivity.setVisibility(View.INVISIBLE);
    }

    public boolean userOptedIn() {
        return mOptInCheckBox.isChecked();
    }

//    public void showExtendedView(boolean show) {
//       mFooterExtendedView.setVisibility(show ? View.VISIBLE : View.GONE);
//
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFooterExtendedView.getLayoutParams();
//        if (show) {
//
//            params.height = 375;
//
//        } else {
//            params.height = 0;
//        }
//
//        mFooterExtendedView.setLayoutParams(params);
//        mFooterExtendedView.invalidate();
//        invalidate();
//    }
}