package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/11/14.
 */
public class MTSimpleTextDialogView extends LinearLayout {

    private static final String TAG = makeLogTag(MTSimpleEntryDialog.class);
    private MTTextView mDescriptionTextView;
    private MTTextView mHeadingTextView;
    private LinearLayout mContentLayout;
    private String mDescriptionText = null;
    private String mHeadingText = null;

    private Activity mActivity = null;

    public MTSimpleTextDialogView(Context context) {
        super(context);
        init(context);
    }

    public MTSimpleTextDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTSimpleTextDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MTSimpleTextDialogView(Context context, String headingText, String descriptionText) {
        super(context);
        setupSimpleEntryDialogView(headingText, descriptionText);
        init(context);
    }

    public void setupSimpleEntryDialogView(String headingText, String descriptionText) {
        mHeadingText = headingText;
        mDescriptionText = descriptionText;
    }

    public void init(Context context) {

        View root = LayoutInflater.from(context).inflate(R.layout.view_base_dialog, this);

        mContentLayout = (LinearLayout)root.findViewById(R.id.simpleDialogContentLayout);

        if (context instanceof Activity) {
            mActivity = (Activity)context;
        } else {
            return;
        }

        mHeadingTextView = (MTTextView)root.findViewById(R.id.simpleDialogHeadingTextView);
        mHeadingTextView.setText(mHeadingText);

        mDescriptionTextView = new MTTextView(mActivity);
        mDescriptionTextView.setTextColor(MiscUtils.getAppResources().getColor(R.color.mt_black));
        mDescriptionTextView.setText(mDescriptionText);
        mDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        int padding = UIUtils.getPixels(10);
        mDescriptionTextView.setPadding(UIUtils.getPixels(20), padding, UIUtils.getPixels(20), padding);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.getPixels(100));

        mDescriptionTextView.setLayoutParams(layoutParams);

        if (mContentLayout != null) {
            LOGD(TAG, "Adding text view to layout");
            mContentLayout.addView(mDescriptionTextView, mContentLayout.getChildCount());
        }

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.getPixels(15));
        View view = new View(context);
        view.setLayoutParams(layoutParams);
        if (mContentLayout != null) {
            LOGD(TAG, "Adding blank view to layout");
            mContentLayout.addView(view, mContentLayout.getChildCount());
        }
    }
}
