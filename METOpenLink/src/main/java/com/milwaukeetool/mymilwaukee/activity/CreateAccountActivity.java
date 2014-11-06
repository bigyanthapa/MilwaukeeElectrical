package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.view.MTFooterView;
import com.milwaukeetool.mymilwaukee.view.MTHeaderView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cent146 on 10/24/14.
 */
public class CreateAccountActivity extends Activity {

    private ListView mListView;
    private CreateAccountAdapter mCreateAccountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.setupViews();
    }

    protected void setupViews() {
        ListView listView = (ListView)findViewById(R.id.header_stuff);
        LinkedList<View> views = new LinkedList<View>();

        views.add(new MTHeaderView(this));
        views.add(this.buildSimpleFieldView("First Name"));
        views.add(this.buildSimpleFieldView("Last Name"));
        views.add(this.buildSimpleFieldView("Password"));
        views.add(this.buildSimpleFieldView("Confirm Password"));
        views.add(this.buildSimpleFieldView("Trade/Occupation"));
        views.add(new MTFooterView(this));

        mCreateAccountAdapter = new CreateAccountAdapter(views);

        if (listView != null) {
            listView.setAdapter(mCreateAccountAdapter);
            listView.setFocusable(true);
        }
    }

    protected MTSimpleFieldView buildSimpleFieldView(String fieldName) {
        MTSimpleFieldView simpleFieldView = new MTSimpleFieldView(this);
        simpleFieldView.setFieldName(fieldName);

        return simpleFieldView;
    }

    private class CreateAccountAdapter extends SackOfViewsAdapter {

        public CreateAccountAdapter(List<View> views) {
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Log analytics
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
