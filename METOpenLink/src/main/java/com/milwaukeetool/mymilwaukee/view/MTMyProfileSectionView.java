package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/18/2014.
 */
public class MTMyProfileSectionView extends RelativeLayout {
    private static final String TAG = makeLogTag(MTMyProfileSectionView.class);

    private MTTextView mtTextView;

    public MTMyProfileSectionView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_my_profile_section, this);

        mtTextView = (MTTextView) this.findViewById(R.id.my_profile_section_header);
    }

    public void setHeader(String value) {
        this.mtTextView.setText(value);
    }
}
