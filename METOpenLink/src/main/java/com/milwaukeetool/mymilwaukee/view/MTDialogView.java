package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/16/14.
 */
public abstract class MTDialogView extends LinearLayout {

    protected static final String TAG = makeLogTag(MTSimpleEntryDialog.class);
    protected MTTextView mDialogTitleTextView;
    protected LinearLayout mContentLayout;
    protected String mDialogTitle = null;

    protected Activity mActivity = null;

    public MTDialogView(Context context) {
        super(context);

        if (context instanceof Activity) {
            mActivity = (Activity)context;
        } else {
            return;
        }

        // Call standard init
        createLayout(context);
    }

    public MTDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (context instanceof Activity) {
            mActivity = (Activity)context;
        } else {
            return;
        }

        // Call standard init
        createLayout(context);
    }

    public MTDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (context instanceof Activity) {
            mActivity = (Activity)context;
        } else {
            return;
        }
    }

    public MTDialogView(Context context, String dialogTitle) {
        super(context);

        if (context instanceof Activity) {
            mActivity = (Activity)context;
        } else {
            return;
        }

        mDialogTitle = dialogTitle;
    }

    protected void createLayout(Context context) {

        View root = LayoutInflater.from(context).inflate(R.layout.view_base_dialog, this);
        mContentLayout = (LinearLayout)root.findViewById(R.id.simpleDialogContentLayout);

        mDialogTitleTextView = (MTTextView)root.findViewById(R.id.simpleDialogHeadingTextView);
        mDialogTitleTextView.setText(mDialogTitle);

        layoutDialogView();
    }

    protected void addViewToLayout(View view) {
        if (mContentLayout != null) {
            LOGD(TAG, "Adding view to layout");
            mContentLayout.addView(view, mContentLayout.getChildCount());
        }
    }

    protected abstract void layoutDialogView();
    protected abstract MTSimpleFieldView getEntryView();

}
