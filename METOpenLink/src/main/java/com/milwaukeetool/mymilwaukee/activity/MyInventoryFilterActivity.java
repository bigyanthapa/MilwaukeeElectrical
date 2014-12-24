package com.milwaukeetool.mymilwaukee.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.manager.MyInventoryManager;
import com.milwaukeetool.mymilwaukee.model.event.MTChangeFilterEvent;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTSelectableItemView;

import java.util.LinkedList;

import de.greenrobot.event.EventBus;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/23/14.
 */
public class MyInventoryFilterActivity extends MTActivity {

    private LinearLayout mLayout;

    private static final String TAG = makeLogTag(MyInventoryFilterActivity.class);

    private LinkedList<View> mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setupViews();
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

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_view_by_selection);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_my_inventory_filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.my_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mProgressView.isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews() {

        mViews = new LinkedList<View>();

        // Add view by model number


        // Add view all inventory
        MTSelectableItemView allInventoryView = new MTSelectableItemView(this);
        allInventoryView.setItemText(MiscUtils.getString(R.string.filter_view_all_inventory));
        allInventoryView.setBackground(MiscUtils.getAppResources().getDrawable(R.drawable.selectable_background_milwaukeetool));
        allInventoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Clicked all Inventory");

                EventBus.getDefault().post(new MTChangeFilterEvent(MyInventoryFilterActivity.this,
                        MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_ALL_INVENTORY));

                finish();
            }
        });
        mViews.add(allInventoryView);

        if (MyInventoryManager.sharedInstance().getInventoryFilterType() ==
                MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_ALL_INVENTORY) {

            // Set as checked
            final IconDrawable checkedDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                    Iconify.IconValue.fa_check_circle_o).colorRes(R.color.mt_red).sizeDp(20);
            allInventoryView.setItemDrawable(checkedDrawable);
        } else {
            allInventoryView.setItemDrawable(null);
        }
        mViews.add(createDividerView());

        // Add view by manufacturer
        MTSelectableItemView manufacturerView = new MTSelectableItemView(this);
        manufacturerView.setItemText(MiscUtils.getString(R.string.filter_select_manufacturer));
        manufacturerView.setBackground(MiscUtils.getAppResources().getDrawable(R.drawable.selectable_background_milwaukeetool));
        manufacturerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Clicked Manufacturer");

                EventBus.getDefault().post(new MTChangeFilterEvent(MyInventoryFilterActivity.this,
                        MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_BY_MANUFACTURER));

                finish();
            }
        });
        mViews.add(manufacturerView);

        mViews.add(createDividerView());

        // Add view category
        MTSelectableItemView categoryView = new MTSelectableItemView(this);
        categoryView.setItemText(MiscUtils.getString(R.string.filter_select_category));
        categoryView.setBackground(MiscUtils.getAppResources().getDrawable(R.drawable.selectable_background_milwaukeetool));
        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Clicked Category");

                finish();
            }
        });
        mViews.add(categoryView);

        mViews.add(createDividerView());

        mLayout = (LinearLayout)findViewById(R.id.myInventoryFilterLayout);

        for(View view : mViews) {
            if (view instanceof MTSelectableItemView) {
                view.setLayoutParams(getStandardLayoutParams());
            }
            mLayout.addView(view);
        }
    }

    private LinearLayout.LayoutParams getStandardLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getPixels(60));
    }

    private View createDividerView() {
        View dividerView = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getPixels(1));
        dividerView.setBackgroundColor(MiscUtils.getAppResources().getColor(R.color.mt_divider_gray));
        dividerView.setLayoutParams(layoutParams);
        return dividerView;
    }
}
