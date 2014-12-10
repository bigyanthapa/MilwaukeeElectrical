package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.model.MTSection;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/19/14.
 */
public class InventoryFragment extends MTFragment {

    private static final String TAG = makeLogTag(NearbyFragment.class);
    public static final String ADD_ITEM = "Add Item";
    private static final String ARG_POSITION = "position";
    private MTButton mAddInventoryBtn;

    private LinearLayout mNoInventoryLayout;
    private RelativeLayout mInventoryLayout;

    private View mCurrentView;

    private int position;

    public static InventoryFragment newInstance(int position) {
        InventoryFragment f = new InventoryFragment();
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

        this.checkInventory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Pull back layouts to set visibility
        mNoInventoryLayout = (LinearLayout)rootView.findViewById(R.id.inventoryEmptyLayout);
        mInventoryLayout = (RelativeLayout)rootView.findViewById(R.id.inventoryNormalLayout);

        mInventoryLayout.setVisibility(View.VISIBLE);
        mNoInventoryLayout.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.startAddItemActivity();
        return true;
    }

    protected void startAddItemActivity() {
        Intent intent = new Intent(this.getActivity(), AddItemActivity.class);
        startActivity(intent);
    }

    protected void checkInventory() {
        Callback<MTUserItemResponse> responseCallback = new Callback<MTUserItemResponse>() {
            @Override
            public void success(MTUserItemResponse result, Response response) {
                InventoryFragment.this.updateView(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                MTActivity activity = (MTActivity) InventoryFragment.this.getActivity();
                activity.getProgressView().stopProgress();

                // TODO:Need to do more here, see another implementation

                int a = 3;
            }
        };

        // Start progress before making web service call
        MTActivity activity = (MTActivity) this.getActivity();
        if (activity != null) {
            // TODO:
            activity.getProgressView().updateMessageAndStart("UPDATE THIS MESSAGE");
        }

        MTWebInterface.sharedInstance().getUserService().getItems(
                MTUtils.getAuthHeaderForBearerToken(),
                responseCallback);
    }

    public void updateView(MTUserItemResponse result) {
        MTActivity activity = (MTActivity) InventoryFragment.this.getActivity();
        activity.getProgressView().stopProgress();

        // TODO Add a helper function here to do this calculation, and clean it up, I just did this to show you idea
        boolean hasResults = (result != null) && !result.isEmpty();
        boolean hasItems = false;
        if (hasResults && result.getSections() != null) {
            for(MTSection section : result.getSections()) {
                if (section != null) {
                    if (section.getItems() != null && section.getItems().size() > 0) {
                        hasItems = true;
                        break;
                    }
                }
            }
        }

        // Update both layouts always
        this.mNoInventoryLayout.setVisibility(hasItems ? View.INVISIBLE : View.VISIBLE);
        this.mInventoryLayout.setVisibility(hasItems ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_menu, menu);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_inventory_title));
    }

}
