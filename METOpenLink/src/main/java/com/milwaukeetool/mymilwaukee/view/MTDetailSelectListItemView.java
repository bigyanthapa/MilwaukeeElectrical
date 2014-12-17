package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTTextView;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/9/14.
 */
public class MTDetailSelectListItemView extends RelativeLayout {

    private static final String TAG = makeLogTag(MTDetailSelectListItemView.class);

    private Activity mCallingActivity;

    private MTTextView mTextView;
    private RelativeLayout mExtraButton;

    private String mItemText;


    public MTDetailSelectListItemView(Context context) {
        super(context);
        init(context);
    }

    public MTDetailSelectListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTDetailSelectListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {

    }

    public MTDetailSelectListItemView(Activity activity, String itemText) {
        super(activity);

        mCallingActivity = activity;
        mItemText = itemText;

        this.inflateView(activity);

        this.setupView();
    }

    private void inflateView(Activity activity) {
        LayoutInflater.from(activity).inflate(R.layout.view_detail_select_list_item, this);
    }

    private void setupView() {

        mTextView = (MTTextView) this.findViewById(R.id.detailSelectListItemTextView);
        mExtraButton = (RelativeLayout) this.findViewById(R.id.detailSelectListItemExtraButton);

        final IconDrawable ellipsis = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_ellipsis_v).colorRes(R.color.mt_common_gray).sizeDp(20);
        mExtraButton.setBackground(ellipsis);

        mExtraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public String getItemText() {
        return mItemText;
    }

    public void setItemText(String itemText) {
        this.mItemText = itemText;
        mTextView.setText(mItemText);
    }
}
