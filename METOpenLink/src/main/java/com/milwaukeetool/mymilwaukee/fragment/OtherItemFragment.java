package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.interfaces.MTAlertDialogListener;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.view.MTSimpleEntryDialog;

import java.util.ArrayList;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/9/14.
 */
public class OtherItemFragment extends Fragment {
    private static final String TAG = makeLogTag(OtherItemFragment.class);

    private static final String ARG_POSITION = "position";

    private int position;

    //private FrameLayout mLayout;

    private AddItemActivity mAddItemActivity = null;

    private ListView mListView;
    private OtherItemManufacturerAdapter mAdapter;
    private LayoutInflater mInflater;
    private MenuItem mOtherItemAddManufacturer;

    public static OtherItemFragment newInstance(int position) {
        OtherItemFragment f = new OtherItemFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof AddItemActivity) {
            mAddItemActivity = (AddItemActivity)activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other_item, container, false);

//        if (rootView instanceof FrameLayout) {
//            mLayout = (FrameLayout)rootView;
//        }

        mInflater = LayoutInflater.from(this.getActivity());

        mListView = (ListView)rootView.findViewById(R.id.otherItemManufacturerListView);

        mAdapter = new OtherItemManufacturerAdapter(this.getActivity(), null);

        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOGD(TAG, "Clicked list item at position: " + position);


            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.other_item_menu, menu);
        mOtherItemAddManufacturer = menu.findItem(R.id.otherItemAddManufacturer);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_other_item_title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mOtherItemAddManufacturer == item) {

            LOGD(TAG, "Adding a manufacturer");

            // Add manufacturer dialog
            MTSimpleEntryDialog dialog = new MTSimpleEntryDialog(this.getActivity(),
                    MiscUtils.getString(R.string.add_other_item_add_manufacturer),
                    MiscUtils.getString(R.string.add_other_item_manufacturer_name), null, 64);

            dialog.showDialog(new MTAlertDialogListener() {

                @Override
                public void didTapCancel() {

                }

                @Override
                public void didTapOkWithResult(Object result) {

                    if (result instanceof String) {
                        LOGD(TAG, "Add Manufacturer: OK with RESULT: " + (String)result);
                    }

                }
            });
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class OtherItemManufacturerAdapter extends BaseAdapter {

        private ArrayList<String> mOtherItemManufacturers;

        public OtherItemManufacturerAdapter(Context context, ArrayList<String> otherItemManufacturers) {

            if (otherItemManufacturers != null) {
                mOtherItemManufacturers = otherItemManufacturers;
            } else {
                mOtherItemManufacturers = new ArrayList<String>();
            }
        }

        @Override
        public int getCount() {
            return mOtherItemManufacturers.size();
        }

        @Override
        public Object getItem(int position) {
            return mOtherItemManufacturers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if(convertView == null) {

                view = mInflater.inflate(R.layout.view_detail_select_list_item, parent, false);
                holder = new ViewHolder();
                holder.detailSelectImageView = (ImageView)view.findViewById(R.id.detailSelectListItemImageView);
                holder.detailSelectTextView = (TextView)view.findViewById(R.id.detailSelectListItemTextView);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder)view.getTag();
            }

            String otherItemManufacturer = mOtherItemManufacturers.get(position);

            final IconDrawable ellipsis = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_ellipsis_v).colorRes(R.color.mt_common_gray).sizeDp(20);
            holder.detailSelectImageView.setImageDrawable(ellipsis);
            holder.detailSelectTextView.setText(otherItemManufacturer);

            return view;
        }

        private class ViewHolder {
            public ImageView detailSelectImageView;
            public TextView detailSelectTextView;
        }
    }
}
