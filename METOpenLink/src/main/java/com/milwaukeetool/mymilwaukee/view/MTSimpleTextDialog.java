package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.MTAlertDialogListener;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

/**
 * Created by cent146 on 12/11/14.
 */
public class MTSimpleTextDialog {
    private Activity mCallingActivity;

    private MTAlertDialogListener mAlertDialogListener;

    private boolean mShowOk = false;
    private boolean mShowCancel = false;
    private boolean mAllowCancelOutside = true;

    private String mActionText = "Done";

    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;

    private MTSimpleTextDialogView mTextDialogView;

    private ViewGroup mParent;

    public MTSimpleTextDialog(Activity activity,
                              String headingText,
                              String descriptionText) {

        this(activity, headingText, descriptionText, MiscUtils.getString(R.string.action_done), true, true, false);
    }

    public MTSimpleTextDialog(Activity activity,
                              String headingText,
                              String descriptionText,
                              String actionText) {

        this(activity, headingText, descriptionText, actionText, true, true, false);
    }


    public MTSimpleTextDialog(Activity activity,
                               String headingText,
                               String descriptionText,
                               String actionText,
                               boolean showOk,
                               boolean showCancel,
                               boolean allowCancelOutside) {

        mCallingActivity = activity;

        mActionText = actionText;

        mShowCancel = showCancel;
        mShowOk = showOk;
        mAllowCancelOutside = allowCancelOutside;

        mTextDialogView = new MTSimpleTextDialogView(mCallingActivity,headingText,
                descriptionText);

        buildDialog(activity, mTextDialogView);
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
                    mAlertDialogListener.didTapCancel();
                }
            });
        }

        if (mShowOk) {
            mBuilder.setPositiveButton(mActionText, null);
        }
        mBuilder.setView(alertView);
    }

    public void showDialog(MTAlertDialogListener alertDialogListener) {
        mAlertDialogListener = alertDialogListener;

        mDialog = mBuilder.create();

        if (mDialog != null) {

            // Allow the user to cancel outside?
            mDialog.setCanceledOnTouchOutside(mAllowCancelOutside);

            mDialog.show();
//            Window window = mDialog.getWindow();
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            if (mShowOk) {
                Button okButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (okButton != null) {
                    okButton.setText(mActionText);
                    okButton.invalidate();
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Dismiss the dialog
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }

                            // Call the listener for the caller
                            mAlertDialogListener.didTapOkWithResult(null);
                        }
                    });
                }
            }
        }
    }

    public AlertDialog getDialog() {
        return mDialog;
    }
}
