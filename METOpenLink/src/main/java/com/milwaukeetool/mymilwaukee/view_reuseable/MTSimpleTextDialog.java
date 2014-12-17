package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.app.Activity;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

/**
 * Created by cent146 on 12/11/14.
 */
public class MTSimpleTextDialog extends MTDialog {

    private String mDescriptionText;

    public MTSimpleTextDialog(Activity activity,
                              String dialogTitle,
                              String descriptionText) {

        this(activity, dialogTitle, descriptionText, MiscUtils.getString(R.string.action_done), true, true, false);
    }

    @Override
    protected MTDialogView createDialogView() {
        return new MTSimpleTextDialogView(mCallingActivity, mDialogTitle,
                mDescriptionText);
    }

    public MTSimpleTextDialog(Activity activity,
                              String dialogTitle,
                              String descriptionText,
                              String actionText) {
        this(activity, dialogTitle, descriptionText, actionText, true, true, false);
    }

    public MTSimpleTextDialog(Activity activity,
                               String dialogTitle,
                               String descriptionText,
                               String actionText,
                               boolean showOk,
                               boolean showCancel,
                               boolean allowCancelOutside) {

        super(activity,dialogTitle,actionText,showOk,showCancel,allowCancelOutside);
        mViewHasTextEntry = true;
        mDescriptionText = descriptionText;
        finishDialogSetup();
    }
}
