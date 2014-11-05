package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

/**
 * Created by scott.hopfensperger on 10/30/2014.
 */
public class LogInActivity extends Activity {

//    private Button mTestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        UIUtils.updateActionBarTitle(this, "Log in");

        // Link back to UI

//        mTestButton = (Button)findViewById(R.id.testButton);
//        mTestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Do something that crashes
//                int i = 1/0;
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        // Analytics
        AnalyticUtils.logScreenView(this,"Sign In");AnalyticUtils.logScreenView(this,"Sign In");
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
