package com.milwaukeetool.mymilwaukee.view;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.interfaces.ClickListener;

/**
 * Created by cent146 on 11/19/14.
 */
public class MTSettingsFooterView extends RelativeLayout {

//    private SettingsFragment mSettingsFragment;
//    private MainActivity mMainActivity = null;
    private MTButton mLogOutButton;
    private MTTextView mVersionTypeDistributionTextView;

    private ClickListener mLogoutListener = null;

    public MTSettingsFooterView(Fragment fragment) {
        super(fragment.getActivity());
        LayoutInflater.from(fragment.getActivity()).inflate(R.layout.view_settings_footer, this);

//        if (fragment instanceof SettingsFragment) {
//            mSettingsFragment = (SettingsFragment) fragment;
//        }

//        if (fragment.getActivity() instanceof MainActivity) {
//            mMainActivity = (MainActivity)fragment.getActivity();
//        }

        mVersionTypeDistributionTextView = (MTTextView) findViewById(R.id.versionTypeDistributionTextView);

        if (MTConfig.isExternalRelease()) {
            // Hide the version label
            mVersionTypeDistributionTextView.setVisibility(View.INVISIBLE);
        } else {
            mVersionTypeDistributionTextView.setVisibility(View.VISIBLE);
            mVersionTypeDistributionTextView.setText(MTConfig.getDistributionTypeVersionString());
        }

        mLogOutButton = (MTButton) findViewById(R.id.settingsFooterLogOutButton);
        mLogOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLogoutListener != null) {
                    mLogoutListener.didClickItem(v);
                }
            }
        });
    }

    public void setLogoutListener(ClickListener logoutListener) {
        mLogoutListener = logoutListener;
    }
}
