package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/11/14.
 */
public class MTSimpleTextDialogView extends MTDialogView {

    private static final String TAG = makeLogTag(MTSimpleEntryDialog.class);

    private MTTextView mDescriptionTextView;
    private String mDescriptionText = null;

    public MTSimpleTextDialogView(Context context) {
        super(context);
    }

    public MTSimpleTextDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTSimpleTextDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MTSimpleTextDialogView(Context context, String dialogTitle, String descriptionText) {
        super(context, dialogTitle);
        mDescriptionText = descriptionText;
        createLayout(context);
    }

    @Override
    protected void layoutDialogView() {

        // Add a description view
        mDescriptionTextView = new MTTextView(mActivity);
        mDescriptionTextView.setTextColor(MiscUtils.getAppResources().getColor(R.color.mt_black));
        mDescriptionTextView.setText(mDescriptionText);
        mDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        int padding = UIUtils.getPixels(20);
        mDescriptionTextView.setPadding(padding, padding, padding, padding);

        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mDescriptionTextView.setLayoutParams(layoutParams);
        addViewToLayout(mDescriptionTextView);
    }

    @Override
    protected MTSimpleFieldView getEntryView() {
        return null;
    }
}
