package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

/**
 * Created by scott.hopfensperger on 11/24/2014.
 */
public class MTChangePasswordPopupView extends LinearLayout {

    private MTSimpleFieldView mCurrent;
    private MTSimpleFieldView mUpdate;
    private MTSimpleFieldView mConfirm;

    public MTChangePasswordPopupView(Activity activity) {
        super(activity);

        LayoutInflater.from(activity).inflate(R.layout.view_change_password, this);

        mCurrent = MTSimpleFieldView.createSimpleFieldView(activity, MiscUtils.getString(R.string.create_account_field_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mCurrent.setTextColorResource(R.color.mt_black);
        mCurrent.setHintColorTextResource(R.color.mt_black);

        mUpdate = MTSimpleFieldView.createSimpleFieldView(activity, MiscUtils.getString(R.string.create_account_field_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mUpdate.setTextColorResource(R.color.mt_black);
        mUpdate.setHintColorTextResource(R.color.mt_black);

        mConfirm = MTSimpleFieldView.createSimpleFieldView(activity, MiscUtils.getString(R.string.create_account_field_password))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mConfirm.setTextColorResource(R.color.mt_black);
        mConfirm.setHintColorTextResource(R.color.mt_black);

        this.addView(mCurrent, this.getChildCount());
        this.addView(mUpdate, this.getChildCount());
        this.addView(mConfirm, this.getChildCount());
    }

    public MTSimpleFieldView getCurrent() {
        return mCurrent;
    }

    public void setCurrent(MTSimpleFieldView mCurrent) {
        this.mCurrent = mCurrent;
    }

    public MTSimpleFieldView getUpdate() {
        return mUpdate;
    }

    public void setUpdate(MTSimpleFieldView mUpdate) {
        this.mUpdate = mUpdate;
    }

    public MTSimpleFieldView getConfirm() {
        return mConfirm;
    }

    public void setConfirm(MTSimpleFieldView mConfirm) {
        this.mConfirm = mConfirm;
    }
}
