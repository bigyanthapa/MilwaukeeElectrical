package com.milwaukeetool.mymilwaukee.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.activity.AddItemDetailActivity;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.interfaces.FirstPageFragmentListener;
import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;
import com.milwaukeetool.mymilwaukee.model.event.MTSearchResultEvent;
import com.milwaukeetool.mymilwaukee.services.MTInventoryHelper;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view_custom.MTMilwaukeeItemView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/2/2014.
 */
@SuppressLint("ValidFragment")
public class ItemSearchResultsFragment extends MTFragment {
    private static final String TAG = makeLogTag(ItemSearchResultsFragment.class);

    private int position;

    private static final String ARG_POSITION = "position";

    static FirstPageFragmentListener mFirstPageListener;

    private MenuItem mSearchMenuItem;
    private AddItemActivity mAddItemActivity;
    private ListView mListView;
    private ItemSearchResultsAdapter mAdapter;
    private MTSearchResultEvent mLastSearchResultEvent = null;
    private MTMilwaukeeItemView mMilwaukeeItemView = null;
    private RelativeLayout mNoItemsLayout = null;

    private int mVisibleThreshold = 5;
    private boolean mLoadingSearchResults = false;
    private SearchView mSearchView = null;

    private View mListFooterView = null;
    private View mListFooterLoadingView = null;
    private View mListFooterEmptyView = null;
    private LayoutInflater mInflater;


    public static ItemSearchResultsFragment newInstance(int position, FirstPageFragmentListener listener) {
        ItemSearchResultsFragment f = new ItemSearchResultsFragment(listener);
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    public ItemSearchResultsFragment(FirstPageFragmentListener listener) {
        mFirstPageListener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof AddItemActivity) {
            mAddItemActivity = (AddItemActivity)activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_search_results, container, false);

        mInflater = LayoutInflater.from(this.getActivity());

        mMilwaukeeItemView = (MTMilwaukeeItemView)rootView.findViewById(R.id.itemSearchResultsMilwaukeeView);
        mNoItemsLayout = (RelativeLayout)rootView.findViewById(R.id.itemSearchResultsNoItemsLayout);

        mListView = (ListView)rootView.findViewById(R.id.itemSearchResultsListView);

        // Preload the footer views for the list
        mListFooterLoadingView = mInflater.inflate(R.layout.view_footer_load_item, null, false);

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        mListFooterEmptyView = new View(this.getActivity());
        mListFooterEmptyView.setLayoutParams(lp);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                boolean shouldRequestMoreItems = true;

                LOGD(TAG, "****SCROLL CHANGED");

                //int visibleThreshold = ItemSearchResultsFragment.this.determineNumberOfItemsToDisplay();

                int totalItemCount = 0;
                if (mAdapter.getSearchResults() != null) {
                    totalItemCount = mAdapter.getSearchResults().size();
                }

                if (mLastSearchResultEvent == null) {
                    LOGD(TAG, "****No last search result found");
                    shouldRequestMoreItems = false;
                }

                if (totalItemCount <= mVisibleThreshold || mLastSearchResultEvent == null) {
                    LOGD(TAG, "Total item count is <= visible threshold:" + totalItemCount);
                    shouldRequestMoreItems = false;
                }

                int currentPosition = mListView.getLastVisiblePosition();
                LOGD (TAG, "****IS CURRENT WINDOW: " + (currentPosition + mVisibleThreshold + MTInventoryHelper.INVENTORY_BUFFER_SIZE) + " BEYOND TOTAL INDEX: " + (totalItemCount - 1));

                if (shouldRequestMoreItems && (currentPosition + mVisibleThreshold + MTInventoryHelper.INVENTORY_BUFFER_SIZE) >= (totalItemCount - 1)) {

                    LOGD(TAG, "****Last search result count: " + mLastSearchResultEvent.getLastSearchResultCount());

                    // Request if we received the max number of items back
                    if (mLastSearchResultEvent.getLastSearchResultCount() == MTInventoryHelper.INVENTORY_ITEM_REQUEST_COUNT) {
                        int newSkipCount = (mAdapter.getSearchResults() != null) ? mAdapter.getSearchResults().size() : 0;
                        LOGD(TAG, "****New skip count: " + newSkipCount + " for term: " + mLastSearchResultEvent.getLastSearchTerm());
                        mLoadingSearchResults = true;
                        MTInventoryHelper.sharedInstance().searchForResults(mLastSearchResultEvent.getLastSearchTerm(),
                                newSkipCount, false, (AddItemActivity)ItemSearchResultsFragment.this.getActivity());
                        mLastSearchResultEvent = null;
                    } else {
                        LOGD(TAG, "****Resetting for empty result");
                        mLoadingSearchResults = false;
                        mLastSearchResultEvent = null;
                        shouldRequestMoreItems = false;
                    }
                }

                if (shouldRequestMoreItems) {
                    showLoadingFooterView(mListView, mLoadingSearchResults);
                }
            }
        });

        mAdapter = new ItemSearchResultsAdapter(this.getActivity(), null);

        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOGD(TAG, "Clicked list item at position: " + position);

                ArrayList<MTItemSearchResult> searchResults = mAdapter.getSearchResults();
                MTItemSearchResult result = null;
                if (searchResults != null && position < searchResults.size()) {
                    result = searchResults.get(position);
                }

                if (result != null) {
                    Activity activity = ItemSearchResultsFragment.this.getActivity();
                    Intent intent = new Intent(activity, AddItemDetailActivity.class);
                    intent.putExtra(MTConstants.SEARCH_ITEM_RESULT, result);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Determine if the results should be shown
        showSearchResults(mAdapter.hasSearchResults() || mLastSearchResultEvent != null);

        mVisibleThreshold = determineNumberOfItemsToDisplay();
        LOGD(TAG, "Number of items in threshold: " + mVisibleThreshold);

        // Hide the keyboard
        UIUtils.hideKeyboard(this.getActivity());

        setupSearchView();
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return MiscUtils.getString(R.string.mt_screen_name_item_search_results);
    }

    private class ItemSearchResultsAdapter extends BaseAdapter {

        private ArrayList<MTItemSearchResult> mSearchResults;

        public ItemSearchResultsAdapter(Context context, ArrayList<MTItemSearchResult> searchResults) {

            if (searchResults != null) {
                mSearchResults = searchResults;
            } else {
                mSearchResults = new ArrayList<MTItemSearchResult>();
            }
        }

        @Override
        public int getCount() {
            return mSearchResults.size();
        }

        @Override
        public Object getItem(int position) {
            return mSearchResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if(convertView == null) {
                view = mInflater.inflate(R.layout.view_list_item_inventory_item, parent, false);
                holder = new ViewHolder();
                holder.thumbnail = (ImageView)view.findViewById(R.id.itemImageView);
                holder.description = (TextView)view.findViewById(R.id.itemDescriptionTextView);
                holder.modelNumber = (TextView)view.findViewById(R.id.itemModelNumberTextView);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder)view.getTag();
            }

            MTItemSearchResult searchResult = mSearchResults.get(position);

            Picasso.with(ItemSearchResultsFragment.this.getActivity())
                    .load(MTConstants.HTTP_PREFIX + searchResult.getImageUrl())
                    .placeholder(R.drawable.ic_mkeplaceholder)
                    .error(R.drawable.ic_mkeplaceholder)
                    .into(holder.thumbnail);

            holder.description.setText(searchResult.getItemDescription());
            holder.modelNumber.setText(searchResult.getModelNumber());

            return view;
        }

        private class ViewHolder {
            public ImageView thumbnail;
            public TextView description;
            public TextView modelNumber;
        }

        public void clearSearchResults() {
            mSearchResults.clear();
            notifyDataSetChanged();
        }

        public void appendSearchResults(ArrayList<MTItemSearchResult> searchResults) {
            mSearchResults.addAll(searchResults);
            notifyDataSetChanged();
        }

        public ArrayList<MTItemSearchResult> getSearchResults() {
            return mSearchResults;
        }

        public boolean hasSearchResults() {
            return ((mSearchResults != null) && (mSearchResults.size() > 0));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_add_item_menu, menu);

        final ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_add_item));

        final SearchView searchView =
                (SearchView) menu.findItem(R.id.inventory_search).getActionView();

        mSearchView = searchView;

        mSearchMenuItem = menu.findItem(R.id.inventory_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

//                if (mLoadingSearchResults) {
//                    LOGD(TAG, "Already processing search request");
//                    return false;
//                }
                LOGD(TAG, "Submitting search query: " + s);

                // Hide the keyboard
                UIUtils.hideKeyboard(ItemSearchResultsFragment.this.getActivity());

                // Remove the search from actionbar
                //mSearchMenuItem.collapseActionView();

                // Clear previous search results
                mAdapter.clearSearchResults();

                // Make request to server
                if (mAddItemActivity != null && !TextUtils.isEmpty(s)) {
                    mLoadingSearchResults = true;
                    mAddItemActivity.performSearchRequest(s, 0);
                }

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

        setupSearchView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == mSearchMenuItem) {
            mSearchMenuItem.expandActionView();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backPressed() {
        mFirstPageListener.onSwitchToNextFragment();
    }


    public void onEvent(MTSearchResultEvent event) {
        if (event != null) {
            mLoadingSearchResults = false;
            showLoadingFooterView(mListView, mLoadingSearchResults);
            ArrayList<MTItemSearchResult> searchResults = event.getSearchResults();
            if (searchResults == null) {
                LOGD(TAG, "No more search results");
                mLastSearchResultEvent = null;
                showSearchResults(false);
            } else {
                LOGD(TAG, "Search Result Count:" + searchResults.size());
                mAdapter.appendSearchResults(searchResults);
                mLastSearchResultEvent = event;
                showSearchResults(true);
            }
        }
    }


    private void setupSearchView() {
        if (mSearchView != null) {
            LOGD(TAG, "Configuring search view");
            int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
            EditText searchEditText = (EditText) mSearchView.findViewById(searchSrcTextId);

            searchEditText.setText(mAddItemActivity.getLastSearchString());
            searchEditText.setTextColor(MiscUtils.getAppResources().getColor(R.color.mt_white));

            //searchEditText.setHintTextColor(MiscUtils.getAppResources().getColor(R.color.mt_very_light_gray));
            mSearchView.setQueryHint("Search Model #");
        }
    }

    private int determineNumberOfItemsToDisplay() {

        // Determine the height of the screen
        Point screenSize = UIUtils.getScreenSize(this.getActivity());

        int actionBarHeight = UIUtils.getActionBarHeight(this.getActivity());
        int statusBarHeight = UIUtils.getStatusBarHeight();
        int pagerTabStripHeight = UIUtils.getPixels(45);

        // Determine the top header space (actionbar +  pager tab strip + status bar)
        int remainingHeight = screenSize.y - (actionBarHeight + statusBarHeight + pagerTabStripHeight);

        // Determine the number of list items that can fit on the screen
        int listItemHeight = UIUtils.getPixels(80);

        int numberOfListItems = (remainingHeight / listItemHeight) + 1;

        LOGD(TAG, "Number of list items for search results: " + numberOfListItems);

        return numberOfListItems;
    }

    private void showSearchResults(boolean showSearchResults) {
        mMilwaukeeItemView.setVisibility(showSearchResults ? View.INVISIBLE : View.VISIBLE);
        if (mAdapter.getSearchResults() != null && mAdapter.getSearchResults().size() > 0) {
            mListView.setVisibility(showSearchResults ? View.VISIBLE : View.INVISIBLE);
            mNoItemsLayout.setVisibility(showSearchResults ? View.INVISIBLE : View.VISIBLE);
        } else {
            mNoItemsLayout.setVisibility(showSearchResults ? View.VISIBLE : View.INVISIBLE);
            mListView.setVisibility(showSearchResults ? View.INVISIBLE : View.VISIBLE);
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
}
