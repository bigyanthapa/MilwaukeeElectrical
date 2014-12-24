package com.milwaukeetool.mymilwaukee.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.interfaces.MTFinishedListener;
import com.milwaukeetool.mymilwaukee.interfaces.MTLaunchListener;
import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.MTItemSearchResult;
import com.milwaukeetool.mymilwaukee.model.event.MTAddItemEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTLaunchEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTItemDetailRequest;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTLaunchableFieldView;
import com.milwaukeetool.mymilwaukee.view.MTNotesView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.milwaukeetool.mymilwaukee.view.MTToastView;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/3/2014.
 */
public class AddItemDetailActivity extends MTActivity implements MTLaunchListener {
    private static final String TAG = makeLogTag(AddItemDetailActivity.class);

    private MTSimpleFieldView descriptionFieldView;
    private MTSimpleFieldView modelNumberFieldView;
    private MTSimpleFieldView customIdNameFieldView;
    private MTSimpleFieldView serialNumberFieldView;
    private MTSimpleFieldView purchaseLocationFieldView;

    private MTLaunchableFieldView categoryFieldView;
    private MTLaunchableFieldView notesFieldView;
    private MTLaunchableFieldView proofFieldView;

    private String mNotesText = null;

    private View mSpacer;
    private View mFooter;

    private LinearLayout mProductDetailLayout;
    private AlertDialog notesDialog;

    private boolean mSaveInProgress = false;

    private MTItemSearchResult mItemSearchResult;

    private ImageView mItemImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemSearchResult = (MTItemSearchResult)getIntent().getParcelableExtra(MTConstants.SEARCH_ITEM_RESULT);

        mItemImageView = (ImageView)this.findViewById(R.id.addItemImageView);

        mSpacer = new View(this);
        mSpacer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                UIUtils.getPixels(25)));

        mFooter = new View(this);
        mFooter.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                UIUtils.getPixels(20)));

        this.descriptionFieldView = this.createSimpleFieldView(R.string.tool_detail_description,
                R.color.mt_red,
                256,
                true,
                false);

        this.modelNumberFieldView = this.createSimpleFieldView(R.string.tool_detail_model_number,
                R.color.mt_red,
                32,
                true,
                false);

        this.customIdNameFieldView = this.createSimpleFieldView(R.string.tool_detail_name,
                R.color.mt_black,
                20,
                false,
                true);

        this.serialNumberFieldView = this.createSimpleFieldView(R.string.tool_detail_serial_number,
                R.color.mt_black,
                64,
                false,
                true);

        this.purchaseLocationFieldView = this.createSimpleFieldView(R.string.tool_detail_purchase_location,
                R.color.mt_black,
                64,
                false,
                true);
        this.purchaseLocationFieldView.setNextActionDone();

        this.notesFieldView = this.createLaunchableFieldView(R.string.tool_detail_notes,
                R.color.mt_black,
                256,
                false);
        this.notesFieldView.setHintColorTextResource(R.color.mt_black);

        this.proofFieldView = this.createLaunchableFieldView(R.string.tool_detail_proof,
                R.color.mt_black,
                0,
                false);
        this.proofFieldView.setHintColorTextResource(R.color.mt_black);

        this.categoryFieldView = this.createLaunchableFieldView(R.string.tool_detail_category,
                R.color.mt_black,
                0,
                false);
        this.categoryFieldView.setHintColorTextResource(R.color.mt_black);

        this.mapSearchResults(mItemSearchResult);

        this.assembleLayout();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onBackPressed() {

        this.setTheme(R.style.Theme_Milwaukeetool);

        if (mProgressView.isDisplayed()) {
            return;
        }
        super.onBackPressed();
    }

    public void launched(MTLaunchEvent launchEvent) {

        if (launchEvent.getSource() == this.notesFieldView) {

            LayoutInflater inflater = this.getLayoutInflater();

            final MTNotesView notesView = new MTNotesView(this);
            notesView.setNotes(mNotesText);

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(AddItemDetailActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

            builder.setNegativeButton(MiscUtils.getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddItemDetailActivity.this.setTheme(R.style.Theme_Milwaukeetool);
                    UIUtils.hideKeyboard(AddItemDetailActivity.this, notesView);
                }
            });
            builder.setPositiveButton(MiscUtils.getString(R.string.action_done),null);
            builder.setView(notesView);

            notesDialog = builder.create();

            // Don't allow user to cancel by tapping outside
            notesDialog.setCanceledOnTouchOutside(false);

            if (notesDialog != null) {

                // Change the soft input mode to make sure the keyboard is visible for the popup
                notesDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                this.setTheme(android.R.style.Theme_Holo_Light);
                notesDialog.show();

                Button changeButton = notesDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (changeButton != null) {
                    changeButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            AddItemDetailActivity.this.setTheme(R.style.Theme_Milwaukeetool);
                            UIUtils.hideKeyboard(AddItemDetailActivity.this, notesView);
                            AddItemDetailActivity.this.handleNotes(notesView.getNotes());
                        }
                    });
                }
            }
        } else if (launchEvent.getSource() == this.categoryFieldView) {
            Intent categoryActivity = new Intent(AddItemDetailActivity.this, CategoryActivity.class);
            startActivityForResult(categoryActivity, MTConstants.SELECT_CATEGORY_REQUEST);
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MTConstants.SELECT_CATEGORY_REQUEST) {
            if (resultCode == RESULT_OK) {
                MTCategory category = data.getParcelableExtra("category");
                this.categoryFieldView.setFieldValue(category.getName());
                this.categoryFieldView.setFieldId(category.getId());
            }
        }
    }

    public void handleNotes(String notes) {

        mNotesText = notes;

        if (notesDialog.isShowing()) {
            notesDialog.dismiss();
        }
    }

    protected void mapSearchResults(MTItemSearchResult mtItemSearchResult) {
        this.modelNumberFieldView.setFieldValue(mtItemSearchResult.getModelNumber());
        this.descriptionFieldView.setFieldValue(mtItemSearchResult.getItemDescription());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mItemImageView != null && mItemSearchResult != null) {
            Picasso.with(this)
                    .load(MTConstants.HTTP_PREFIX + mItemSearchResult.getImageUrl())
                    .placeholder(R.drawable.ic_mkeplaceholder)
                    .error(R.drawable.ic_mkeplaceholder)
                    .into(mItemImageView);
        }
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_add_item_detail);
    }

    @Override
    protected String getLogTag() {
        return AddItemDetailActivity.TAG;
    }

    @Override
    protected String getScreenName() {
        return MiscUtils.getString(R.string.mt_screen_name_add_item_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mProgressView.isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mSaveInProgress) {
                    finish();
                }
                return true;
            case R.id.addItemActionSave:
                LOGD(TAG, "Saving Item Detail...");

                UIUtils.hideKeyboard(this);

                Callback<Response> responseCallback = new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        mSaveInProgress = false;
                        mProgressView.stopProgress();

                        final IconDrawable successDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                                Iconify.IconValue.fa_check_circle).colorRes(R.color.mt_black).sizeDp(40);

                        EventBus.getDefault().post(new MTAddItemEvent(AddItemDetailActivity.this));

                        MTToastView.showMessage(AddItemDetailActivity.this,
                                MiscUtils.getString(R.string.message_success_add_item),
                                MTToastView.MT_TOAST_SHORT,
                                successDrawable,
                                new MTFinishedListener() {
                                    @Override
                                    public void didFinish() {
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        LOGD(TAG, "Failed to add item");

                        mSaveInProgress = false;

                        // Hide progress indicator
                        mProgressView.stopProgress();
                        MTUtils.handleRetrofitError(retrofitError, AddItemDetailActivity.this,
                                MiscUtils.getString(R.string.dialog_title_add_item_failure));
                    }
                };

                if (this.isFieldsValid()) {
                    mSaveInProgress = true;
                    mProgressView.updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving));
                    MTItemDetailRequest detailRequest = this.constructMTItemDetailRequest();

                    MTWebInterface.sharedInstance().getUserService().addItem(MTUtils.getAuthHeaderForBearerToken(),
                            detailRequest,
                            responseCallback);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected boolean isFieldsValid() {
        if (this.descriptionFieldView.isValid() &&
                this.modelNumberFieldView.isValid() &&
                this.customIdNameFieldView.isValid() &&
                this.serialNumberFieldView.isValid() &&
                this.purchaseLocationFieldView.isValid() &&
                this.categoryFieldView.isValid() &&
                this.notesFieldView.isValid() &&
                this.proofFieldView.isValid()) {
            return true;
        }

        return false;
    }

    protected MTItemDetailRequest constructMTItemDetailRequest() {
        MTItemDetailRequest request = new MTItemDetailRequest();

        request.setModelNumber(this.modelNumberFieldView.getFieldValue());
        request.setItemDescription(this.descriptionFieldView.getFieldValue());

        request.setImageUrl(mItemSearchResult.getImageUrl());
        if (!TextUtils.isEmpty(mNotesText)) {
            request.setNotes(mNotesText);
        }

        if (!TextUtils.isEmpty(this.categoryFieldView.getFieldValue())) {
            request.setCategoryId(this.categoryFieldView.getFieldId());
        }

        request.setPurchaseLocation(this.purchaseLocationFieldView.getFieldValue());
        request.setSerialNumber(this.serialNumberFieldView.getFieldValue());
        request.setCustomIdentifier(this.customIdNameFieldView.getFieldValue());

//        request.setManufacturerId(-1);
//        request.setCategoryId(-1);

//        request.setItemizationImageUrl("Itemization URL");
//        request.setOrderInformationImageUrl("Order Information Image URL");

        return request;
    }

    protected void assembleLayout() {
        this.mProductDetailLayout = (LinearLayout) this.findViewById(R.id.addItemDetailLayout);
        this.mProductDetailLayout.addView(this.descriptionFieldView);
        this.mProductDetailLayout.addView(this.modelNumberFieldView);
        this.mProductDetailLayout.addView(mSpacer);
        this.mProductDetailLayout.addView(this.categoryFieldView);
        this.mProductDetailLayout.addView(this.customIdNameFieldView);
        this.mProductDetailLayout.addView(this.serialNumberFieldView);
        this.mProductDetailLayout.addView(this.purchaseLocationFieldView);
        this.mProductDetailLayout.addView(this.notesFieldView);
        this.mProductDetailLayout.addView(this.proofFieldView);
        this.mProductDetailLayout.addView(mFooter);
    }

    protected MTLaunchableFieldView createLaunchableFieldView(int name,
                                                              int color,
                                                              int maxLength,
                                                              boolean required) {
        String nameStr = this.getResources().getString(name);
        MTLaunchableFieldView view = MTLaunchableFieldView.createLaunchableFieldView(this, nameStr);
        this.configureFieldView(view, color, maxLength, required, true);
        return view;
    }

    protected MTSimpleFieldView createSimpleFieldView(int name,
                                                      int color,
                                                      int maxLength,
                                                      boolean required,
                                                      boolean editable) {
        String nameStr = this.getResources().getString(name);
        MTSimpleFieldView view = MTSimpleFieldView.createSimpleFieldView(this, nameStr);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                UIUtils.getPixels(45));
        layoutParams.setMargins(0, UIUtils.getPixels(5), 0, 0);
        view.setLayoutParams(layoutParams);

        this.configureFieldView(view, color, maxLength, required, editable);
        return view;
    }

    protected MTSimpleFieldView configureFieldView(MTSimpleFieldView view,
                                                   int color,
                                                   int maxLength,
                                                   boolean required,
                                                   boolean editable) {
        view.setTextColorResource(color);
        view.setHintColorTextResource(R.color.mt_common_gray);
        view.setRequired(required);

        if (maxLength > 0) {
            view.setMaxLength(maxLength);
        }

        if (!editable) {
            view.setUneditable();
        }
        return view;
    }
}
