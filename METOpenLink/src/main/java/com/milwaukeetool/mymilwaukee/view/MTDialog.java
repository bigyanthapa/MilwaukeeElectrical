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
 * Created by cent146 on 12/16/14.
 */
public abstract class MTDialog {

    protected Activity mCallingActivity;

    protected MTAlertDialogListener mAlertDialogListener;

    protected boolean mShowOk = false;
    protected boolean mShowCancel = false;
    protected boolean mAllowCancelOutside = true;

    protected String mDialogTitle = null;
    protected String mActionText = MiscUtils.getString(R.string.action_done);

    protected AlertDialog mDialog;
    protected AlertDialog.Builder mBuilder;
    protected MTDialogView mDialogView;

    protected ViewGroup mParent;

    protected boolean mViewHasTextEntry = false;

    public MTDialog(Activity activity,
                    String dialogTitle,
                    String actionText) {

        this(activity, dialogTitle, actionText, true, true, false);
    }

    public MTDialog(Activity activity,
                    String dialogTitle,
                    String actionText,
                    boolean showOk,
                    boolean showCancel,
                    boolean allowCancelOutside) {

        mCallingActivity = activity;

        mDialogTitle = dialogTitle;
        mActionText = actionText;

        mShowCancel = showCancel;
        mShowOk = showOk;
        mAllowCancelOutside = allowCancelOutside;
    }

    protected void finishDialogSetup() {
        mDialogView = createDialogView();
        buildDialog(mCallingActivity, mDialogView);
    }

    protected void buildDialog(final Activity activity, final MTDialogView dialogView) {

        if (activity == null || mDialogView == null) {
            return;
        }

        mBuilder = new AlertDialog.Builder(activity);

        if (mShowCancel) {
            mBuilder.setNegativeButton(MiscUtils.getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (mViewHasTextEntry) {
                        activity.setTheme(R.style.Theme_Milwaukeetool);

                        if (mDialogView.getEntryView() != null) {
                            UIUtils.hideKeyboard(activity, mDialogView.getEntryView());
                        } else {
                            UIUtils.hideKeyboard(activity);
                        }
                    }

                    mAlertDialogListener.didTapCancel();
                }
            });
        }

        if (mShowOk) {
            mBuilder.setPositiveButton(MiscUtils.getString(R.string.action_done), null);
        }
        mBuilder.setView(mDialogView);
    }

    public void showDialog(MTAlertDialogListener alertDialogListener) {
        mAlertDialogListener = alertDialogListener;

        mDialog = mBuilder.create();

        if (mDialog != null) {

            if (mViewHasTextEntry) {
                mCallingActivity.setTheme(android.R.style.Theme_Holo_Light);

                // Change the soft input mode to make sure the keyboard is visible for the popup
                mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }

            // Allow the user to cancel outside?
            mDialog.setCanceledOnTouchOutside(mAllowCancelOutside);

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
        if (mViewHasTextEntry && mDialogView.getEntryView() != null && mDialogView.getEntryView().isValid()) {

            UIUtils.hideKeyboard(mCallingActivity, mDialogView.getEntryView());

            // Return the theme and keyboard
            if (mCallingActivity != null) {
                mCallingActivity.setTheme(R.style.Theme_Milwaukeetool);
                UIUtils.hideKeyboard(mCallingActivity, mDialogView.getEntryView());
            }

            // Dismiss the dialog
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }

            // Call the listener for the caller
            mAlertDialogListener.didTapOkWithResult(mDialogView.getEntryView().getFieldValue());
        } else {
            // Dismiss the dialog
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }

            mAlertDialogListener.didTapOkWithResult(null);
        }
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    public MTDialogView getDialogView() {
        return mDialogView;
    }

    // Requires implementation
    protected abstract MTDialogView createDialogView();

}
