package com.milwaukeetool.mymilwaukee.view;

import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.LandingActivity;
import com.milwaukeetool.mymilwaukee.activity.MainActivity;
import com.milwaukeetool.mymilwaukee.fragment.SettingsFragment;
import com.milwaukeetool.mymilwaukee.util.MTUtils;

/**
 * Created by cent146 on 11/19/14.
 */
public class MTSettingsFooterView extends RelativeLayout {

    private SettingsFragment mSettingsFragment;
    private MainActivity mMainActivity = null;
    private MTButton mLogOutButton;


    public MTSettingsFooterView(Fragment fragment) {
        super(fragment.getActivity());
        LayoutInflater.from(fragment.getActivity()).inflate(R.layout.view_settings_footer, this);

        if (fragment instanceof SettingsFragment) {
            mSettingsFragment = (SettingsFragment) fragment;
        }

        if (fragment.getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity)fragment.getActivity();
        }

        mLogOutButton = (MTButton) findViewById(R.id.settingsFooterLogOutButton);
//        mLogOutButton.setOnTouchListener(new MTTouchListener(mMainActivity) {
//            @Override
//            public void didTapView(MotionEvent event) {
//
//                MTUtils.clearLoginInfo();
//
//                if (mMainActivity != null) {
//                    Intent intent = new Intent(mMainActivity, LandingActivity.class);
//                    mMainActivity.startActivity(intent);
//                    mMainActivity.finish();
//                }
//            }
//        });
        mLogOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MTUtils.clearLoginInfo();

                if (mMainActivity != null) {
                    Intent intent = new Intent(mMainActivity, LandingActivity.class);
                    mMainActivity.startActivity(intent);
                    mMainActivity.finish();
                }
            }
        });
    }
}