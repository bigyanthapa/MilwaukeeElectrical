package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.interfaces.FirstPageFragmentListener;
import com.milwaukeetool.mymilwaukee.model.event.MTSearchResultEvent;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/2/14.
 */
public class ItemSearchResultsFragment extends MTFragment {
    private static final String TAG = makeLogTag(ItemSearchResultsFragment.class);

    private static final String ARG_POSITION = "position";

    private int position;

    static FirstPageFragmentListener mFirstPageListener;

    private MenuItem mSearchMenuItem;

    private AddItemActivity mAddItemActivity;

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

    public static ItemSearchResultsFragment newInstance(int position, FirstPageFragmentListener listener) {
        ItemSearchResultsFragment f = new ItemSearchResultsFragment(listener);
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        return rootView;

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
            } else {
                LOGD(TAG, "Search Result Count:" + event.getSearchResults().size());
            }
        }
    }
}
