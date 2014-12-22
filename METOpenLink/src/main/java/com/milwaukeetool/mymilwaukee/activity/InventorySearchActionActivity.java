package com.milwaukeetool.mymilwaukee.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.model.MTSection;
import com.milwaukeetool.mymilwaukee.model.MTUserItem;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;
import com.milwaukeetool.mymilwaukee.services.MTUserItemHelper;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NamedObject;
import com.milwaukeetool.mymilwaukee.util.StringHelper;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTListItemHeaderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/19/2014.
 */
public class InventorySearchActionActivity extends MTActivity {

    private static final String TAG = makeLogTag(InventorySearchActionActivity.class);
    private MTUserItemResponse mLastResponse = null;
    private LayoutInflater mInflater;
    private ListView mItemListView;
    private InventorySearchActionAdapter mAdapter;

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

        this.mItemListView = (ListView) this.findViewById(R.id.inventorySearchResultsListView);

        mAdapter = new InventorySearchActionAdapter(this, null);

        if (this.mItemListView != null) {
            this.mItemListView.setAdapter(mAdapter);
        }

        setContentView(R.layout.activity_inventory_search_action);
        handleIntent(this.getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //TODO: webservices call
        }
    }

    private class InventorySearchActionAdapter extends BaseAdapter {

        private List<NamedObject> _listItems = null;

        public InventorySearchActionAdapter(Context context, List<NamedObject> listItems) {

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
                view = new View(InventorySearchActionActivity.this);
            }

            return view;
        }

        public View createNewHeaderView(NamedObject namedObject) {
            MTSection section = (MTSection)namedObject.object;

            View view = new MTListItemHeaderView(InventorySearchActionActivity.this);
            ((MTListItemHeaderView)view).setMargins(0, 0, 0, UIUtils.getPixels(0));

            return view;
        }

        public View createNewItemView(NamedObject namedObject, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.view_list_item_inventory_item, parent, false);
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

                if (userItem.getManufacturer().isPrimary()) {
                    Picasso.with(InventorySearchActionActivity.this)
                            .load(MTConstants.HTTP_PREFIX + userItem.getImageUrl())
                            .placeholder(R.drawable.ic_mkeplaceholder)
                            .error(R.drawable.ic_mkeplaceholder)
                            .into(holder.thumbnailImageView);
                } else {
                    Picasso.with(InventorySearchActionActivity.this)
                            .load(MTConstants.HTTP_PREFIX + userItem.getImageUrl())
                            .placeholder(R.drawable.ic_otherplaceholder)
                            .error(R.drawable.ic_otherplaceholder)
                            .into(holder.thumbnailImageView);
                }

                Date serverDate = StringHelper.parseServerDate(userItem.getDateAdded());
                String formattedDateString = StringHelper.getDateFull(serverDate);

                String descriptionString = userItem.getManufacturer().getName() + " " + userItem.getItemDescription();
                String modelNumberString = userItem.getModelNumber() + " • " + MiscUtils.getString(R.string.all_inventory_details_date) + formattedDateString;

                holder.descriptionTextView.setText(descriptionString);
                holder.modelNumberTextView.setText(modelNumberString);
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

        public List<NamedObject> getListItems() {
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
