package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/19/14.
 */
public class MTSelectableItemView extends LinearLayout {

    private static final String TAG = makeLogTag(MTSelectableItemView.class);

    private MTTextView mSelectableItemTextView;
    private ImageView mSelectableItemImageView;

    private Activity mCallingActivity;

    public MTSelectableItemView(Activity activity) {
        super(activity);

        mCallingActivity = activity;

        LayoutInflater.from(activity).inflate(R.layout.view_selectable_item, this);

        mSelectableItemImageView = (ImageView)findViewById(R.id.selectableItemImageView);
        mSelectableItemTextView = (MTTextView)findViewById(R.id.selectableItemTextView);

        final IconDrawable arrowDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_angle_right).colorRes(R.color.mt_common_gray).sizeDp(30);
        mSelectableItemImageView.setImageDrawable(arrowDrawable);
    }

    public void setItemText(String text) {
        mSelectableItemTextView.setText(text);
    }

    public void setItemDrawable(Drawable drawable) {

        if (drawable == null) {
            mSelectableItemImageView.setVisibility(INVISIBLE);
        } else {
            mSelectableItemImageView.setVisibility(VISIBLE);
            mSelectableItemImageView.setImageDrawable(drawable);
        }
    }


}
