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
import android.widget.SearchView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.activity.InventorySearchActionActivity;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.activity.MainActivity;
import com.milwaukeetool.mymilwaukee.activity.MyInventoryFilterActivity;
import com.milwaukeetool.mymilwaukee.adapter.InventoryItemAdapter;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.manager.MyInventoryManager;
import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;
import com.milwaukeetool.mymilwaukee.model.event.MTChangeFilterEvent;
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
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    private RelativeLayout mNoItemsFoundLayout;
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
    //private boolean mHasInventory = false;

    public static InventoryFragment newInstance() {
        InventoryFragment f = new InventoryFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
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
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && !mInventoryLoaded) {
            retrieveInventory(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //checkForInventory(true);
//        if (!mInventoryLoaded) {
//            retrieveInventory(true);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();

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
        //mHasInventory = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LOGD(TAG, "View was destroyed, need to request my inventory");
        mInventoryLoaded = false;
        //mHasInventory = false;
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
        mNoItemsFoundLayout = (RelativeLayout)rootView.findViewById(R.id.inventoryNoItemsFoundLayout);

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

                if (mLastUserItemResultEvent == null) {
                    mLoadingResults = false;
                    return;
                }

                boolean shouldRequestMoreItems = true;

                LOGD(TAG, "****SCROLL CHANGED");

                int visibleThreshold = UIUtils.determineNumberOfItemsToDisplay(InventoryFragment.this.getActivity(),UIUtils.getPixels(80));

                int totalItemCount = 0;
                if (mUserItemManager != null) {
                    totalItemCount = mUserItemManager.getTotalNumberOfItems();
                }

                if (mUserItemManager.isEndOfResults()) {
                    LOGD(TAG, "****No last search result found");
                    shouldRequestMoreItems = false;
                    mLoadingResults = false;
                    mLastUserItemResultEvent = null;
                    LOGD(TAG, "****Resetting for empty result");
                    showLoadingFooterView(mItemListView, false);
                }

                if (totalItemCount <= visibleThreshold) {
                    LOGD(TAG, "Total item count is <= visible threshold:" + totalItemCount);
                    shouldRequestMoreItems = false;
                }

                int currentPosition = mItemListView.getLastVisiblePosition();
                LOGD (TAG, "****IS CURRENT WINDOW: " + (currentPosition + visibleThreshold + MTInventoryHelper.INVENTORY_BUFFER_SIZE) + " BEYOND TOTAL INDEX: " + (totalItemCount - 1));

                if (shouldRequestMoreItems && !mLoadingResults &&
                        (currentPosition + visibleThreshold + MTInventoryHelper.INVENTORY_BUFFER_SIZE) >= (totalItemCount - 1)) {

                    LOGD(TAG, "****Last result count: " + mLastUserItemResultEvent.getLastResultCount());

                    int newSkipCount = (mAdapter != null) ?  mUserItemManager.getTotalNumberOfItems() : 0;
                    LOGD(TAG, "****New skip count: " + newSkipCount + " for term: " + mLastUserItemResultEvent.getLastSearchTerm());
                    mLoadingResults = true;

                    MyInventoryManager manager = MyInventoryManager.sharedInstance();
                    updateInventory(manager.getInventoryFilterType(),
                            manager.getCurrentCategory(),
                            manager.getCurrentManufacturer(),
                            newSkipCount);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        MyInventoryManager inventoryManager = MyInventoryManager.sharedInstance();

        if (mActivity != null && mActivity.getProgressView() != null && mActivity.getProgressView().isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.actionFilter:
                Intent intent = new Intent(this.getActivity(), MyInventoryFilterActivity.class);
                intent.putExtra(MTConstants.INTENT_EXTRA_INVENTORY_FILTER_TYPE, inventoryManager.getInventoryFilterType());
                intent.putExtra(MTConstants.INTENT_EXTRA_CATEGORY, inventoryManager.getCurrentCategory());
                intent.putExtra(MTConstants.INTENT_EXTRA_MANUFACTURER, inventoryManager.getCurrentManufacturer());
                startActivity(intent);
                break;
            case R.id.actionAdd:
                this.startAddItemActivity();
                break;
            case R.id.actionRefresh:
                //mInventoryLoaded = false;
                //mHasInventory = false;
                //checkForInventory(true);
                retrieveInventory(true);
                break;
        }
        return true;
    }

    protected void startInventorySearchResultsActivity() {
        Intent intent = new Intent(this.getActivity(), InventorySearchActionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_menu, menu);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_inventory_title));

        final SearchView searchView =
                (SearchView) menu.findItem(R.id.actionSearch).getActionView();

        mSearchView = searchView;
        mSearchMenuItem = menu.findItem(R.id.actionSearch);

        searchView.setQueryHint(MiscUtils.getString(R.string.search_inventory_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                LOGD(TAG, "Submitting search query: " + s);

                Activity parentActivity = InventoryFragment.this.getActivity();

                // Hide the keyboard
                UIUtils.hideKeyboard(parentActivity);

                Intent intent = new Intent(parentActivity, InventorySearchActionActivity.class);
                intent.setAction(MTConstants.INVENTORY_SEARCH_ACTION);
                intent.putExtra(MTConstants.INVENTORY_SEARCH_QUERY, s);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }

        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Remove the search from actionbar
                mSearchMenuItem.collapseActionView();
                return false;
            }
        });
    }

    public void onEvent(MTUserItemResultEvent event) {

//        if (event != null && event.isSingleRequest()) {
//            mInventoryLoaded = true;
//            if (event.getUserItemResponse() != null) {
//                mHasInventory = !(event.getUserItemResponse().isEmpty());
//                // Now retrieve the user's current inventory
//                retrieveInventory(true);
//            }
//        } else
        if (event != null) {
            mLoadingResults = false;
            if (event.getLastResultCount() > 0) {
                mLastUserItemResultEvent = event;
                loadInventory();
                showLoadingFooterView(mItemListView,false);
            } else if (mUserItemManager.getTotalNumberOfItems() == 0) {
                mLastUserItemResultEvent = event;
                loadInventory();
                showLoadingFooterView(mItemListView,false);
            }
        }
    }

    public void onEvent(MTRefreshInventoryEvent event) {

        if (event != null && event.originatedFrom instanceof MainActivity) {
            if (mInventoryLayout != null) {
                mInventoryLayout.setRefreshing(false);
            }
            //mInventoryLoaded = false;
            //mHasInventory = false;
            //checkForInventory(true);
            retrieveInventory(true);
        }
    }

    public void onEvent(MTChangeFilterEvent event) {
        if (event != null) {
            updateInventory(event.getFilterType(), event.getCategory(), event.getManufacturer());
        }
    }

//    public void checkForInventory(boolean recheck) {
//        if (!mInventoryLoaded || (recheck && (mUserItemManager != null && (mUserItemManager.isDirty())))) {
//            if (mUserItemManager != null) {
//                mUserItemManager.getItemsSingleRequest((MTActivity) InventoryFragment.this.getActivity(), true);
//            }
//        }
//    }

    public void retrieveInventory(boolean refresh) {
        if (mUserItemManager != null && (mUserItemManager.isDirty() || refresh)) {

            MyInventoryManager manager = MyInventoryManager.sharedInstance();

            updateInventory(manager.getInventoryFilterType(),
                    manager.getCurrentCategory(),
                    manager.getCurrentManufacturer());
        }
    }

    public void updateInventory(MyInventoryManager.MyInventoryFilterType inventoryFilterType,
                                MTCategory category, MTManufacturer manufacturer, int skipCount) {

        // Show progress only for initial updates
        boolean showProgress = (skipCount > 0) ? false : true;

        switch (inventoryFilterType) {
            case FILTER_TYPE_BY_CATEGORY:
                if (category != null) {
                    // reload we have an id, and previously didn't
                    if (mUserItemManager != null) {
                        mUserItemManager.getItems((MTActivity) InventoryFragment.this.getActivity(), skipCount, showProgress,
                                category.getId(),
                                MTUserItemManager.UserItemFilterType.FILTER_TYPE_CATEGORY);
                    }
                }
                break;

            case FILTER_TYPE_BY_MANUFACTURER:
                if (manufacturer != null) {
                    // reload we have an id, and previously didn't
                    if (mUserItemManager != null) {
                        mUserItemManager.getItems((MTActivity) InventoryFragment.this.getActivity(), skipCount, showProgress,
                                manufacturer.getId(),
                                MTUserItemManager.UserItemFilterType.FILTER_TYPE_MANUFACTURER);
                    }
                }
                break;
            case FILTER_TYPE_ALL_INVENTORY:
            case FILTER_TYPE_DEFAULT:
            default:
                // We need to reload id's are different
                if (mUserItemManager != null) {
                    mUserItemManager.getItems((MTActivity) InventoryFragment.this.getActivity(), skipCount, showProgress);
                }
                break;
        }

        mInventoryLoaded = true;
    }

    public void updateInventory(MyInventoryManager.MyInventoryFilterType inventoryFilterType,
                                    MTCategory category, MTManufacturer manufacturer) {
        updateInventory(inventoryFilterType, category, manufacturer, 0);
    }

    public void loadInventory() {
        if (mAdapter != null) {
            mAdapter.updateListItems(mUserItemManager);
            mItemListView.setSelection(0);
            updateView();
        }
    }

    public void updateView() {

        if (mUserItemManager != null) {

            MyInventoryManager inventoryManager = MyInventoryManager.sharedInstance();

            boolean hasItems = (mUserItemManager.getTotalNumberOfItems() > 0);

            boolean filtered = (inventoryManager.getInventoryFilterType() ==
                    MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_BY_CATEGORY ||
                    inventoryManager.getInventoryFilterType() ==
                            MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_BY_MANUFACTURER
            );

//            if (filtered && mHasInventory) {
//                this.mNoInventoryLayout.setVisibility(View.INVISIBLE);
//                this.mNoItemsFoundLayout.setVisibility(hasItems ? View.INVISIBLE : View.VISIBLE);
//            } else {
//                this.mNoItemsFoundLayout.setVisibility(View.INVISIBLE);
//                this.mNoInventoryLayout.setVisibility(mHasInventory ? View.INVISIBLE : View.VISIBLE);
//            }
//
//            this.mInventoryLayout.setVisibility((mHasInventory && hasItems) ? View.VISIBLE : View.INVISIBLE);

            if (filtered) {
                this.mNoInventoryLayout.setVisibility(View.INVISIBLE);
                this.mNoItemsFoundLayout.setVisibility(hasItems ? View.INVISIBLE : View.VISIBLE);
            } else {
                this.mNoItemsFoundLayout.setVisibility(View.INVISIBLE);
                this.mNoInventoryLayout.setVisibility(hasItems ? View.INVISIBLE : View.VISIBLE);
            }

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
