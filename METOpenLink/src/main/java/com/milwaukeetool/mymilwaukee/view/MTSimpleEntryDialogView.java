package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/10/14.
 */
public class MTSimpleEntryDialogView extends LinearLayout {

    private static final String TAG = makeLogTag(MTSimpleEntryDialog.class);
    private MTSimpleFieldView mEntryFieldView;
    private MTTextView mHeadingTextView;
    private LinearLayout mContentLayout;
    private String mEntryText = null;
    private String mHeadingText = null;
    private String mEntryPlaceholderText = null;
    private int mEntryMaxLength = -1;

    private Activity mActivity = null;

    public MTSimpleEntryDialogView(Context context) {
        super(context);
        init(context);
    }

    public MTSimpleEntryDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTSimpleEntryDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MTSimpleEntryDialogView(Context context, String headingText, String entryPlaceholderText,
                                   String entryText, int entryMaxLength) {
        super(context);
        setupSimpleEntryDialogView(headingText, entryPlaceholderText, entryText, entryMaxLength);
        init(context);
    }

    public void setupSimpleEntryDialogView(String headingText, String entryPlaceholderText,
                                           String entryText, int entryMaxLength) {
        mHeadingText = headingText;
        mEntryPlaceholderText = entryPlaceholderText;
        mEntryText = entryText;
        mEntryMaxLength = entryMaxLength;
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

        mEntryFieldView = MTSimpleFieldView.createSimpleFieldView(mActivity, mEntryPlaceholderText)
                .setRequired(true);

        if (mEntryMaxLength > 0) {
            mEntryFieldView.setMaxLength(mEntryMaxLength);
        }

        mEntryFieldView.setTextColorResource(R.color.mt_black);
        mEntryFieldView.setHintColorTextResource(R.color.mt_common_gray);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mEntryFieldView.setLayoutParams(layoutParams);
        mEntryFieldView.setNextActionGo();

        if (!TextUtils.isEmpty(mEntryText)) {
            mEntryFieldView.setFieldValue(mEntryText);
        }

        if (mContentLayout != null) {
            LOGD(TAG, "Adding entry field view to layout");
            mContentLayout.addView(mEntryFieldView, mContentLayout.getChildCount());
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

    public MTSimpleFieldView getEntryFieldView() {
        return mEntryFieldView;
    }
}
