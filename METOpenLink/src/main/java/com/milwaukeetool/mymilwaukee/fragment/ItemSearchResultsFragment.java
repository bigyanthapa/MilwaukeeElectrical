package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.interfaces.FirstPageFragmentListener;
import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;
import com.milwaukeetool.mymilwaukee.model.event.MTSearchResultEvent;
import com.milwaukeetool.mymilwaukee.services.MTInventoryHelper;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/2/2014.
 */
public class ItemSearchResultsFragment extends MTFragment {
    private static final String TAG = makeLogTag(ItemSearchResultsFragment.class);

    private int position;

    static FirstPageFragmentListener mFirstPageListener;
    private MenuItem mSearchMenuItem;

    private static final String ARG_POSITION = "position";

    private ListView mListView;

    private AddItemActivity mAddItemActivity;

    private ItemSearchResultsAdapter mAdapter;

    private int visibleThreshold = MTInventoryHelper.INVENTORY_REQUEST_BUFFER;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    private MTSearchResultEvent mLastSearchResultEvent = null;

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
        View rootView = inflater.inflate(R.layout.fragment_inventory_results, container, false);

        mListView = (ListView)rootView.findViewById(R.id.inventory_results);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        currentPage++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // I load the next page of gigs using a background task,
                    // but you can call any function here.
                    if (mLastSearchResultEvent != null) {

                        if (mLastSearchResultEvent.getLastSearchResultCount() == MTInventoryHelper.INVENTORY_ITEM_REQUEST_COUNT) {
                            int newSkipCount = mLastSearchResultEvent.getLastSkipCount() + mLastSearchResultEvent.getLastSearchResultCount();

                            MTInventoryHelper.sharedInstance().searchForResults(mLastSearchResultEvent.getLastSearchTerm(),
                                    newSkipCount, false, (AddItemActivity)ItemSearchResultsFragment.this.getActivity());
                        }
                    }
                    loading = true;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

        mAdapter = new ItemSearchResultsAdapter(this.getActivity(), null);

        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }

        return rootView;
    }

    private class ItemSearchResultsAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ArrayList<MTItemSearchResult> mSearchResults;

        public ItemSearchResultsAdapter(Context context, ArrayList<MTItemSearchResult> searchResults) {
            mInflater = LayoutInflater.from(context);

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
                view = mInflater.inflate(R.layout.view_item_search_result_list_item, parent, false);
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
                    .load("http:" + searchResult.getImageUrl())
                    //.placeholder(R.drawable.user_placeholder)
                    //.error(R.drawable.user_placeholder_error)
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_add_item_menu, menu);

        final ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_add_item_title));

        final SearchView searchView =
                (SearchView) menu.findItem(R.id.inventory_search).getActionView();

        mSearchMenuItem = menu.findItem(R.id.inventory_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                LOGD(TAG, "Submitting search query: " + s);

                // Hide the keyboard
                UIUtils.hideKeyboard(ItemSearchResultsFragment.this.getActivity());

                // Remove the search from actionbar
                mSearchMenuItem.collapseActionView();

                // Make request to server
                if (mAddItemActivity != null && !TextUtils.isEmpty(s)) {
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
            if (event.getSearchResults() == null) {
                LOGD(TAG, "No search results");
                mLastSearchResultEvent = null;
            } else {
                LOGD(TAG, "Search Result Count:" + event.getSearchResults().size());
                mAdapter.appendSearchResults(event.getSearchResults());
                mLastSearchResultEvent = event;
            }
        }
    }

    public int determineNumberOfItemsToDisplay() {

        // Determine the height of the screen
        Point screenSize = UIUtils.getScreenSize(this.getActivity());

        int actionBarHeight = UIUtils.getActionBarHeight(this.getActivity());
        int statusBarHeight = UIUtils.getStatusBarHeight();
        int pagerTabStripHeight = UIUtils.getPixels(45);

        // Determine the top header space (actionbar +  pager tab strip + status bar)
        int remainingHeight = screenSize.y - (actionBarHeight + statusBarHeight + pagerTabStripHeight);

        // Determine the number of list items that can fit on the screen
        int listItemHeight = UIUtils.getPixels(80);

        int numberOfListItems = remainingHeight / listItemHeight + 1;

        return numberOfListItems;
    }


}
