package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.app.Activity;

/**
 * Created by cent146 on 12/9/14.
 */
public class MTSimpleEntryDialog extends MTDialog {

    private String mEntryPlaceholderText = null;
    private String mEntryText = null;
    private int mEntryMaxLength = -1;

    public MTSimpleEntryDialog(Activity activity,
                               String dialogTitle,
                               String entryPlaceholderText,
                               String entryText,
                               int entryMaxLength,
                               String actionText) {

        this(activity, dialogTitle, entryPlaceholderText, entryText, entryMaxLength, actionText, true, true, false);
    }

    public MTSimpleEntryDialog(Activity activity,
                               String dialogTitle,
                               String entryPlaceholderText,
                               String entryText,
                               int entryMaxLength,
                               String actionText,
                               boolean showOk,
                               boolean showCancel,
                               boolean allowCancelOutside) {

        super(activity,dialogTitle,actionText,showOk,showCancel,allowCancelOutside);
        mViewHasTextEntry = true;
        mEntryPlaceholderText = entryPlaceholderText;
        mEntryText = entryText;
        mEntryMaxLength = entryMaxLength;
        finishDialogSetup();
    }

    @Override
    protected MTDialogView createDialogView() {
        return new MTSimpleEntryDialogView(mCallingActivity,mDialogTitle,
                mEntryPlaceholderText, mEntryText, mEntryMaxLength);
    }
}
