package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/20/2014.
 */
public class MTSwitchView extends RelativeLayout {
    private static final String TAG = makeLogTag(MTMyProfileSectionView.class);

    private MTSwitch mSwitch;

    public MTSwitchView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_switch_item, this);

        mSwitch = (MTSwitch) this.findViewById(R.id.emailCommunicationSwitch);
    }

    public void setText(String value) {
        this.mSwitch.setText(value);
    }
}
