package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.activity.InventorySearchActivity;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.activity.MainActivity;
import com.milwaukeetool.mymilwaukee.adapter.InventoryItemAdapter;
import com.milwaukeetool.mymilwaukee.model.event.MTRefreshInventoryEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTUserItemResultEvent;
import com.milwaukeetool.mymilwaukee.services.MTInventoryHelper;
import com.milwaukeetool.mymilwaukee.services.MTUserItemManager;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/19/14.
 */
public class InventoryFragment extends MTFragment {

    private static final String TAG = makeLogTag(NearbyFragment.class);

    private LayoutInflater mInflater;

    private MTActivity mActivity = null;
    private MTButton mAddInventoryBtn;

    private RelativeLayout mNoInventoryLayout;
    private SwipeRefreshLayout mInventoryLayout;

    private ListView mItemListView;
    private InventoryItemAdapter mAdapter;
    private MTUserItemManager mUserItemManager = null;
    private MTUserItemResultEvent mLastUserItemResultEvent = null;

    private View mListFooterView = null;
    private View mListFooterLoadingView = null;
    private View mListFooterEmptyView = null;

    private boolean mLoadingResults = false;
    private boolean mInventoryLoaded = false;

    public static InventoryFragment newInstance() {
        InventoryFragment f = new InventoryFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mInventoryLoaded || (mUserItemManager != null && mUserItemManager.isDirty())) {
            retrieveInventory(true);
        }
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return MiscUtils.getString(R.string.mt_screen_name_inventory);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MTActivity) {
            mActivity = (MTActivity)activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

        mUserItemManager = new MTUserItemManager();
        mInventoryLoaded = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LOGD(TAG, "View was destroyed, need to request my inventory");
        mInventoryLoaded = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mInflater = inflater;

        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        mItemListView = (ListView)rootView.findViewById(R.id.inventoryItemListView);

        this.mAddInventoryBtn = (MTButton) rootView.findViewById(R.id.addInventoryBtn);
        this.mAddInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddItemActivity();
            }
        });

        // Pull back layouts to set visibility
        mNoInventoryLayout = (RelativeLayout)rootView.findViewById(R.id.inventoryEmptyLayout);
        mInventoryLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.inventoryNormalLayout);

        if (this.getActivity() instanceof MainActivity) {
            mInventoryLayout.setOnRefreshListener((MainActivity) this.getActivity());
        }

        mInventoryLayout.setVisibility(View.INVISIBLE);
        mNoInventoryLayout.setVisibility(View.INVISIBLE);

        // Preload the footer views for the list
        mListFooterLoadingView = mInflater.inflate(R.layout.view_footer_load_item, null, false);

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        mListFooterEmptyView = new View(this.getActivity());
        mListFooterEmptyView.setLayoutParams(lp);

        mItemListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                boolean shouldRequestMoreItems = true;

                LOGD(TAG, "****SCROLL CHANGED");

                int visibleThreshold = UIUtils.determineNumberOfItemsToDisplay(InventoryFragment.this.getActivity(),UIUtils.getPixels(80));

                int totalItemCount = 0;
                if (mUserItemManager != null) {
                    totalItemCount = mUserItemManager.getTotalNumberOfItems();
                }

                if (mUserItemManager.isEndOfResults() || mLastUserItemResultEvent == null) {
                    LOGD(TAG, "****No last search result found");
                    shouldRequestMoreItems = false;
                    mLoadingResults = false;
                    mLastUserItemResultEvent = null;
                    LOGD(TAG, "****Resetting for empty result");
                }

                if (totalItemCount <= visibleThreshold) {
                    LOGD(TAG, "Total item count is <= visible threshold:" + totalItemCount);
                    shouldRequestMoreItems = false;
                }

                int currentPosition = mItemListView.getLastVisiblePosition();
                LOGD (TAG, "****IS CURRENT WINDOW: " + (currentPosition + visibleThreshold + MTInventoryHelper.INVENTORY_BUFFER_SIZE) + " BEYOND TOTAL INDEX: " + (totalItemCount - 1));

                if (mLastUserItemResultEvent != null && shouldRequestMoreItems &&
                        (currentPosition + visibleThreshold + MTInventoryHelper.INVENTORY_BUFFER_SIZE) >= (totalItemCount - 1)) {

                    LOGD(TAG, "****Last result count: " + mLastUserItemResultEvent.getLastResultCount());

                    int newSkipCount = (mAdapter != null) ?  mUserItemManager.getTotalNumberOfItems() : 0;
                    LOGD(TAG, "****New skip count: " + newSkipCount + " for term: " + mLastUserItemResultEvent.getLastSearchTerm());
                    mLoadingResults = true;
                    mLastUserItemResultEvent = null;
                    mUserItemManager.getItems((MTActivity)InventoryFragment.this.getActivity(), newSkipCount, false);
                }

                if (shouldRequestMoreItems) {
                    showLoadingFooterView(mItemListView, mLoadingResults);
                }
            }
        });

        mAdapter = new InventoryItemAdapter(this.getActivity(), null);

        if (mItemListView != null) {
            mItemListView.setAdapter(mAdapter);
        }

        return rootView;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            LOGD(TAG, "Fragment menu is visible");
            retrieveInventory(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mActivity != null && mActivity.getProgressView() != null && mActivity.getProgressView().isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.actionAdd:
                this.startAddItemActivity();
                break;
            case R.id.actionRefresh:
                this.retrieveInventory(true);
                break;
            case R.id.actionSearch:
                this.startInventorySearchResultsActivity();
                break;
        }
        return true;
    }

    protected void startInventorySearchResultsActivity() {
        Intent intent = new Intent(this.getActivity(), InventorySearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_menu, menu);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_inventory_title));
    }

    public void onEvent(MTUserItemResultEvent event) {
        if (event.getLastResultCount() > 0) {
            mInventoryLoaded = true;
            mLastUserItemResultEvent = event;
            loadInventory();
            showLoadingFooterView(mItemListView, false);
        }
    }

    public void onEvent(MTRefreshInventoryEvent event) {

        if (event != null && event.originatedFrom instanceof MainActivity) {
            if (mInventoryLayout != null) {
                mInventoryLayout.setRefreshing(false);
            }
            retrieveInventory(true);
        }
    }

    public void retrieveInventory(boolean refresh) {
        if (mUserItemManager != null && (mUserItemManager.isDirty() || refresh)) {
            mUserItemManager.getItems((MTActivity) InventoryFragment.this.getActivity(), 0, true);
        }
    }

    public void loadInventory() {
        if (mAdapter != null) {
            mAdapter.clearListItems();
            mAdapter.updateListItems(mUserItemManager);
            updateView();
        }
    }

    public void updateView() {

        if (mUserItemManager != null) {
            boolean hasItems = (mUserItemManager.getTotalNumberOfItems() > 0);

            // Update both layouts always
            this.mNoInventoryLayout.setVisibility(hasItems ? View.INVISIBLE : View.VISIBLE);
            this.mInventoryLayout.setVisibility(hasItems ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void showLoadingFooterView(ListView listView, boolean isLoading) {

        if (mListFooterView == mListFooterLoadingView && isLoading) {
            return;
        }

        if (mListFooterView == null && !isLoading) {
            return;
        }

        if (mListFooterView != null && !isLoading) {
            listView.removeFooterView(mListFooterView);
            mListFooterView = null;
        }

        if (isLoading && mListFooterView == null) {
            mListFooterView = mListFooterLoadingView;
            listView.addFooterView(mListFooterView, null, false);
        }
    }

    protected void startAddItemActivity() {
        Intent intent = new Intent(this.getActivity(), AddItemActivity.class);
        startActivity(intent);
    }
}
