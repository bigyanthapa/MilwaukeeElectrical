package com.milwaukeetool.mymilwaukee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.view.MTInventoryResultItem;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;

import java.util.LinkedList;
import java.util.List;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/2/2014.
 */
public class InventoryResultsFragment extends MTFragment {
    private static final String TAG = makeLogTag(InventoryResultsFragment.class);

    private static final String ARG_POSITION = "position";
    private ListView mListView;

    private int position;

    public static InventoryResultsFragment newInstance(int position) {
        InventoryResultsFragment f = new InventoryResultsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory_results, container, false);

        mListView = (ListView)rootView.findViewById(R.id.inventory_results);
        LinkedList<View> views = new LinkedList<View>();

        MTInventoryResultItem item = new MTInventoryResultItem(this.getActivity());
        item.setDescription("Scott kjdsfl s;df jsf lksajflksjflksa fsa fsa flksa jfsf jlksaf");
        item.setModelNumber("1234");
        item.setToolIcon(R.drawable.tool);
        views.add(item);

        InventoryResultsAdapter mInventoryResultsAdapter = new InventoryResultsAdapter(views);

        if (mListView != null) {
            mListView.setAdapter(mInventoryResultsAdapter);
        }

        return rootView;
    }

    private class InventoryResultsAdapter extends SackOfViewsAdapter {

        public InventoryResultsAdapter(List<View> views) {
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
}
