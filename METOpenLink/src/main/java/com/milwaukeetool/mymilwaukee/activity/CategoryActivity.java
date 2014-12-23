package com.milwaukeetool.mymilwaukee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.MTAlertDialogListener;
import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.event.MTChangeInventoryEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTUserCategoryRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTUserCategoryResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;
import com.milwaukeetool.mymilwaukee.view.MTSimpleEntryDialog;
import com.milwaukeetool.mymilwaukee.view.MTSimpleTextDialog;
import com.milwaukeetool.mymilwaukee.view.MTToastView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/16/2014.
 */
public class CategoryActivity extends MTActivity {

    private static final String TAG = makeLogTag(CategoryActivity.class);
    private ListView mListView;
    private RelativeLayout mRelativeLayout;
    private MTButton mAddCategoryButton;
    private LayoutInflater mInflater;
    private CategoryAdapter mAdapter;
    private boolean mLoadedCategories = false;
    private MTSimpleEntryDialog mCategoryDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = (ListView)this.findViewById(R.id.categoryListView);
        mListView.setVisibility(View.VISIBLE);

        mRelativeLayout = (RelativeLayout)this.findViewById(R.id.noCategoryLayout);
        mRelativeLayout.setVisibility(View.INVISIBLE);

        mAddCategoryButton = (MTButton)this.findViewById(R.id.addCateogryButton);
        mAddCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog(null);
            }
        });

        loadCategories(false);

        mInflater = LayoutInflater.from(this);

        mAdapter = new CategoryAdapter(this, null);

        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOGD(TAG, "Clicked list item at position: " + position);


                MTCategory selectedCategory = null;
                List<MTCategory> categories = mAdapter.getCategories();
                if (categories != null && categories.size() > position) {
                    selectedCategory = categories.get(position);
                }

                if (selectedCategory != null) {
                    Intent intent = new Intent();
                    intent.putExtra("category", selectedCategory);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Revert the theme back always
        setTheme(R.style.Theme_Milwaukeetool);
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_category);
    }

    @Override
    protected String getLogTag() {
        return CategoryActivity.TAG;
    }

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_category);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionCategoryAdd) {
            LOGD(TAG, "Adding a category");
            showCategoryDialog(null);

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showCategoryDialog(final MTCategory category) {
        String title = null;
        String name = null;

        if (category == null) {
            title = MiscUtils.getString(R.string.ctgy_add_category);
        } else {
            title = MiscUtils.getString(R.string.ctgy_edit_category);
            name = category.getName();
        }

        mCategoryDialog = new MTSimpleEntryDialog(this,
                title, MiscUtils.getString(R.string.ctgy_category_hint),
                name, 64, MiscUtils.getString(R.string.action_save));

        AnalyticUtils.logScreenView(this, MiscUtils.getString(R.string.mt_screen_name_add_edit_category));

        mCategoryDialog.showDialog(new MTAlertDialogListener() {

            @Override
            public void didTapCancel() {
                UIUtils.hideKeyboard(CategoryActivity.this);

                mCategoryDialog = null;
            }

            @Override
            public void didTapOkWithResult(Object result) {

                if (result instanceof String) {
                    if (category == null) {
                        addCategory((String)result);
                    } else {
                        editCategory(category, (String)result);
                    }
                }
                mCategoryDialog = null;
            }
        });
    }

    public void addCategory(String categoryName) {
        LOGD(TAG, "Add Category: OK with RESULT");

        Callback<Response> responseCallback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                CategoryActivity categoryActivity = CategoryActivity.this;
                categoryActivity.getProgressView().stopProgress();

                final IconDrawable successDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                        Iconify.IconValue.fa_check_circle).colorRes(R.color.mt_black).sizeDp(40);

                MTToastView.showSuccessMessage(CategoryActivity.this,
                        MiscUtils.getString(R.string.message_success_save_category));

                LOGD(TAG, "Successfully added user category");

                loadCategories(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleWebServiceError(retrofitError, MiscUtils.getString(R.string.ctgy_dialog_title_add_category_failure));
            }
        };

        this.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving));

        MTUserCategoryRequest request = new MTUserCategoryRequest();
        request.setCategoryName(categoryName);

        MTWebInterface.sharedInstance().getUserCategoryService().addCategory(
                MTUtils.getAuthHeaderForBearerToken(),
                request, responseCallback);
    }

    private void editCategory(MTCategory category, String categoryName) {
        LOGD(TAG, "Edit Category: OK with RESULT: " + category.getName());

        Callback<Response> responseCallback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                CategoryActivity categoryActivity = CategoryActivity.this;
                categoryActivity.getProgressView().stopProgress();

                LOGD(TAG, "Successfully edited user category");

                final IconDrawable successDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                        Iconify.IconValue.fa_check_circle).colorRes(R.color.mt_black).sizeDp(40);

                MTToastView.showSuccessMessage(CategoryActivity.this,
                        MiscUtils.getString(R.string.message_success_edit_category));

                loadCategories(true);

                EventBus.getDefault().post(new MTChangeInventoryEvent(CategoryActivity.this));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleWebServiceError(retrofitError,MiscUtils.getString(R.string.ctgy_dialog_title_update_category_failure));
            }
        };

        this.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving));

        MTUserCategoryRequest request = new MTUserCategoryRequest();
        request.setCategoryName(categoryName);

        MTWebInterface.sharedInstance().getUserCategoryService().updateCategory(
                MTUtils.getAuthHeaderForBearerToken(),
                category.getId(), request, responseCallback);
    }

    private class CategoryAdapter extends BaseAdapter {

        private List<MTCategory> mtCategories;

        public CategoryAdapter(Context context, List<MTCategory> categories) {

            if (categories != null) {
                mtCategories = categories;
            } else {
                mtCategories = new ArrayList<MTCategory>();
            }
        }

        @Override
        public int getCount() {
            return mtCategories.size();
        }

        @Override
        public Object getItem(int position) {
            return mtCategories.get(position);
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

            final MTCategory category = mtCategories.get(position);

            final IconDrawable ellipsis = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_ellipsis_v).colorRes(R.color.mt_common_gray).sizeDp(20);
            holder.detailSelectImageView.setBackground(ellipsis);
            holder.detailSelectTextView.setText(category.getName() + " (" + category.getItemCount() + ")");

            holder.detailSelectLayoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LOGD(TAG, "Tapped list item extra button!!!!");

                    PopupMenu menu = new PopupMenu(CategoryActivity.this, holder.detailSelectLayoutButton);

                    menu.getMenuInflater().inflate(R.menu.category_options_menu, menu.getMenu());

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            LOGD(TAG, "Clicked item in popup menu");

                            switch (item.getItemId()) {

                                case R.id.editCategoryOptionItem:
                                    showCategoryDialog(category);
                                    break;

                                case R.id.deleteCategoryOptionItem:
                                    attemptToDeleteCategory(category);
                                    break;

                            }
                            return false;
                        }
                    });

                    AnalyticUtils.logScreenView(CategoryActivity.this, MiscUtils.getString(R.string.mt_screen_name_mfr_option_view));

                    menu.show();
                }
            });

            return view;
        }

        private class ViewHolder {
            public RelativeLayout detailSelectLayoutButton;
            public ImageView detailSelectImageView;
            public TextView detailSelectTextView;
        }

        public void clearCategories() {
            this.mtCategories.clear();
            notifyDataSetChanged();
        }

        public void updateCategories(List<MTCategory> categories) {
            clearCategories();
            if (categories != null) {
                this.mtCategories.addAll(categories);
            }
            notifyDataSetChanged();
        }

        public List<MTCategory> getCategories() {
            return this.mtCategories;
        }

        public boolean hasCategories() {
            return ((this.mtCategories != null) && (this.mtCategories.size() > 0));
        }
    }

    public void loadCategories(boolean refresh) {

        if (mLoadedCategories && !refresh) {
            return;
        }

        Callback<MTUserCategoryResponse> responseCallback = new Callback<MTUserCategoryResponse>() {
            @Override
            public void success(MTUserCategoryResponse result, Response response) {
                CategoryActivity categoryActivity = CategoryActivity.this;
                categoryActivity.getProgressView().stopProgress();

                updateCategoriesWithResponse(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleWebServiceError(retrofitError, MiscUtils.getString(R.string.ctgy_dialog_title_get_categories_failure));
                mAdapter.updateCategories(null);
            }
        };

        this.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_default_message));

        MTWebInterface.sharedInstance().getUserCategoryService().getCategories(MTUtils.getAuthHeaderForBearerToken(),
                false, responseCallback);
    }

    private void updateCategoriesWithResponse(MTUserCategoryResponse response) {

        if (response != null) {
            List<MTCategory> categories = response.getItems();

            this.mLoadedCategories = true;

            if (categories != null && categories.size() > 0) {
                LOGD(TAG, "Successfully retrieved user categories: " + categories.size());
                mAdapter.updateCategories(categories);
                mRelativeLayout.setVisibility(View.INVISIBLE);
                mListView.setVisibility(View.VISIBLE);
            } else {
                mRelativeLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void attemptToDeleteCategory(final MTCategory category) {
        // Confirm deletion
        if (category != null ) {
            deleteCategory(category);
        } else {
            LOGD(TAG, "Unable to delete category, does not exist");
        }
    }

    private void deleteCategory(final MTCategory category) {

        if (category == null) {
            LOGD(TAG, "Unable to delete category, does not exist");
            return;
        }

        LOGD(TAG, "Delete Category: " + category.getName());

        String heading = MiscUtils.getString(R.string.dialog_title_delete_prefix) + " " + category.getName() + "?";

        // Confirm deletion
        MTSimpleTextDialog dialog = new MTSimpleTextDialog(this, heading,
                MiscUtils.getString(R.string.ctgy_delete_confirmation_msg),
                MiscUtils.getString(R.string.action_delete));

        AnalyticUtils.logScreenView(this, MiscUtils.getString(R.string.mt_screen_name_add_edit_category));

        dialog.showDialog(new MTAlertDialogListener() {

            @Override
            public void didTapCancel() {

            }

            @Override
            public void didTapOkWithResult(Object result) {
                performDelete(category);
            }
        });
    }

    public void performDelete(final MTCategory category) {
        Callback<Response> responseCallback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                CategoryActivity categoryActivity = CategoryActivity.this;
                categoryActivity.getProgressView().stopProgress();

                LOGD(TAG, "Successfully deleted user category");

                final IconDrawable successDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                        Iconify.IconValue.fa_check_circle).colorRes(R.color.mt_black).sizeDp(40);

                MTToastView.showSuccessMessage(CategoryActivity.this,
                        MiscUtils.getString(R.string.message_success_delete_category));

                loadCategories(true);

                EventBus.getDefault().post(new MTChangeInventoryEvent(CategoryActivity.this));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                handleWebServiceError(retrofitError, MiscUtils.getString(R.string.ctgy_dialog_title_delete_category_failure));
            }
        };

        this.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_deleting));

        MTWebInterface.sharedInstance().getUserCategoryService().deleteCategory(
                MTUtils.getAuthHeaderForBearerToken(),
                category.getId(), responseCallback);
    }

    public void onEvent(MTimeActionEvent event) {
        if (event.callingActivity == this) {
            if (event.action == EditorInfo.IME_ACTION_GO &&
                    event.fieldName.equalsIgnoreCase(MiscUtils.getString(R.string.ctgy_category_hint)) && mCategoryDialog != null) {
                mCategoryDialog.completeDialog();
                mCategoryDialog = null;
            }
        }
    }

    public void handleWebServiceError(RetrofitError retrofitError, String errorTitle) {

        // Stop progress
        CategoryActivity categoryActivity = CategoryActivity.this;
        categoryActivity.getProgressView().stopProgress();

        // Process error message
        MTUtils.handleRetrofitError(retrofitError, categoryActivity, errorTitle);
        LOGD(TAG, errorTitle);

        // Revert the theme back always
        setTheme(R.style.Theme_Milwaukeetool);
    }
}
