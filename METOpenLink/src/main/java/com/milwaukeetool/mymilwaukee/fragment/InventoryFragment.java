package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.activity.AddItemDetailActivity;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;
import com.milwaukeetool.mymilwaukee.model.event.MTAddItemEvent;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/19/14.
 */
public class InventoryFragment extends MTFragment {

    private static final String TAG = makeLogTag(NearbyFragment.class);
    private static final String ARG_POSITION = "position";
    private MTActivity mActivity = null;
    private MTButton mAddInventoryBtn;

    private RelativeLayout mNoInventoryLayout;
    private RelativeLayout mInventoryLayout;

    private ListView mItemListView;

    private int position;

    private boolean mLoadedInventory = false;
    private MTUserItemResponse mLastResponse = null;

    private LayoutInflater mInflater;

    public static InventoryFragment newInstance(int position) {
        InventoryFragment f = new InventoryFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (UIUtils.isViewVisible(this.getView())) {
            loadUserItems(false);
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
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            LOGD(TAG, "Fragment menu is visible");
            loadUserItems(false);
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
        mInventoryLayout = (RelativeLayout)rootView.findViewById(R.id.inventoryNormalLayout);

        mInventoryLayout.setVisibility(View.INVISIBLE);
        mNoInventoryLayout.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mActivity != null && mActivity.getProgressView() != null && mActivity.getProgressView().isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.actionSearch:
                this.startAddItemActivity();
                break;
            case R.id.actionRefresh:
                this.loadUserItems(true);
                break;
        }
        return true;
    }

    protected void startAddItemActivity() {
        Intent intent = new Intent(this.getActivity(), AddItemActivity.class);
        startActivity(intent);
    }

    protected void loadUserItems(boolean refresh) {

        if (mLoadedInventory && !refresh) {
            updateView();
            return;
        }

        Callback<MTUserItemResponse> responseCallback = new Callback<MTUserItemResponse>() {
            @Override
            public void success(MTUserItemResponse result, Response response) {
                mLoadedInventory = true;
                mLastResponse = result;
                mActivity.getProgressView().stopProgress();
                InventoryFragment.this.updateView();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mActivity.getProgressView().stopProgress();

                MTUtils.handleRetrofitError(retrofitError, InventoryFragment.this.getActivity(),
                        MiscUtils.getString(R.string.dialog_title_retrieve_inventory_failure));
            }
        };

        // Start progress before making web service call
        if (mActivity != null) {
            mActivity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_default_message));
        }

        MTWebInterface.sharedInstance().getUserService().getItems(
                MTUtils.getAuthHeaderForBearerToken(),
                responseCallback);
    }

    public void updateView() {

        boolean hasItems = mLastResponse.isEmpty();

        // Update both layouts always
        this.mNoInventoryLayout.setVisibility(hasItems ? View.INVISIBLE : View.VISIBLE);
        this.mInventoryLayout.setVisibility(hasItems ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_menu, menu);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_inventory_title));
    }

    public void onEvent(MTAddItemEvent event) {
        if (event.originatedFrom instanceof AddItemDetailActivity) {
            loadUserItems(true);
        }
    }

    private class InventoryItemAdapter extends BaseAdapter {

        private ArrayList<MTItemSearchResult> mSearchResults;

        public InventoryItemAdapter(Context context, ArrayList<MTItemSearchResult> searchResults) {

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

            Picasso.with(InventoryFragment.this.getActivity())
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
}
