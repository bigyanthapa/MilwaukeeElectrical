package com.milwaukeetool.mymilwaukee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;

import com.milwaukeetool.mymilwaukee.model.event.MTKeyboardEvent;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

import de.greenrobot.event.EventBus;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;

/**
 * Created by cent146 on 11/18/14.
 */
public abstract class MTFragment extends Fragment {

    protected boolean mActivityAvailable = false;
    protected boolean mIsVisible = false;

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
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        mIsVisible = menuVisible;

        if (menuVisible && this.getActivity() != null) {
            mActivityAvailable = true;
            logScreenView();
        } else if (menuVisible) {
            mActivityAvailable = false;
            LOGD(getLogTag(), "Menu is visible: No activity yet");
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser) {
//            LOGD(getTag(), "Is visible to user!");
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mActivityAvailable && mIsVisible) {
            mActivityAvailable = true;
            logScreenView();
        }
    }

    public void logScreenView() {
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
