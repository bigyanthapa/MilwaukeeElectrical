package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.MTLaunchListener;
import com.milwaukeetool.mymilwaukee.model.event.MTLaunchEvent;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;

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

        mIconView = (ImageView)this.findViewById(R.id.selectableFieldIconView);
        setIndicatorIcon(R.color.mt_common_gray);

        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(true);
        mEditText.setKeyListener(null);

        this.mFieldType = MTSimpleFieldView.FieldType.SELECTABLE;

        this.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view");

                MTLaunchEvent launchEvent = new MTLaunchEvent(MTLaunchableFieldView.this);
                if (mCallingActivity instanceof MTLaunchListener) {
                    ((MTLaunchListener) mCallingActivity).launched(launchEvent);
                }
            }
        });

        mEditText.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view - edittext");
                MTLaunchEvent launchEvent = new MTLaunchEvent(MTLaunchableFieldView.this);

                if (mCallingActivity instanceof MTLaunchListener) {
                    ((MTLaunchListener) mCallingActivity).launched(launchEvent);
                }
            }
        });
    }

    public static MTLaunchableFieldView createLaunchableFieldView(Activity activity, String fieldName) {
        MTLaunchableFieldView launchableFieldView = new MTLaunchableFieldView(activity);
        launchableFieldView.setFieldName(fieldName);
        return launchableFieldView;
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
