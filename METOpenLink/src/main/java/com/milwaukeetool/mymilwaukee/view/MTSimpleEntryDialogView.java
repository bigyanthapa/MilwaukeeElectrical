package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/10/14.
 */
public class MTSimpleEntryDialogView extends MTDialogView {

    private static final String TAG = makeLogTag(MTSimpleEntryDialog.class);

    private MTSimpleFieldView mEntryFieldView;
    private String mEntryText = null;
    private String mEntryPlaceholderText = null;
    private int mEntryMaxLength = -1;

    public MTSimpleEntryDialogView(Context context) {
        super(context);
    }

    public MTSimpleEntryDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTSimpleEntryDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MTSimpleEntryDialogView(Context context, String dialogTitle, String entryPlaceholderText,
                                   String entryText, int entryMaxLength) {
        super(context, dialogTitle);

        mEntryPlaceholderText = entryPlaceholderText;
        mEntryText = entryText;
        mEntryMaxLength = entryMaxLength;

        createLayout(context);
    }

    @Override
    protected void layoutDialogView() {

        int padding = UIUtils.getPixels(20);

        mEntryFieldView = MTSimpleFieldView.createSimpleFieldView(mActivity, mEntryPlaceholderText)
                .setRequired(true);
        mEntryFieldView.setPadding(0,UIUtils.getPixels(15),0,padding);

        if (mEntryMaxLength > 0) {
            mEntryFieldView.setMaxLength(mEntryMaxLength);
        }

        mEntryFieldView.setTextColorResource(R.color.mt_black);
        mEntryFieldView.setHintColorTextResource(R.color.mt_common_gray);
        mEntryFieldView.setRequired(true);

        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mEntryFieldView.setLayoutParams(layoutParams);
        mEntryFieldView.setNextActionGo();

        if (!TextUtils.isEmpty(mEntryText)) {
            mEntryFieldView.setFieldValue(mEntryText);
        }

        addViewToLayout(mEntryFieldView);
    }

    @Override
    public MTSimpleFieldView getEntryView() {
        return mEntryFieldView;
    }
}
