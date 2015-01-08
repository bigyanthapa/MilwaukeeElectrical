package com.milwaukeetool.mymilwaukee.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.adapter.InventoryItemAdapter;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.manager.MyInventoryManager;
import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;
import com.milwaukeetool.mymilwaukee.model.event.MTUserItemResultEvent;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;
import com.milwaukeetool.mymilwaukee.services.MTInventoryHelper;
import com.milwaukeetool.mymilwaukee.manager.MTUserItemManager;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/19/2014.
 */
public class InventorySearchActionActivity extends MTActivity {

    private static final String TAG = makeLogTag(InventorySearchActionActivity.class);
    private MTUserItemResponse mLastResponse = null;
    private ListView mSearchResultsListView;
    private InventoryItemAdapter mAdapter;
    private MTUserItemManager mUserItemManager;
    private RelativeLayout mNoInventoryLayout;
    private MTUserItemResultEvent mLastUserItemResultEvent;
    private boolean mLoadingResults;
    private View mListFooterView;
    private View mListFooterLoadingView ;
    private View mListFooterEmptyView;

    private String mLastSearchTerm;

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_inventory_search_action);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLoadingResults = false;
        this.mUserItemManager = new MTUserItemManager();
        this.mSearchResultsListView = (ListView) this.findViewById(R.id.inventorySearchResultsListView);
        this.mNoInventoryLayout = (RelativeLayout) this.findViewById(R.id.inventorySearchResultsNoInventoryLayout);
        this.mNoInventoryLayout.setVisibility(View.INVISIBLE);

        mAdapter = new InventoryItemAdapter(this, null);

        if (this.mSearchResultsListView != null) {
            this.mSearchResultsListView.setAdapter(mAdapter);
        }

        mListFooterLoadingView = this.getLayoutInflater().inflate(R.layout.view_footer_load_item, null, false);

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        mListFooterEmptyView = new View(this);
        mListFooterEmptyView.setLayoutParams(lp);

        this.mSearchResultsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                int visibleThreshold = UIUtils.determineNumberOfItemsToDisplay(InventorySearchActionActivity.this, UIUtils.getPixels(80));

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
                    showLoadingFooterView(mSearchResultsListView, false);
                }

                if (totalItemCount <= visibleThreshold) {
                    LOGD(TAG, "Total item count is <= visible threshold:" + totalItemCount);
                    shouldRequestMoreItems = false;
                }

                int currentPosition = mSearchResultsListView.getLastVisiblePosition();
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
                            mLastSearchTerm,
                            newSkipCount);
                }

                if (shouldRequestMoreItems) {
                    showLoadingFooterView(mSearchResultsListView, mLoadingResults);
                }
            }
        });

        handleIntent(this.getIntent());
    }

    public void updateInventory(MyInventoryManager.MyInventoryFilterType inventoryFilterType,
                                MTCategory category, MTManufacturer manufacturer,
                                String searchTerm, int skipCount) {

        // Show progress only for initial updates
        boolean showProgress = (skipCount > 0) ? false : true;

        switch (inventoryFilterType) {
            case FILTER_TYPE_BY_CATEGORY:
                if (category != null) {
                    // reload we have an id, and previously didn't
                    if (mUserItemManager != null) {
                        mUserItemManager.getItems((MTActivity) InventorySearchActionActivity.this,
                                skipCount, showProgress, category.getId(),
                                MTUserItemManager.UserItemFilterType.FILTER_TYPE_CATEGORY,
                                searchTerm);
                    }
                }
                break;

            case FILTER_TYPE_BY_MANUFACTURER:
                if (manufacturer != null) {
                    // reload we have an id, and previously didn't
                    if (mUserItemManager != null) {
                        mUserItemManager.getItems((MTActivity) InventorySearchActionActivity.this,
                                skipCount, showProgress, manufacturer.getId(),
                                MTUserItemManager.UserItemFilterType.FILTER_TYPE_MANUFACTURER,
                                searchTerm);
                    }
                }
                break;
            case FILTER_TYPE_ALL_INVENTORY:
            case FILTER_TYPE_DEFAULT:
            default:
                // We need to reload id's are different
                if (mUserItemManager != null) {
                    mUserItemManager.getItems((MTActivity) InventorySearchActionActivity.this,
                            skipCount, showProgress, searchTerm);
                }
                break;
        }
    }

    public void updateInventory(MyInventoryManager.MyInventoryFilterType inventoryFilterType,
                                MTCategory category, MTManufacturer manufacturer, String searchTerm) {
        updateInventory(inventoryFilterType, category, manufacturer, searchTerm, 0);
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

    private void handleIntent(Intent intent) {
        String action = intent.getAction();

        if (MTConstants.INVENTORY_SEARCH_ACTION.equals(action)) {
            String query = intent.getExtras().getString(MTConstants.INVENTORY_SEARCH_QUERY);

            mLastSearchTerm = query;

            if (!TextUtils.isEmpty(mLastSearchTerm)) {
                UIUtils.hideKeyboard(InventorySearchActionActivity.this);

                MyInventoryManager manager = MyInventoryManager.sharedInstance();
                updateInventory(manager.getInventoryFilterType(),
                        manager.getCurrentCategory(),
                        manager.getCurrentManufacturer(),
                        mLastSearchTerm);
            }
        }
    }

    public void onEvent(MTUserItemResultEvent event) {

        if (event != null) {
            mLoadingResults = false;
            if (event.getLastResultCount() > 0 || mUserItemManager.getTotalNumberOfItems() == 0) {
                mLastUserItemResultEvent = event;
                loadInventory();
                showLoadingFooterView(mSearchResultsListView, false);
            }
        }
    }

    public void loadInventory() {
        if (mAdapter != null) {
            mAdapter.updateListItems(mUserItemManager);
            //mSearchResultsListView.setSelection(0);
            updateView();
        }
    }

    public void updateView() {

        if (mUserItemManager != null) {
            boolean hasItems = (mUserItemManager.getTotalNumberOfItems() > 0);

            // Update both layouts always
            this.mNoInventoryLayout.setVisibility(hasItems ? View.INVISIBLE : View.VISIBLE);
            this.mSearchResultsListView.setVisibility(hasItems ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.inventory_search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionInventorySearch).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(MiscUtils.getString(R.string.search_inventory_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                mLastSearchTerm = s;

                if (!TextUtils.isEmpty(mLastSearchTerm)) {
                    UIUtils.hideKeyboard(InventorySearchActionActivity.this);

                    MyInventoryManager manager = MyInventoryManager.sharedInstance();
                    updateInventory(manager.getInventoryFilterType(),
                            manager.getCurrentCategory(),
                            manager.getCurrentManufacturer(),
                            mLastSearchTerm);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionCategoryAdd) {
            LOGD(TAG, "Adding a category");
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
