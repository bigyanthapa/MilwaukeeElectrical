package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.util.MTTouchListener;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/8/14.
 */
public class MTSelectableFieldView extends MTSimpleFieldView {

    private static final String TAG = makeLogTag(MTSelectableFieldView.class);

    public MTSelectableFieldView(Activity activity) {
        super(activity);

        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(true);

        this.mFieldType = FieldType.SELECTABLE;

        this.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view");
            }
        });
        mEditText.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view - edittext");
            }
        });
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LOGD(TAG, "Has focus: " + hasFocus);
                if (hasFocus) {
                    mEditText.clearFocus();
                }
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Do some stuff
                return true;
            }
        });
    }

    public static MTSelectableFieldView createSelectableFieldView(Activity activity, String fieldName) {
        MTSelectableFieldView selectableFieldView = new MTSelectableFieldView(activity);
        selectableFieldView.setFieldName(fieldName);
        return selectableFieldView;
    }

    public MTSelectableFieldView setRequired(boolean isRequired) {
        super.setRequired(isRequired());
        return this;
    }

    public MTSelectableFieldView setMinLength(int minLength) {
        super.setMinLength(minLength);
        return this;
    }

    public MTSelectableFieldView setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        return this;
    }

    public MTSelectableFieldView setFieldType(FieldType fieldType) {
        super.setFieldType(fieldType);
        return this;
    }

    public MTSelectableFieldView updateFocus() {
        super.updateFocus();
        return this;
    }
}
