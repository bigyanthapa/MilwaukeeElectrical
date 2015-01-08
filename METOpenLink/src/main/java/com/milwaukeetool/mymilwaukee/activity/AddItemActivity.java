package com.milwaukeetool.mymilwaukee.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.fragment.ItemSearchResultsFragment;
import com.milwaukeetool.mymilwaukee.fragment.OtherItemFragment;
import com.milwaukeetool.mymilwaukee.interfaces.FirstPageFragmentListener;
import com.milwaukeetool.mymilwaukee.services.MTInventoryHelper;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.ZoomOutPageTransformer;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/25/2014.
 */
public class AddItemActivity extends MTActivity {
    private static final String TAG = makeLogTag(AddItemActivity.class);

    private Fragment mOtherFragment = null;

    private ViewPager mPager = null;
    private AddItemTabAdapter mAdapter = null;

    private String mLastSearchString = null;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return null;
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

        mAdapter = new AddItemTabAdapter(getFragmentManager());

        // Initialize the ViewPager and set an adapter
        mPager = (ViewPager) findViewById(R.id.mainActivityPager);
        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mPager.setPageMargin(pageMargin);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.mainActivityTabs);

//        Typeface arialBlackTypeface = Typefaces.get(this, "fonts/Arial Black.ttf");
//
//        // Must be set before remaining configuration!
//        tabs.setTypeface(arialBlackTypeface, Typeface.NORMAL);

        // Must set before setting view pager, expand to fill!
        tabs.setShouldExpand(true);

        tabs.setViewPager(mPager);
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

        if (mProgressView.isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AddItemTabAdapter extends android.support.v13.app.FragmentPagerAdapter {

        private final class FirstPageListener implements
                FirstPageFragmentListener {
            public void onSwitchToNextFragment() {
//                mFragmentManager.beginTransaction().remove(mFragmentAtPos0)
//                        .commit();
//                if (mFragmentAtPos0 instanceof MilwaukeeItemFragment){
//                    mFragmentAtPos0 = ItemSearchResultsFragment.newInstance(0, listener);
//                } else { // Instance of NextFragment
//                    mFragmentAtPos0 = MilwaukeeItemFragment.newInstance(0, listener);
//                }
//                notifyDataSetChanged();
            }
        }

        private final FragmentManager mFragmentManager;
        public Fragment mFragmentAtPos0 = null;

        FirstPageListener listener = new FirstPageListener();

        private final String[] TITLES = {
                MiscUtils.getString(R.string.main_title_milwaukee_item_title),
                MiscUtils.getString(R.string.main_title_other_item_title)
        };

        public AddItemTabAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
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
            switch (position) {
                case 0:

                    if (mFragmentAtPos0 == null)
                    {
                        mFragmentAtPos0 = ItemSearchResultsFragment.newInstance(position, listener);
                    }
                    return mFragmentAtPos0;

                case 1:
                    mOtherFragment = OtherItemFragment.newInstance(position);
                    return mOtherFragment;

                default:
                    return new Fragment();
            }
        }

        @Override
        public int getItemPosition(Object object)
        {
//            if (object instanceof MilwaukeeItemFragment &&
//                    mFragmentAtPos0 instanceof ItemSearchResultsFragment) {
//                return POSITION_NONE;
//            }
//            if (object instanceof ItemSearchResultsFragment &&
//                    mFragmentAtPos0 instanceof MilwaukeeItemFragment) {
//                return POSITION_NONE;
//            }
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public void onBackPressed() {

        if (mProgressView.isDisplayed()) {
            return;
        }

//        if(mPager.getCurrentItem() == 0) {
//            if (mAdapter.getItem(0) instanceof ItemSearchResultsFragment) {
//                //((ItemSearchResultsFragment) mAdapter.getItem(0)).backPressed();
//                super.onBackPressed();
//            }
//            else if (mAdapter.getItem(0) instanceof MilwaukeeItemFragment) {
//                super.onBackPressed();
//            }
//        }

        super.onBackPressed();
    }

    public void performSearchRequest(String searchTerm, int skipCount) {
        MTInventoryHelper.sharedInstance().searchForResults(searchTerm, skipCount, true, this);
        mLastSearchString = searchTerm;
    }

    public String getLastSearchString () {
        return mLastSearchString;
    }
}
