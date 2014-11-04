package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 10/24/14.
 */
public class RouterActivity extends Activity {
    private static final String TAG = makeLogTag(RouterActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_router);

        AnalyticUtils.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, MainActivity.class);

        // TODO: Add any extras???? Anything to pass

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        // TODO: Check if we have logged in already, if not launch Login Activity instead

    }
}
