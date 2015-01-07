package com.milwaukeetool.mymilwaukee.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConstants;

import java.util.ArrayList;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/24/14.
 */
public class SelectItemActivity extends MTActivity {

    private static final String TAG = makeLogTag(MyInventoryFilterActivity.class);

    private ArrayList<String> mItems = null;
    private ListView mSelectItemListView;
    private BaseAdapter mAdapter;

    private int mSelectedPosition = -1;
    private String mTitle = null;

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_select_item);
    }

    @Override
    protected String getLogTag() {
        return null;
    }

    @Override
    protected String getScreenName() {
        return mTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();

        mSelectItemListView = (ListView)findViewById(R.id.selectItemListView);

        mSelectItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mItems.size() > position) {
                    LOGD(TAG, "Selected item: " + mItems.get(position));
                } else {
                    LOGD(TAG, "Selected invalid item");
                }
                Intent intent = new Intent();
                intent.putExtra(MTConstants.INTENT_EXTRA_SELECTED_INDEX, position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mAdapter = new SelectItemAdapter(this, mItems);

        if (mSelectItemListView != null) {
            mSelectItemListView.setAdapter(mAdapter);
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
            mItems = currentIntent.getStringArrayListExtra(MTConstants.INTENT_EXTRA_SELECT_ITEM_ARRAY_LIST);
            mSelectedPosition = currentIntent.getIntExtra(MTConstants.INTENT_EXTRA_SELECTED_INDEX, -1);
            mTitle = currentIntent.getStringExtra(MTConstants.INTENT_EXTRA_TITLE);

            this.setTitle(mTitle);
        }
    }

    private class SelectItemAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        private ArrayList<String> mSelectableItems;

        public SelectItemAdapter(Context context, ArrayList<String> selectableItems) {

            mInflater = SelectItemActivity.this.getLayoutInflater();

            if (selectableItems != null) {
                mSelectableItems = selectableItems;
            } else {
                mSelectableItems = new ArrayList<String>();
            }
        }

        @Override
        public int getCount() {
            return mSelectableItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mSelectableItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view;
            final ViewHolder holder;
            if(convertView == null) {

                view = mInflater.inflate(R.layout.view_detail_select_list_item, parent, false);
                holder = new ViewHolder();
                holder.detailSelectLayoutButton = (RelativeLayout)view.findViewById(R.id.detailSelectListItemExtraButton);
                holder.detailSelectImageView = (ImageView)view.findViewById(R.id.detailSelectListItemImageView);
                holder.detailSelectTextView = (TextView)view.findViewById(R.id.detailSelectListItemTextView);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder)view.getTag();
            }

            final String currentItem = mSelectableItems.get(position);

            if (position == mSelectedPosition) {
                final IconDrawable checked = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_check_circle_o).colorRes(R.color.mt_red).sizeDp(20);
                holder.detailSelectImageView.setBackground(checked);
            } else {
                holder.detailSelectImageView.setBackground(null);
            }
            holder.detailSelectTextView.setText(currentItem);

            return view;
        }

        private class ViewHolder {
            public RelativeLayout detailSelectLayoutButton;
            public ImageView detailSelectImageView;
            public TextView detailSelectTextView;
        }
    }
}
