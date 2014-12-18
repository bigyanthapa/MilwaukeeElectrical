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

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.activity.AddItemDetailActivity;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.model.MTSection;
import com.milwaukeetool.mymilwaukee.model.MTUserItem;
import com.milwaukeetool.mymilwaukee.model.event.MTAddItemEvent;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;
import com.milwaukeetool.mymilwaukee.services.MTUserItemHelper;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NamedObject;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTListItemHeaderView;
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
    private InventoryItemAdapter mAdapter;

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

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
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
    public void onDestroyView() {
        super.onDestroyView();
        LOGD(TAG, "View was destroyed, need to request my inventory");
        mLoadedInventory = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mInflater = inflater;//LayoutInflater.from(this.getActivity());

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

        mAdapter = new InventoryItemAdapter(this.getActivity(), null);

        if (mItemListView != null) {
            mItemListView.setAdapter(mAdapter);
        }

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
                mActivity.getProgressView().stopProgress();
                mAdapter.setListItems(result);
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

        boolean hasItems = mAdapter.hasUserItems();

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

        private ArrayList<NamedObject> _listItems = null;

        public InventoryItemAdapter(Context context, ArrayList<NamedObject> listItems) {

            if (listItems != null) {
                _listItems = listItems;
            } else {
                _listItems = new ArrayList<NamedObject>();
            }
        }

        @Override
        public int getCount() {
            if (_listItems == null) {
                return 0;
            }
            return _listItems.size();
        }

        @Override
        public Object getItem(int position) {
            if (_listItems == null) {
                return null;
            }
            return _listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            NamedObject namedObject = _listItems.get(position);

            LOGD(TAG, "Get View for type: " + namedObject.name + " at index: " + position);

            if(convertView == null) {
                if (MTUserItemHelper.isSection(namedObject) && (namedObject.object instanceof MTSection)) {
                    view = createNewHeaderView(namedObject);
                    updateHeader(namedObject, view);

                } else if (MTUserItemHelper.isUserItem(namedObject) && (namedObject.object instanceof MTUserItem)) {
                    view = createNewItemView(namedObject, parent);
                    updateItem(namedObject, view);
                }
            } else {

                if (MTUserItemHelper.isSection(namedObject) && (namedObject.object instanceof MTSection)) {

                    if (view != null && view instanceof MTListItemHeaderView) {
                        view = convertView;
                    } else {
                        view = createNewHeaderView(namedObject);
                    }

                    updateHeader(namedObject, view);

                } else if (MTUserItemHelper.isUserItem(namedObject) && (namedObject.object instanceof MTUserItem)) {

                    if (view != null && !(view instanceof MTListItemHeaderView)) {
                        view = convertView;
                    } else {
                        view = createNewItemView(namedObject, parent);
                    }

                    updateItem(namedObject, view);
                }
            }

            if (view == null) {
                view = new View(InventoryFragment.this.getActivity());
            }

            return view;
        }

        public View createNewHeaderView(NamedObject namedObject) {
            MTSection section = (MTSection)namedObject.object;

            View view = new MTListItemHeaderView(InventoryFragment.this.getActivity());
            ((MTListItemHeaderView)view).setMargins(0, 0, 0, UIUtils.getPixels(0));

            return view;
        }

        public View createNewItemView(NamedObject namedObject, ViewGroup parent) {

            View view = mInflater.inflate(R.layout.view_item_search_result_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView)view.findViewById(R.id.itemImageView);
            holder.descriptionTextView = (TextView)view.findViewById(R.id.itemDescriptionTextView);
            holder.modelNumberTextView = (TextView)view.findViewById(R.id.itemModelNumberTextView);
            holder.actionImageView = (ImageView)view.findViewById(R.id.addItemImageView);
            view.setTag(holder);


            final IconDrawable arrowRight = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                    Iconify.IconValue.fa_angle_right).colorRes(R.color.mt_common_gray).sizeDp(25);
            holder.actionImageView.setImageDrawable(arrowRight);

            return view;
        }

        public void updateItem(NamedObject namedObject, View view) {

            ViewHolder holder = (ViewHolder)view.getTag();

            MTUserItem userItem = (MTUserItem)namedObject.object;

            if (userItem != null && holder != null) {
                Picasso.with(InventoryFragment.this.getActivity())
                        .load(MTConstants.HTTP_PREFIX + userItem.getImageUrl())
                        .placeholder(R.drawable.ic_mkeplaceholder)
                        .error(R.drawable.ic_mkeplaceholder)
                        .into(holder.thumbnailImageView);

                holder.descriptionTextView.setText(userItem.getItemDescription());
                holder.modelNumberTextView.setText(userItem.getModelNumber());
            }
        }

        public void updateHeader(NamedObject namedObject, View view) {
            MTSection section = (MTSection)namedObject.object;

            if (view instanceof MTListItemHeaderView) {
                ((MTListItemHeaderView)view).setHeader(section.getTitle());
            }
        }

        private class ViewHolder {
            public ImageView thumbnailImageView;
            public TextView descriptionTextView;
            public TextView modelNumberTextView;
            public ImageView actionImageView;
        }

        public void clearListItems() {
            mLastResponse = null;
            _listItems.clear();
            notifyDataSetChanged();
        }

        public void setListItems(MTUserItemResponse response) {
            mLastResponse = response;
            _listItems = MTUserItemHelper.getAllListItemsForResponse(response);
            notifyDataSetChanged();
        }

        public ArrayList<NamedObject> getListItems() {
            return _listItems;
        }

        public boolean hasUserItems() {
            if (mLastResponse == null) {
                return false;
            }

            return mLastResponse.isEmpty();
        }
    }
}
