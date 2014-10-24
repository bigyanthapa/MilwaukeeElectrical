package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.milwaukeetool.test.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 10/24/14.
 */
public class MainActivity extends Activity {

    private static final String TAG = makeLogTag(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
        checkForUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkForCrashes() {
        CrashManager.register(this, MTConfig.getHockeyAppID(), new CrashManagerListener() {
            public boolean shouldAutoUploadCrashes() {
                // Always upload automatically for ALL release builds
                return MTConfig.isExternalRelease();
            }
        });
    }

    private void checkForUpdates() {
        if (!MTConfig.isExternalRelease()) {
            // Include for hockey app builds
            UpdateManager.register(this, MTConfig.getHockeyAppID());
        }
    }
}
