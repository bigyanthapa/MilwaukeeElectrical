package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/18/2014.
 */
public class MTListItemHeaderView extends RelativeLayout {
    private static final String TAG = makeLogTag(MTListItemHeaderView.class);

    private MTHeaderTextView mtTextView;

    public MTListItemHeaderView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_list_item_header, this);

        mtTextView = (MTHeaderTextView) this.findViewById(R.id.my_profile_section_header);
    }

    public void setHeader(String value) {
        this.mtTextView.setText(value);
    }

    public void setMargins(int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mtTextView.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
    }
}
