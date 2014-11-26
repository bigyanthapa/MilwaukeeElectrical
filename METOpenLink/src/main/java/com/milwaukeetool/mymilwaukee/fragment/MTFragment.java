package com.milwaukeetool.mymilwaukee.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.milwaukeetool.mymilwaukee.util.MiscUtils;

/**
 * Created by cent146 on 11/18/14.
 */
public class MTFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Clear the keep screen on flag
        if (this.getActivity() != null) {
            MiscUtils.disableKeepScreenOn(this.getActivity());
        }
    }
}
