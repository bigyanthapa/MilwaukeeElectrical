package com.milwaukeetool.mymilwaukee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.view.MTButton;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.UpdateManagerListener;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 10/24/14.
 */
public class MainActivity extends Activity {

    private static final String TAG = makeLogTag(MainActivity.class);

    private MTButton mCreateAccountBtn;
    private MTButton mLogInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.i(TAG, "PACKAGE NAME: " + getApplicationContext().getPackageName());

        mCreateAccountBtn = (MTButton)findViewById(R.id.createAccountButton);
        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAccountIntent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(createAccountIntent);
            }
        });

        mLogInBtn = (MTButton)findViewById(R.id.logInButton);
        mLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(logInIntent);
            }
        });
        checkForUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();

        AnalyticUtils.logScreenView(this,"Sign Up / Log In");
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        checkForCrashes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
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

        LOGD(TAG,"Checking for HockeyApp Crashes...");

        CrashManager.register(this, MTConfig.getHockeyAppID(), new CrashManagerListener() {
            public boolean shouldAutoUploadCrashes() {
                // Always upload automatically for ALL release builds
                return MTConfig.isExternalRelease();
            }
        });
    }

    private void checkForUpdates() {
        if (!MTConfig.isProduction() && !MTConfig.isBeta()) {

            LOGD(TAG,"Checking for HockeyApp Updates...");

            // Include for hockey app builds
            UpdateManager.register(this, MTConfig.getHockeyAppID(),new UpdateManagerListener() {
                public void onUpdateAvailable() {
                    super.onUpdateAvailable();

                    LOGD(TAG,"Update is available!");
                    Toast.makeText(MainActivity.this, "Update is available!", Toast.LENGTH_SHORT).show();
                }
                public void onNoUpdateAvailable() {
                    Toast.makeText(MainActivity.this, "No updates found.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
