package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

/**
 * Created by scott.hopfensperger on 10/30/2014.
 */
public class LogInActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        UIUtils.updateActionBarTitle(this, "Log in");

        // Link back to UI


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Analytics
    }

    @Override
    protected void onPause() {
        super.onPause();


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
