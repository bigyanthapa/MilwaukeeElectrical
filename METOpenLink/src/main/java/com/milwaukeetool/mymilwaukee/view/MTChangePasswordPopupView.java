package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

/**
 * Created by scott.hopfensperger on 11/24/2014.
 */
public class MTChangePasswordPopupView extends LinearLayout {

    private MTSimpleFieldView mCurrent;
    private MTSimpleFieldView mUpdate;
    private MTSimpleFieldView mConfirm;

    private LinearLayout mLayout;

    public MTChangePasswordPopupView(Activity activity) {
        super(activity);

        LayoutInflater.from(activity).inflate(R.layout.view_change_password, this);

        mLayout = (LinearLayout)findViewById(R.id.changePasswordLayout);

        mCurrent = MTSimpleFieldView.createSimpleFieldView(activity, MiscUtils.getString(R.string.change_password_current))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mCurrent.setTextColorResource(R.color.mt_black);
        mCurrent.setHintColorTextResource(R.color.mt_common_gray);

        mUpdate = MTSimpleFieldView.createSimpleFieldView(activity, MiscUtils.getString(R.string.change_password_new))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mUpdate.setTextColorResource(R.color.mt_black);
        mUpdate.setHintColorTextResource(R.color.mt_common_gray);

        mConfirm = MTSimpleFieldView.createSimpleFieldView(activity, MiscUtils.getString(R.string.change_password_confirm))
                .setFieldType(MTSimpleFieldView.FieldType.PASSWORD).setRequired(true).setMinLength(8).setMaxLength(1024);
        mConfirm.setTextColorResource(R.color.mt_black);
        mConfirm.setHintColorTextResource(R.color.mt_common_gray);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mCurrent.setLayoutParams(layoutParams);
        mUpdate.setLayoutParams(layoutParams);
        mConfirm.setLayoutParams(layoutParams);
        mConfirm.setLastGroupItem();
        mConfirm.setNextActionGo();

        mLayout.addView(mCurrent);

        layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.getPixels(15));
        View view = new View(activity);
        view.setLayoutParams(layoutParams);
        mLayout.addView(view);

        mLayout.addView(mUpdate);
        mLayout.addView(mConfirm);
    }

    public MTSimpleFieldView getCurrent() {
        return mCurrent;
    }

    public void setCurrent(MTSimpleFieldView current) {
        this.mCurrent = current;
    }

    public MTSimpleFieldView getUpdate() {
        return mUpdate;
    }

    public void setUpdate(MTSimpleFieldView update) {
        this.mUpdate = update;
    }

    public MTSimpleFieldView getConfirm() {
        return mConfirm;
    }

    public void setConfirm(MTSimpleFieldView confirm) {
        this.mConfirm = confirm;
    }
}
