package com.milwaukeetool.mymilwaukee.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.milwaukeetool.mymilwaukee.R;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 12/19/2014.
 */
public class InventorySearchActivity extends MTActivity {
    private static final String TAG = makeLogTag(InventorySearchActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_inventory_search);
    }

    @Override
    protected String getLogTag() {
        return InventorySearchActivity.TAG;
    }

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_inventory_search_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.inventory_search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionInventorySearch).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionCategoryAdd) {
            LOGD(TAG, "Adding a category");
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
