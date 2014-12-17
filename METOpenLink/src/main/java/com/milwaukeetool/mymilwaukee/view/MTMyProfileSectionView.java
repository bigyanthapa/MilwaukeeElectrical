package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTHeaderTextView;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/18/2014.
 */
public class MTMyProfileSectionView extends RelativeLayout {
    private static final String TAG = makeLogTag(MTMyProfileSectionView.class);

    private MTHeaderTextView mtTextView;

    public MTMyProfileSectionView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_my_profile_section, this);

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
