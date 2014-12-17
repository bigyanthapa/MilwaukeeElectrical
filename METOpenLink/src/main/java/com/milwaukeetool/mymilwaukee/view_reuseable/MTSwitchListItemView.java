package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTMyProfileSectionView;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTSwitch;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/20/2014.
 */
public class MTSwitchListItemView extends RelativeLayout {
    private static final String TAG = makeLogTag(MTMyProfileSectionView.class);

    private MTSwitch mSwitch;

    public MTSwitchListItemView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.view_switch_list_item, this);

        mSwitch = (MTSwitch) this.findViewById(R.id.emailCommunicationSwitch);
    }

    public void setText(String value) {
        this.mSwitch.setText(value);
    }

    public void setSwitchOn(boolean switchOn) {
        mSwitch.setChecked(switchOn);
    }

    public boolean isSwitchOn() {
        return mSwitch.isChecked();
    }

    public void setLastGroupItem() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.mSwitch.getLayoutParams();
        layoutParams.setMargins(
                UIUtils.getPixels(25),
                0,
                UIUtils.getPixels(25),
                UIUtils.getPixels(10)
        );
    }
}
