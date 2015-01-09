package com.milwaukeetool.mymilwaukee.activity;

import android.content.Intent;
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
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.manager.MyInventoryManager;
import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;
import com.milwaukeetool.mymilwaukee.model.event.MTChangeFilterEvent;
import com.milwaukeetool.mymilwaukee.model.response.MTUserCategoryResponse;
import com.milwaukeetool.mymilwaukee.model.response.MTUserManufacturerDetailsResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTSelectableItemView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/23/14.
 */
public class MyInventoryFilterActivity extends MTActivity {

    private LinearLayout mLayout;

    private static final String TAG = makeLogTag(MyInventoryFilterActivity.class);

    private LinkedList<View> mViews;

    private MyInventoryManager.MyInventoryFilterType mInventoryFilterType;
    private MTCategory mSelectedCategory;
    private MTManufacturer mSelectedManufacturer = null;

    private ArrayList<MTManufacturer> mManufacturers = null;
    private List<MTCategory> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        this.setupViews();
    }

    private void handleIntent() {
        Intent currentIntent = this.getIntent();

        if (currentIntent != null && currentIntent.getExtras() != null) {
            Serializable serializable = currentIntent.getSerializableExtra(MTConstants.INTENT_EXTRA_INVENTORY_FILTER_TYPE);

            if (serializable != null) {
                mInventoryFilterType = (MyInventoryManager.MyInventoryFilterType)serializable;
            } else {
                mInventoryFilterType = MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_DEFAULT;
            }
            mSelectedCategory = currentIntent.getParcelableExtra(MTConstants.INTENT_EXTRA_CATEGORY);
            mSelectedManufacturer = currentIntent.getParcelableExtra(MTConstants.INTENT_EXTRA_MANUFACTURER);
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

        // TODO: FUTURE - Add view by model number

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
        setCheckedItemInSelectableView(allInventoryView, MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_ALL_INVENTORY, false);
        mViews.add(allInventoryView);

        mViews.add(createDividerView());

        // Add view by manufacturer
        MTSelectableItemView manufacturerView = new MTSelectableItemView(this);
        manufacturerView.setItemText(MiscUtils.getString(R.string.filter_view_by_manufacturer));
        manufacturerView.setBackground(MiscUtils.getAppResources().getDrawable(R.drawable.selectable_background_milwaukeetool));
        manufacturerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Clicked Manufacturer");

                // Pull back user's manufacturers
                loadManufacturers();
            }
        });
        setCheckedItemInSelectableView(manufacturerView, MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_BY_MANUFACTURER, true);
        mViews.add(manufacturerView);
        mViews.add(createDividerView());

        // Add view category
        MTSelectableItemView categoryView = new MTSelectableItemView(this);
        categoryView.setItemText(MiscUtils.getString(R.string.filter_view_category));
        categoryView.setBackground(MiscUtils.getAppResources().getDrawable(R.drawable.selectable_background_milwaukeetool));
        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Clicked Category");

                // Pull back user's categories
                loadCategories();
            }
        });
        setCheckedItemInSelectableView(categoryView, MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_BY_CATEGORY, true);
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

    private void setCheckedItemInSelectableView(MTSelectableItemView selectableItemView,
                                                MyInventoryManager.MyInventoryFilterType checkAgainstFilterType,
                                                boolean isAlternateImage) {
        IconDrawable newDrawable = null;

        if (mInventoryFilterType == checkAgainstFilterType) {
            // Set as checked
            newDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                    Iconify.IconValue.fa_check_circle_o).colorRes(R.color.mt_red).sizeDp(20);
        }

        if (isAlternateImage) {
            selectableItemView.setItemAltDrawable(newDrawable);
        } else {
            selectableItemView.setItemDrawable(newDrawable);
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

    private void handleWebServiceError(RetrofitError retrofitError, String errorTitle) {

        // Stop progress
        getProgressView().stopProgress();

        // Process error message
        MTUtils.handleRetrofitError(retrofitError, this, errorTitle);
        LOGD(TAG, errorTitle);

        // Revert the theme back always
        setTheme(R.style.Theme_Milwaukeetool);
    }

    private void loadManufacturers() {

        Callback<MTUserManufacturerDetailsResponse> responseCallback = new Callback<MTUserManufacturerDetailsResponse>() {
            @Override
            public void success(MTUserManufacturerDetailsResponse result, Response response) {
                getProgressView().stopProgress();
                processManufacturersWithResponse(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleWebServiceError(retrofitError, MiscUtils.getString(R.string.mfr_dialog_title_get_manufacturers_failure));
                // TODO: FUTURE - Anything else ?
            }
        };

        getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_default_message));

        // Get all manufacturers
        MTWebInterface.sharedInstance().getUserManufacturerService().getManufacturers(MTUtils.getAuthHeaderForBearerToken(),
                true, responseCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == MTConstants.SELECT_CATEGORY_ITEM_REQUEST) {
                int selectedIndex = data.getIntExtra(MTConstants.INTENT_EXTRA_SELECTED_INDEX, -1);

                MTCategory category = null;
                if (selectedIndex != -1 && mCategories != null && mCategories.size() > selectedIndex) {
                    category = mCategories.get(selectedIndex);
                }

                EventBus.getDefault().post(new MTChangeFilterEvent(MyInventoryFilterActivity.this,
                        MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_BY_CATEGORY,
                        category));

                finish();
            } else if (requestCode == MTConstants.SELECT_MANUFACTURER_ITEM_REQUEST) {

                // Get the position
                int selectedIndex = data.getIntExtra(MTConstants.INTENT_EXTRA_SELECTED_INDEX, -1);

                MTManufacturer manufacturer = null;
                if (selectedIndex != -1 && mManufacturers != null && mManufacturers.size() > selectedIndex) {
                    manufacturer = mManufacturers.get(selectedIndex);
                }

                EventBus.getDefault().post(new MTChangeFilterEvent(MyInventoryFilterActivity.this,
                        MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_BY_MANUFACTURER,
                        manufacturer));

                finish();
            }
        }
    }

    private void processManufacturersWithResponse(MTUserManufacturerDetailsResponse response) {

        if (response != null) {
            mManufacturers = response.getItems();

            if (mManufacturers != null && mManufacturers.size() > 0) {
                LOGD(TAG, "Successfully retrieved user manufacturers: " + mManufacturers.size());

                Intent selectItemActivity = new Intent(MyInventoryFilterActivity.this, SelectItemActivity.class);

                int selectedManufacturerIndex = -1;

                if (mSelectedManufacturer != null) {
                    for(int i = 0; i < mManufacturers.size(); i++) {

                        MTManufacturer manufacturer = mManufacturers.get(i);

                        if (manufacturer != null) {
                            LOGD(TAG, "Comparing: " + manufacturer.getId() + " to: " + mSelectedManufacturer.getId());
                            if (manufacturer.getId().compareTo(mSelectedManufacturer.getId()) == 0) {
                                LOGD(TAG, "Successful comparison: " + manufacturer.getId() + " to: " + mSelectedManufacturer.getId());
                                selectedManufacturerIndex = i;
                                break;
                            }
                        }
                    }
                }

                ArrayList<String> mfrStringArray = new ArrayList<>();
                for(int i = 0; i < mManufacturers.size(); i++) {
                    MTManufacturer manufacturer = mManufacturers.get(i);
                    if (manufacturer != null) {
                        mfrStringArray.add(manufacturer.getName() + " (" + manufacturer.getItemCount() + ")");
                    }
                }

                selectItemActivity.putExtra(MTConstants.INTENT_EXTRA_SELECT_ITEM_ARRAY_LIST,
                        mfrStringArray);
                selectItemActivity.putExtra(MTConstants.INTENT_EXTRA_SELECTED_INDEX, selectedManufacturerIndex);
                selectItemActivity.putExtra(MTConstants.INTENT_EXTRA_TITLE, MiscUtils.getString(R.string.filter_select_manufacturer));
                startActivityForResult(selectItemActivity, MTConstants.SELECT_MANUFACTURER_ITEM_REQUEST);

            } else {
                // TODO: FUTURE - ERROR MESSAGE
            }
        }
    }

    private void loadCategories() {
        Callback<MTUserCategoryResponse> responseCallback = new Callback<MTUserCategoryResponse>() {
            @Override
            public void success(MTUserCategoryResponse result, Response response) {
                getProgressView().stopProgress();
                processCategoriesWithResponse(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleWebServiceError(retrofitError, MiscUtils.getString(R.string.ctgy_dialog_title_get_categories_failure));
            }
        };

        getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_default_message));

        MTWebInterface.sharedInstance().getUserCategoryService().getCategories(MTUtils.getAuthHeaderForBearerToken(),
                true, responseCallback);
    }

    private void processCategoriesWithResponse(MTUserCategoryResponse response) {
        if (response != null) {
            mCategories = response.getItems();

            if (mCategories != null && !mCategories.isEmpty()) {
                    LOGD(TAG, "Successfully retrieved user categories: " + mCategories.size());

                int selectedCategoryIndex = -1;

                if (mSelectedCategory != null) {
                    for(int i = 0; i < mCategories.size(); i++) {

                        MTCategory category = mCategories.get(i);

                        if (category != null) {
                            LOGD(TAG, "Comparing: " + category.getId() + " to: " + mSelectedCategory.getId());
                            if (category.getId().compareTo(mSelectedCategory.getId()) == 0) {
                                LOGD(TAG, "Successful comparison: " + category.getId() + " to: " + mSelectedCategory.getId());
                                selectedCategoryIndex = i;
                                break;
                            }
                        }
                    }
                }

                ArrayList<String> ctgyStringArray = new ArrayList<String>();
                for (MTCategory category : mCategories) {
                    StringBuilder str = new StringBuilder(100);
                    str.append(category.getName());
                    str.append(" (");
                    str.append(category.getItemCount());
                    str.append(")");

                    ctgyStringArray.add(str.toString());
                }


                Intent selectItemActivity = new Intent(MyInventoryFilterActivity.this, SelectItemActivity.class);
                selectItemActivity.putExtra(MTConstants.INTENT_EXTRA_SELECT_ITEM_ARRAY_LIST,
                        ctgyStringArray);
                selectItemActivity.putExtra(MTConstants.INTENT_EXTRA_SELECTED_INDEX, selectedCategoryIndex);
                selectItemActivity.putExtra(MTConstants.INTENT_EXTRA_TITLE, MiscUtils.getString(R.string.filter_select_category));
                startActivityForResult(selectItemActivity, MTConstants.SELECT_CATEGORY_ITEM_REQUEST);

            } else {
                // TODO: FUTURE - ERROR MESSAGE
            }
        }
    }

}
