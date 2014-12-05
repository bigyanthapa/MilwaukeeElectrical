package com.milwaukeetool.mymilwaukee.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.MTFinishedListener;
import com.milwaukeetool.mymilwaukee.model.request.MTItemDetailRequest;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTLaunchableFieldView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.milwaukeetool.mymilwaukee.view.MTToastView;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/3/2014.
 */
public class AddItemDetailActivity extends MTActivity {
    private static final String TAG = makeLogTag(AddItemDetailActivity.class);

    private MTSimpleFieldView description;
    private MTSimpleFieldView modelNumber;
    private MTSimpleFieldView name;
    private MTSimpleFieldView serialNumber;
    private MTSimpleFieldView purchaseLocation;

    private MTLaunchableFieldView category;
    private MTLaunchableFieldView notes;
    private MTLaunchableFieldView proof;

    private LinearLayout mProductDetailLayout;

    private boolean mSaveInProgress = false;
    private boolean mEditInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.description = this.createSimpleFieldView(R.string.tool_detail_description,
                R.color.mt_red,
                256,
                true,
                false);
        this.modelNumber = this.createSimpleFieldView(R.string.tool_detail_model_number,
                R.color.mt_red,
                32,
                true,
                false);

        this.name = this.createSimpleFieldView(R.string.tool_detail_name,
                R.color.mt_black,
                20,
                false,
                true);
        this.serialNumber = this.createSimpleFieldView(R.string.tool_detail_serial_number,
                R.color.mt_black,
                64,
                false,
                true);
        this.purchaseLocation = this.createSimpleFieldView(R.string.tool_detail_purchase_location,
                R.color.mt_black,
                64,
                false,
                true);

        this.notes = this.createLaunchableFieldView(R.string.tool_detail_notes,
                R.color.mt_black,
                256,
                false);
        this.proof = this.createLaunchableFieldView(R.string.tool_detail_proof,
                R.color.mt_black,
                64,
                false);
        this.category = this.createLaunchableFieldView(R.string.tool_detail_category,
                R.color.mt_black,
                64,
                false);

        this.assembleLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

                hardcode();

                if (this.isFieldsValid()) {
                    mSaveInProgress = true;
                    mProgressView.updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving_item_details));
                    MTItemDetailRequest detailRequest = this.constructMTItemDetailRequest();

                    MTWebInterface.sharedInstance().getUserService().addItem(MTUtils.getAuthHeaderForBearerToken(),
                            detailRequest,
                            responseCallback);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hardcode() {
        this.description.setFieldValue("description");
        this.modelNumber.setFieldValue("model number");
    }

    protected boolean isFieldsValid() {
        if (this.description.isValid() &&
                this.modelNumber.isValid() &&
                this.name.isValid() &&
                this.serialNumber.isValid() &&
                this.purchaseLocation.isValid() &&
                this.category.isValid() &&
                this.notes.isValid() &&
                this.proof.isValid()) {
            return true;
        }

        return false;
    }

    protected MTItemDetailRequest constructMTItemDetailRequest() {
        MTItemDetailRequest request = new MTItemDetailRequest();

        request.setCategoryId(1);
        request.setCustomIdentifier("Custom");
        request.setDateAdded(Calendar.getInstance().getTime());
        request.setImageUrl("Image");
        request.setItemDescription("Description");
        request.setItemizationImageUrl("Itemization URL");
        request.setManufacturerId(1);
        request.setModelNumber("Model Number");
        request.setNotes("Notes");
        request.setOrderInformationImageUrl("Order Information Image URL");
        request.setPurchaseLocation("Purchase Location");
        request.setSerialNumber("Serial Number");

        return request;
    }

    protected void assembleLayout() {
        this.mProductDetailLayout = (LinearLayout) this.findViewById(R.id.productDetailLayout);
        this.mProductDetailLayout.addView(this.description);
        this.mProductDetailLayout.addView(this.modelNumber);
        this.mProductDetailLayout.addView(this.category);
        this.mProductDetailLayout.addView(this.name);
        this.mProductDetailLayout.addView(this.serialNumber);
        this.mProductDetailLayout.addView(this.purchaseLocation);
        this.mProductDetailLayout.addView(this.notes);
        this.mProductDetailLayout.addView(this.proof);
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
        this.configureFieldView(view, color, maxLength, required, editable);
        return view;
    }

    protected MTSimpleFieldView configureFieldView(MTSimpleFieldView view,
                                                   int color,
                                                   int maxLength,
                                                   boolean required,
                                                   boolean editable) {
        view.setTextColorResource(color);
        view.setHintColorTextResource(color);
        view.setRequired(required);
        view.setMaxLength(maxLength);

        if (!editable) {
            view.setUneditable();
        }
        return view;
    }

    protected void appendToLinearLayout(View view) {
        if (this.mProductDetailLayout == null) {
            mProductDetailLayout = (LinearLayout) this.findViewById(R.id.productDetailLayout);
        }

        this.mProductDetailLayout.addView(view);
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
        return null;
    }
}
