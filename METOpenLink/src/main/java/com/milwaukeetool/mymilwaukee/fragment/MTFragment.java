package com.milwaukeetool.mymilwaukee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;

import com.milwaukeetool.mymilwaukee.model.event.MTKeyboardEvent;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by cent146 on 11/18/14.
 */
public abstract class MTFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Clear the keep screen on flag
        if (this.getActivity() != null) {
            MiscUtils.disableKeepScreenOn(this.getActivity());
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(getScreenName())) {
            AnalyticUtils.logScreenView(this.getActivity(), getScreenName());
        }
    }

    // Implement a dummy event
    public void onEvent(MTKeyboardEvent event) {

    }

    // Must implement these methods in the subclassed activity
    protected abstract String getLogTag();
    protected abstract String getScreenName();
}
