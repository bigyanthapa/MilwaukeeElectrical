package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.view.MTListItemHeaderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 1/8/15.
 */
public class AddKitItemsActivity extends MTActivity {

    private static final String TAG = makeLogTag(CreateAccountActivity.class);

    private ListView mItemListView;
    private KitItemAdapter mAdapter;
    private ArrayList<MTItemSearchResult> mItems = null;

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_add_kit_items);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return MiscUtils.getString(R.string.mt_screen_name_add_kit_item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();

        mItemListView = (ListView)findViewById(R.id.addKitItemsListView);

        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MTItemSearchResult searchResult = null;

                if (mItems.size() > position) {
                    searchResult = mItems.get(position);
                    LOGD(TAG, "Selected item: " + searchResult);
                } else {
                    LOGD(TAG, "Selected invalid item");
                }

                if (searchResult != null) {
                    Intent intent = new Intent(AddKitItemsActivity.this, AddItemDetailActivity.class);
                    intent.putExtra(MTConstants.INTENT_EXTRA_SEARCH_ITEM_RESULT, searchResult);
                    startActivity(intent);
                }
            }
        });

        mAdapter = new KitItemAdapter(this, mItems);

        if (mItemListView != null) {
            mItemListView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void handleIntent() {
        Intent currentIntent = this.getIntent();

        if (currentIntent != null && currentIntent.getExtras() != null) {
            mItems = currentIntent.getParcelableArrayListExtra(MTConstants.INTENT_EXTRA_KIT_ITEM_ARRAY_LIST);
        }
    }

    private class KitItemAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;
        private Activity mActivity = null;

        private ArrayList<MTItemSearchResult> _listItems = null;

        public KitItemAdapter(Context context, ArrayList<MTItemSearchResult> listItems) {

            if (listItems != null) {
                _listItems = listItems;
            } else {
                _listItems = new ArrayList<>();
            }

            if (context instanceof Activity) {
                mActivity = (Activity)context;
            }

            mInflater = LayoutInflater.from(context);
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
            MTItemSearchResult itemSearchResult = _listItems.get(position);


            if(convertView == null) {
                view = createNewItemView(parent);
                updateItem(itemSearchResult, view);
            } else {

                if ((view != null) && !(view instanceof MTListItemHeaderView)) {
                    view = convertView;
                } else {
                    view = createNewItemView(parent);
                }
                updateItem(itemSearchResult, view);
            }

            if (view == null) {
                view = new View(mActivity);
            }

            return view;
        }

        public View createNewItemView(ViewGroup parent) {

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

        public void updateItem(MTItemSearchResult itemSearchResult, View view) {

            ViewHolder holder = (ViewHolder)view.getTag();

            if (itemSearchResult != null && holder != null) {

                Picasso.with(mActivity)
                        .load(MTConstants.HTTP_PREFIX + itemSearchResult.getImageUrl())
                        .placeholder(R.drawable.ic_mkeplaceholder)
                        .error(R.drawable.ic_mkeplaceholder)
                        .into(holder.thumbnailImageView);

                holder.descriptionTextView.setText(itemSearchResult.getItemDescription());
                holder.modelNumberTextView.setText(itemSearchResult.getModelNumber());
            }
        }

        private class ViewHolder {
            public ImageView thumbnailImageView;
            public TextView descriptionTextView;
            public TextView modelNumberTextView;
            public ImageView actionImageView;
        }

        public void clearListItems() {
            _listItems.clear();
            notifyDataSetChanged();
        }

        public void updateListItems(ArrayList<MTItemSearchResult> itemSearchResultArrayList) {
            _listItems = itemSearchResultArrayList;
            notifyDataSetChanged();
        }
    }
}
