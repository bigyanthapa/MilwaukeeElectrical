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

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
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

    private View mNoInventoryView;
    private View mInventoryView;

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
        this.mNoInventoryView = inflater.inflate(R.layout.fragment_no_inventory, container, false);
        this.mInventoryView = inflater.inflate(R.layout.fragment_inventory, container, false);
        return new View(this.getActivity());
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
                int a = 3;
            }
        };

        MTWebInterface.sharedInstance().getUserService().getItems(
                MTUtils.getAuthHeaderForBearerToken(),
                responseCallback);

        MTActivity activity = (MTActivity) this.getActivity();
        if (activity != null) {
            activity.getProgressView().startProgress();
        }
    }

    public void updateView(MTUserItemResponse result) {
        MTActivity activity = (MTActivity) InventoryFragment.this.getActivity();
        activity.getProgressView().stopProgress();

        this.getView().setVisibility(View.GONE);

        if (result == null || result.isEmpty()) {
            this.mNoInventoryView.setVisibility(View.VISIBLE);
        } else {
            this.mInventoryView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_menu, menu);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_title_inventory_title));
    }

}
