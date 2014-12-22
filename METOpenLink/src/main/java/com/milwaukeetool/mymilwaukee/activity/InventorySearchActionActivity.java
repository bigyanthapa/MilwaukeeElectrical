package com.milwaukeetool.mymilwaukee.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.adapter.InventoryItemAdapter;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/19/2014.
 */
public class InventorySearchActionActivity extends MTActivity {

    private static final String TAG = makeLogTag(InventorySearchActionActivity.class);
    private MTUserItemResponse mLastResponse = null;
    private ListView mItemListView;
    private InventoryItemAdapter mAdapter;

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_inventory_search_action);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mItemListView = (ListView) this.findViewById(R.id.inventorySearchResultsListView);

        mAdapter = new InventoryItemAdapter(InventorySearchActionActivity.this, null);

        if (this.mItemListView != null) {
            this.mItemListView.setAdapter(mAdapter);
        }

        setContentView(R.layout.activity_inventory_search_action);
        handleIntent(this.getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //TODO: webservices call
        }
    }
}
