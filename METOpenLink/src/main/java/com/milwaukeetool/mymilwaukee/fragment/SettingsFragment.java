package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.LandingActivity;
import com.milwaukeetool.mymilwaukee.activity.MyProfileActivity;
import com.milwaukeetool.mymilwaukee.interfaces.MTClickListener;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTSelectableItemView;
import com.milwaukeetool.mymilwaukee.view.MTSettingsFooterView;

import java.util.LinkedList;
import java.util.List;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/18/14.
 */
public class SettingsFragment extends MTFragment {

    private static final String TAG = makeLogTag(SettingsFragment.class);

    private static final String ARG_POSITION = "position";

    private int position;

    private ListView mListView;
    private SettingsAdapter mSettingsAdapter;

    public static SettingsFragment newInstance(int position) {
        SettingsFragment f = new SettingsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        mListView = (ListView)rootView.findViewById(R.id.settingsListView);
//        mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
//        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//        mListView.setStackFromBottom(true);

        // Add header (blank view)
        LinearLayout viewHeader = new LinearLayout(this.getActivity());
        viewHeader.setOrientation(LinearLayout.HORIZONTAL);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.getPixels(40));
        viewHeader.setLayoutParams(lp);
        mListView.addHeaderView(viewHeader, null, false);

        LinkedList<View> views = new LinkedList<View>();

        MTSelectableItemView itemView = new MTSelectableItemView(this.getActivity());
        itemView.setItemText(MiscUtils.getString(R.string.settings_my_profile));
        views.add(itemView);

        // Add footer
        MTSettingsFooterView footerView = new MTSettingsFooterView(this);
        footerView.setLogoutListener(new MTClickListener() {
            @Override
            public void didClickItem(Object object) {
                MTUtils.clearLoginInfo();

                Activity activity = SettingsFragment.this.getActivity();

                if (activity != null) {
                    Intent intent = new Intent(activity, LandingActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });
        mListView.addFooterView(footerView, null, true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOGD(TAG, "Clicked list item at position: " + position);
                if (position == 1) {
                    Activity activity = SettingsFragment.this.getActivity();
                    Intent intent = new Intent(activity, MyProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        mSettingsAdapter = new SettingsAdapter(views);

        if (mListView != null) {
            mListView.setAdapter(mSettingsAdapter);
        }

        return rootView;
    }

    private class SettingsAdapter extends SackOfViewsAdapter {

        public SettingsAdapter(List<View> views) {
            super(views);
        }

        @Override
        protected View newView(int position, ViewGroup parent) {
            View view = super.newView(position, parent);
            return view;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_settings_title));
    }
}
