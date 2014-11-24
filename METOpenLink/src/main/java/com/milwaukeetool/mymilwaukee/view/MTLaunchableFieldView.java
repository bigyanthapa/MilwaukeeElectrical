package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Delivery;
import com.r0adkll.postoffice.model.Design;
import com.r0adkll.postoffice.styles.ListStyle;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/21/2014.
 */
public class MTLaunchableFieldView extends MTSimpleFieldView {

    private static final String TAG = makeLogTag(MTLaunchableFieldView.class);
    private ImageView mIconView;

    public MTLaunchableFieldView(Activity activity) {
        super(activity);
    }

    @Override
    protected void inflateView(Activity activity) {
        LayoutInflater.from(activity).inflate(R.layout.view_selectable_field, this);
    }

    @Override
    protected void setupView() {
        // Setup parent first
        super.setupView();

        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(true);
        mEditText.setKeyListener(null);

        this.mFieldType = MTSimpleFieldView.FieldType.SELECTABLE;

        this.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view");
                showSelectableOptions();
            }
        });
        mEditText.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view - edittext");
                showSelectableOptions();
            }
        });
    }

    public void showSelectableOptions() {

        // Request focus
        updateFocus();

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager)mCallingActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

        // Create the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCallingActivity, R.layout.view_popup_list_item);

        // Create list popup
        Delivery delivery = PostOffice.newMail(mCallingActivity)
                .setTitle(this.getFieldName())
                .setThemeColorFromResource(R.color.mt_red)
                .setDesign(Design.HOLO_LIGHT)
                .setCanceledOnTouchOutside(true)
                .setCancelable(true)
                .setStyle(new ListStyle.Builder(mCallingActivity)
                        .setOnItemAcceptedListener(new ListStyle.OnItemAcceptedListener<CharSequence>() {
                            @Override
                            public void onItemAccepted(CharSequence item, int position) {
                                Postable postable = (Postable) mCallingActivity;
                                postable.post(item);
                            }
                        })
                        .setDividerHeight(1)
                        .setDivider(new ColorDrawable(mCallingActivity.getResources().getColor(R.color.mt_common_gray)))
                        .build(adapter))
                .build();

        delivery.show(mCallingActivity.getFragmentManager());
    }

    public static MTSelectableFieldView createSelectableFieldView(Activity activity, String fieldName, String[] selectableOptionArray) {
        MTSelectableFieldView selectableFieldView = new MTSelectableFieldView(activity, selectableOptionArray);
        selectableFieldView.setFieldName(fieldName);
        return selectableFieldView;
    }

    public MTLaunchableFieldView setRequired(boolean isRequired) {
        super.setRequired(isRequired());
        return this;
    }

    public MTLaunchableFieldView setMinLength(int minLength) {
        super.setMinLength(minLength);
        return this;
    }

    public MTLaunchableFieldView setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        return this;
    }

    public MTLaunchableFieldView setFieldType(MTSimpleFieldView.FieldType fieldType) {
        super.setFieldType(fieldType);
        return this;
    }

    public MTLaunchableFieldView updateFocus() {
        super.updateFocus();
        return this;
    }

    public void setTextColorResource(int resourceId) {
        super.setTextColorResource(resourceId);

        // Also set the icon
        setIndicatorIcon(resourceId);
    }

    private void setIndicatorIcon(int colorResId) {
        final IconDrawable arrowDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_angle_right).colorRes(colorResId).sizeDp(20);
        mIconView.setImageDrawable(arrowDrawable);
    }
}
