package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.MTAlertDialogListener;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

/**
 * Created by cent146 on 12/9/14.
 */
public class MTSimpleEntryDialog {

    private Activity mCallingActivity;

    private MTAlertDialogListener mAlertDialogListener;

    private boolean mShowOk = false;
    private boolean mShowCancel = false;
    private boolean mAllowCancelOutside = true;

    private String mActionText = MiscUtils.getString(R.string.action_done);

    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;

    private MTSimpleEntryDialogView mAlertView;

    private ViewGroup mParent;

    public MTSimpleEntryDialog(Activity activity,
                               String headingText,
                               String entryPlaceholderText,
                               String entryText,
                               int entryMaxLength,
                               String actionText) {

        this(activity, headingText, entryPlaceholderText, entryText, entryMaxLength, actionText, true, true, false);
    }


    public MTSimpleEntryDialog(Activity activity,
                               String headingText,
                               String entryPlaceholderText,
                               String entryText,
                               int entryMaxLength,
                               String actionText,
                               boolean showOk,
                               boolean showCancel,
                               boolean allowCancelOutside) {

        mCallingActivity = activity;

        mActionText = actionText;

        mShowCancel = showCancel;
        mShowOk = showOk;
        mAllowCancelOutside = allowCancelOutside;

        mAlertView = new MTSimpleEntryDialogView(mCallingActivity,headingText,
                entryPlaceholderText, entryText, entryMaxLength);

        buildDialog(activity, mAlertView);
    }

    private void buildDialog(final Activity activity, final View alertView) {

        if (activity == null || alertView == null) {
            return;
        }

        mBuilder = new AlertDialog.Builder(activity);

        if (mShowCancel) {
            mBuilder.setNegativeButton(MiscUtils.getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.setTheme(R.style.Theme_Milwaukeetool);
                    UIUtils.hideKeyboard(activity, mAlertView.getEntryFieldView());

                    mAlertDialogListener.didTapCancel();
                }
            });
        }

        if (mShowOk) {
            mBuilder.setPositiveButton(MiscUtils.getString(R.string.action_done), null);
        }
        mBuilder.setView(alertView);
    }

    public void showDialog(MTAlertDialogListener alertDialogListener) {
        mAlertDialogListener = alertDialogListener;

        mDialog = mBuilder.create();

        if (mDialog != null) {

            mCallingActivity.setTheme(android.R.style.Theme_Holo_Light);

            // Allow the user to cancel outside?
            mDialog.setCanceledOnTouchOutside(mAllowCancelOutside);

            // Change the soft input mode to make sure the keyboard is visible for the popup
            mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            mDialog.show();

            if (mShowOk) {
                Button okButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (okButton != null) {
                    okButton.setText(mActionText);
                    okButton.invalidate();
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            completeDialog();
                        }
                    });
                }
            }
        }
    }

    public void completeDialog() {
        // Validate the field
        if (mAlertView.getEntryFieldView().isValid()) {

            UIUtils.hideKeyboard(mCallingActivity, mAlertView.getEntryFieldView());

            // Dismiss the dialog
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }

            // Return the theme and keyboard
            if (mCallingActivity != null) {
                mCallingActivity.setTheme(R.style.Theme_Milwaukeetool);
                UIUtils.hideKeyboard(mCallingActivity, mAlertView.getEntryFieldView());
            }

            // Call the listener for the caller
            mAlertDialogListener.didTapOkWithResult(mAlertView.getEntryFieldView().getFieldValue());
        }
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    public MTSimpleEntryDialogView getAlertView() {
        return mAlertView;
    }
}
