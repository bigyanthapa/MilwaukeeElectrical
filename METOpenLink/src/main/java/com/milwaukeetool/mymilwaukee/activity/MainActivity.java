package com.milwaukeetool.mymilwaukee.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.fragment.SettingsFragment;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;
import com.milwaukeetool.mymilwaukee.view.MTTextView;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/12/14.
 */
public class MainActivity extends MTActivity {

    private static final String TAG = makeLogTag(CreateAccountActivity.class);

    private MTTextView mVersionTypeDistributionTextView;
    private MTButton mLogOutButton;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MiscUtils.checkForUpdates(this);
    }

    //@Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_main);
        this.setupView();
    }

    protected void setupView() {
//        mVersionTypeDistributionTextView = (MTTextView) findViewById(R.id.versionTypeDistributionTextView);
//
//        if (MTConfig.isExternalRelease()) {
//            // Hide the version label
//            mVersionTypeDistributionTextView.setVisibility(View.INVISIBLE);
//        } else {
//            mVersionTypeDistributionTextView.setVisibility(View.VISIBLE);
//            mVersionTypeDistributionTextView.setText(MTConfig.getDistributionTypeVersionString());
//        }
//
//        mLogOutButton = (MTButton) findViewById(R.id.logOutButton);
//        mLogOutButton.setOnTouchListener(new MTTouchListener(this) {
//            @Override
//            public void didTapView(MotionEvent event) {
//                MTUtils.clearLoginInfo();
//
//                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.mainActivityPager);
        pager.setAdapter(new MainTabAdapter(getFragmentManager()));


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.mainActivityTabs);
        tabs.setViewPager(pager);
        tabs.setUnderlineColorResource(R.color.mt_red);
        tabs.setIndicatorColorResource(R.color.mt_red);
        tabs.setTextColorResource(R.color.mt_black);
        tabs.setDividerColorResource(R.color.mt_table_group_gray);
        tabs.setTabBackground(MiscUtils.getAppResources().getColor(R.color.mt_white));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MiscUtils.checkForCrashes(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    public class MainTabAdapter extends android.support.v13.app.FragmentPagerAdapter {

        private final String[] TITLES = { "Inventory", "Nearby Devices", "Settings" };

        public MainTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new Fragment();
                    break;

                case 1:
                    fragment = new Fragment();
                    break;

                case 2:
                    fragment = SettingsFragment.newInstance(position);
                    break;
                default:
                    fragment = new Fragment();
                    break;
            }
            return fragment;
        }

    }
}