package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.interfaces.MTAlertDialogListener;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTUserManufacturerRequest;
import com.milwaukeetool.mymilwaukee.model.response.MTUserManufacturerDetailsResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;
import com.milwaukeetool.mymilwaukee.view.MTSimpleEntryDialog;
import com.milwaukeetool.mymilwaukee.view.MTSimpleTextDialog;
import com.milwaukeetool.mymilwaukee.view.MTToastView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/9/14.
 */
public class OtherItemFragment extends MTFragment {
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

    private MTSimpleEntryDialog mManufacturerDialog = null;

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
                showManufacturerDialog(null);
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
        actionBar.setTitle(this.getResources().getString(R.string.main_title_add_item));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mAddItemActivity.getProgressView().isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        if (mOtherItemAddManufacturer == item) {

            LOGD(TAG, "Adding a manufacturer");

            // Add manufacturer dialog
            showManufacturerDialog(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return MiscUtils.getString(R.string.mt_screen_name_manufacturer_list);
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

            final MTManufacturer otherItemManufacturer = mOtherItemManufacturers.get(position);

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

                            switch (item.getItemId()) {

                                case R.id.editManufacturerOptionItem:
                                    showManufacturerDialog(otherItemManufacturer);
                                    break;

                                case R.id.deleteManufacturerOptionItem:
                                    attemptToDeleteManufacturer(otherItemManufacturer);
                                    break;

                            }
                            return false;
                        }
                    });

                    AnalyticUtils.logScreenView(OtherItemFragment.this.getActivity(), MiscUtils.getString(R.string.mt_screen_name_mfr_option_view));

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

    private void showManufacturerDialog(final MTManufacturer manufacturer) {
        String title = null;
        String name = null;

        if (manufacturer == null) {
            title = MiscUtils.getString(R.string.mfr_add_other_item_add_manufacturer);
        } else {
            title = MiscUtils.getString(R.string.mfr_add_other_item_edit_manufacturer);
            name = manufacturer.getName();
        }

        mManufacturerDialog = new MTSimpleEntryDialog(this.getActivity(),
                title, MiscUtils.getString(R.string.mfr_add_other_item_manufacturer_name),
                name, 64, MiscUtils.getString(R.string.action_save));

        AnalyticUtils.logScreenView(this.getActivity(), MiscUtils.getString(R.string.mt_screen_name_add_edit_mfr));

        mManufacturerDialog.showDialog(new MTAlertDialogListener() {

            @Override
            public void didTapCancel() {
                UIUtils.hideKeyboard(OtherItemFragment.this.getActivity());

                mManufacturerDialog = null;
            }

            @Override
            public void didTapOkWithResult(Object result) {

                if (result instanceof String) {

                    if (manufacturer == null) {
                        addManufacturer((String)result);
                    } else {
                        editManufacturer(manufacturer, (String)result);
                    }
                }

                mManufacturerDialog = null;
            }
        });
    }

    private void addManufacturer(String manufacturer) {
        LOGD(TAG, "Add Manufacturer: OK with RESULT");

        Callback<Response> responseCallback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {

                mAddItemActivity.getProgressView().stopProgress();

                LOGD(TAG, "Successfully added user manufacturer");

                MTToastView.showSuccessMessage((MTActivity)OtherItemFragment.this.getActivity(),
                        MiscUtils.getString(R.string.mfr_toast_saved));

                loadManufacturers(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                mAddItemActivity.getProgressView().stopProgress();

                LOGD(TAG, "Failed to add user manufacturer");

                MTUtils.handleRetrofitError(retrofitError, mAddItemActivity, MiscUtils.getString(R.string.mfr_dialog_title_add_manufacturer_failure));
            }
        };

        mAddItemActivity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving));

        MTUserManufacturerRequest request = new MTUserManufacturerRequest();
        request.setManufacturerName(manufacturer);

        MTWebInterface.sharedInstance().getUserManufacturerService().addManufacturer(
                MTUtils.getAuthHeaderForBearerToken(),
                request, responseCallback);

    }

    private void editManufacturer(MTManufacturer manufacturer, String manufacturerName) {
        LOGD(TAG, "Edit Manufacturer: OK with RESULT: " + manufacturer.getName());

        Callback<Response> responseCallback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {

                mAddItemActivity.getProgressView().stopProgress();

                LOGD(TAG, "Successfully edited user manufacturer");

                MTToastView.showSuccessMessage((MTActivity)OtherItemFragment.this.getActivity(),
                        MiscUtils.getString(R.string.mfr_toast_updated));

                loadManufacturers(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                mAddItemActivity.getProgressView().stopProgress();

                LOGD(TAG, "Failed to edit user manufacturer");

                MTUtils.handleRetrofitError(retrofitError, mAddItemActivity, MiscUtils.getString(R.string.mfr_dialog_title_update_manufacturer_failure));
            }
        };

        mAddItemActivity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving));

        MTUserManufacturerRequest request = new MTUserManufacturerRequest();
        request.setManufacturerName(manufacturerName);

        MTWebInterface.sharedInstance().getUserManufacturerService().updateManufacturer(
                MTUtils.getAuthHeaderForBearerToken(),
                manufacturer.getId(), request, responseCallback);

    }

    private void attemptToDeleteManufacturer(final MTManufacturer manufacturer) {

        // Confirm deletion
        if (manufacturer != null ) {
            if (manufacturer.getItemCount() > 0) {
                // We cannot delete
                MTSimpleTextDialog dialog = new MTSimpleTextDialog(this.getActivity(),
                        MiscUtils.getString(R.string.mfr_dialog_title_delete_manufacturer_failure),
                        MiscUtils.getString(R.string.mfr_has_associated_items_msg),
                        MiscUtils.getString(R.string.action_ok),
                        true, false, true);

                dialog.showDialog(new MTAlertDialogListener() {

                    @Override
                    public void didTapCancel() {

                    }

                    @Override
                    public void didTapOkWithResult(Object result) {

                    }
                });
            } else {
                deleteManufacturer(manufacturer);
            }
        } else {
            LOGD(TAG, "Unable to delete manufacturer, does not exist");
        }
    }

    private void deleteManufacturer(final MTManufacturer manufacturer) {

        if (manufacturer == null) {
            LOGD(TAG, "Unable to delete manufacturer, does not exist");
            return;
        }

        LOGD(TAG, "Delete Manufacturer: " + manufacturer.getName());

        String heading = MiscUtils.getString(R.string.dialog_title_delete_prefix) + " " + manufacturer.getName() + "?";

        // Confirm deletion
        MTSimpleTextDialog dialog = new MTSimpleTextDialog(this.getActivity(), heading,
                MiscUtils.getString(R.string.mfr_delete_confirmation_msg),
                MiscUtils.getString(R.string.action_delete));

        AnalyticUtils.logScreenView(this.getActivity(), MiscUtils.getString(R.string.mt_screen_name_add_edit_mfr));

        dialog.showDialog(new MTAlertDialogListener() {

            @Override
            public void didTapCancel() {

            }

            @Override
            public void didTapOkWithResult(Object result) {
                performDelete(manufacturer);
            }
        });
    }

    public void performDelete(final MTManufacturer manufacturer) {
        Callback<Response> responseCallback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {

                mAddItemActivity.getProgressView().stopProgress();

                LOGD(TAG, "Successfully deleted user manufacturer");

                MTToastView.showSuccessMessage((MTActivity)OtherItemFragment.this.getActivity(),
                        MiscUtils.getString(R.string.mfr_toast_deleted));

                loadManufacturers(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                mAddItemActivity.getProgressView().stopProgress();

                LOGD(TAG, "Failed to delete user manufacturer");

                MTUtils.handleRetrofitError(retrofitError, mAddItemActivity, MiscUtils.getString(R.string.mfr_dialog_title_update_manufacturer_failure));
            }
        };

        mAddItemActivity.getProgressView().updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_deleting));

        MTWebInterface.sharedInstance().getUserManufacturerService().deleteManufacturer(
                MTUtils.getAuthHeaderForBearerToken(),
                manufacturer.getId(), responseCallback);
    }

    public void onEvent(MTimeActionEvent event) {
        if (event.callingActivity == this.getActivity()) {
            if (event.action == EditorInfo.IME_ACTION_GO &&
                    event.fieldName.equalsIgnoreCase(MiscUtils.getString(R.string.mfr_add_other_item_manufacturer_name)) && mManufacturerDialog != null) {
                mManufacturerDialog.completeDialog();
                mManufacturerDialog = null;
            }
        }
    }

}
