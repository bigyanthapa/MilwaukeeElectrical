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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.interfaces.MTAlertDialogListener;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;
import com.milwaukeetool.mymilwaukee.model.request.MTUserManufacturerRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTUserManufacturerDetailsResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;
import com.milwaukeetool.mymilwaukee.view.MTSimpleEntryDialog;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/9/14.
 */
public class OtherItemFragment extends Fragment {
    private static final String TAG = makeLogTag(OtherItemFragment.class);

    private static final String ARG_POSITION = "position";

    private int position;

    private AddItemActivity mAddItemActivity = null;

    private boolean mLoadedManufacturers = false;

    private ListView mListView;
    private OtherItemManufacturerAdapter mAdapter;
    private LayoutInflater mInflater;
    private MenuItem mOtherItemAddManufacturer;

    private RelativeLayout mNoManufacturerLayout;
    private MTButton mAddManufacturerButton;

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

        mInflater = LayoutInflater.from(this.getActivity());

        mListView = (ListView)rootView.findViewById(R.id.otherItemManufacturerListView);
        mListView.setVisibility(View.VISIBLE);

        mNoManufacturerLayout = (RelativeLayout)rootView.findViewById(R.id.otherItemNoManufacturerLayout);
        mNoManufacturerLayout.setVisibility(View.INVISIBLE);

        mAddManufacturerButton = (MTButton)rootView.findViewById(R.id.otherItemAddManufacturerButton);
        mAddManufacturerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddManufacturerDialog();
            }
        });

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
        actionBar.setTitle(this.getResources().getString(R.string.main_add_item_title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mOtherItemAddManufacturer == item) {

            LOGD(TAG, "Adding a manufacturer");

            // Add manufacturer dialog
            showAddManufacturerDialog();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            LOGD(TAG, "Fragment menu is visible");
            loadManufacturers(false);
        }
    }

    private class OtherItemManufacturerAdapter extends BaseAdapter {

        private ArrayList<MTManufacturer> mOtherItemManufacturers;

        public OtherItemManufacturerAdapter(Context context, ArrayList<MTManufacturer> otherItemManufacturers) {

            if (otherItemManufacturers != null) {
                mOtherItemManufacturers = otherItemManufacturers;
            } else {
                mOtherItemManufacturers = new ArrayList<MTManufacturer>();
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
            final View view;
            final ViewHolder holder;
            if(convertView == null) {

                view = mInflater.inflate(R.layout.view_detail_select_list_item, parent, false);
                holder = new ViewHolder();
                holder.detailSelectLayoutButton = (RelativeLayout)view.findViewById(R.id.detailSelectListItemExtraButton);
                holder.detailSelectImageView = (ImageView)view.findViewById(R.id.detailSelectListItemImageView);
                holder.detailSelectTextView = (TextView)view.findViewById(R.id.detailSelectListItemTextView);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder)view.getTag();
            }

            MTManufacturer otherItemManufacturer = mOtherItemManufacturers.get(position);

            final IconDrawable ellipsis = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_ellipsis_v).colorRes(R.color.mt_common_gray).sizeDp(20);
            holder.detailSelectImageView.setBackground(ellipsis);
            holder.detailSelectTextView.setText(otherItemManufacturer.getName() + " (" + otherItemManufacturer.getItemCount() + ")");

            holder.detailSelectLayoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LOGD(TAG, "Tapped list item extra button!!!!");

                    PopupMenu menu = new PopupMenu(OtherItemFragment.this.getActivity(), holder.detailSelectLayoutButton);

                    menu.getMenuInflater().inflate(R.menu.manufacturer_options_menu, menu.getMenu());

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            LOGD(TAG, "Clicked item in popup menu");


                            return false;
                        }
                    });

                    menu.show();
                }
            });

            return view;
        }

        private class ViewHolder {
            public RelativeLayout detailSelectLayoutButton;
            public ImageView detailSelectImageView;
            public TextView detailSelectTextView;
        }

        public void clearManufacturers() {
            mOtherItemManufacturers.clear();
            notifyDataSetChanged();
        }

        public void updateManufacturers(ArrayList<MTManufacturer> manufacturers) {
            clearManufacturers();
            if (manufacturers != null) {
                mOtherItemManufacturers.addAll(manufacturers);
            }
            notifyDataSetChanged();
        }

        public ArrayList<MTManufacturer> getManufacturers() {
            return mOtherItemManufacturers;
        }

        public boolean hasManufacturers() {
            return ((mOtherItemManufacturers != null) && (mOtherItemManufacturers.size() > 0));
        }
    }

    public void loadManufacturers(boolean refresh) {

        if (mLoadedManufacturers && !refresh) {
            return;
        }

        Callback<MTUserManufacturerDetailsResponse> responseCallback = new Callback<MTUserManufacturerDetailsResponse>() {
            @Override
            public void success(MTUserManufacturerDetailsResponse result, Response response) {

                mAddItemActivity.getProgressView().stopProgress();

                updateManufacturersWithResponse(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                mAddItemActivity.getProgressView().stopProgress();

                LOGD(TAG, "Failed to retrieve user manufacturers");

                MTUtils.handleRetrofitError(retrofitError, mAddItemActivity, MiscUtils.getString(R.string.mfr_dialog_title_get_manufacturers_failure));

                mAdapter.updateManufacturers(null);
            }
        };

        mAddItemActivity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_default_message));

        // Get non-Milwaukee manufacturers
        MTWebInterface.sharedInstance().getUserManufacturerService().getManufacturers(MTUtils.getAuthHeaderForBearerToken(),
                false, responseCallback);
    }

    public ArrayList<MTManufacturer> getOtherManufacturers(ArrayList<MTManufacturer> allManufacturers) {

        ArrayList<MTManufacturer> otherManufacturers = new ArrayList<>();

        for(MTManufacturer mfr : allManufacturers) {

            if (mfr != null && !mfr.isPrimary()) {
                otherManufacturers.add(mfr);
            }
        }
        return otherManufacturers;
    }

    private void updateManufacturersWithResponse(MTUserManufacturerDetailsResponse response) {

        if (response != null) {
            ArrayList<MTManufacturer> otherManufacturers = response.getItems();

            mLoadedManufacturers = true;

            if (otherManufacturers != null && otherManufacturers.size() > 0) {
                LOGD(TAG, "Successfully retrieved user manufacturers: " + otherManufacturers.size());
                mAdapter.updateManufacturers(otherManufacturers);
                mNoManufacturerLayout.setVisibility(View.INVISIBLE);
                mListView.setVisibility(View.VISIBLE);
            } else {
                mNoManufacturerLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showAddManufacturerDialog() {
        MTSimpleEntryDialog dialog = new MTSimpleEntryDialog(this.getActivity(),
                MiscUtils.getString(R.string.mfr_add_other_item_add_manufacturer),
                MiscUtils.getString(R.string.mfr_add_other_item_manufacturer_name), null, 64);

        dialog.showDialog(new MTAlertDialogListener() {

            @Override
            public void didTapCancel() {

            }

            @Override
            public void didTapOkWithResult(Object result) {

                if (result instanceof String) {

                    LOGD(TAG, "Add Manufacturer: OK with RESULT: " + (String)result);

                    Callback<Response> responseCallback = new Callback<Response>() {
                        @Override
                        public void success(Response result, Response response) {

                            mAddItemActivity.getProgressView().stopProgress();

                            UIUtils.hideKeyboard(OtherItemFragment.this.getActivity());

                            LOGD(TAG, "Successfully added user manufacturer");

                            loadManufacturers(true);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {

                            mAddItemActivity.getProgressView().stopProgress();

                            UIUtils.hideKeyboard(OtherItemFragment.this.getActivity());

                            LOGD(TAG, "Failed to add user manufacturer");

                            MTUtils.handleRetrofitError(retrofitError, mAddItemActivity, MiscUtils.getString(R.string.mfr_dialog_title_add_manufacturer_failure));
                        }
                    };

                    mAddItemActivity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving));

                    MTUserManufacturerRequest request = new MTUserManufacturerRequest();
                    request.setManufacturerName((String)result);

                    MTWebInterface.sharedInstance().getUserManufacturerService().addManufacturer(
                            MTUtils.getAuthHeaderForBearerToken(),
                            request, responseCallback);

                }
            }
        });
    }

}
