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

    //private Button mTestButton;

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
////                int i = 1/0;
//                runWebServiceTests();
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

//    public void runWebServiceTests() {
//        runGoodRegisterUserTest();
//        runBadEmailRegisterUserTest();
//        runShortPasswordRegisterUserTest();
//        runMismatchPasswordRegisterUserTest();
//        runMissingFieldRegisterUserTest();
//    }
//
//    public void runGoodRegisterUserTest() {
//
//        MTUserRegistrationRequest request = new MTUserRegistrationRequest();
//        request.userFirstName = "TestKeith";
//        request.userLastName = "TestMcDonald";
//        request.userOptIn = false;
//        request.userOccupation = "DIY";
//        request.userEmail = "testkeith@centare.com";
//        request.userPassword = "Abc123456";
//        request.userConfirmPassword = "Abc123456";
//
//        Callback<Response> responseCallback = new Callback<Response>() {
//
//            @Override
//            public void success(Response result, Response response) {
//                LOGD("runGoodRegisterUserTest", "Successfully registered user: " + result.getBody().toString());
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                LOGD("runGoodRegisterUserTest", "Failed to register user");
//                retrofitError.printStackTrace();
//                Toast.makeText(LogInActivity.this, MTWebInterface.getErrorMessage(retrofitError), Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        MTWebInterface.sharedInstance().getUserService().registerUser(request, responseCallback);
//
//    }
//
//    public void runMissingFieldRegisterUserTest() {
//
//        MTUserRegistrationRequest request = new MTUserRegistrationRequest();
//        request.userFirstName = "TestKeith";
//        request.userLastName = "TestMcDonald";
//        request.userOptIn = false;
//        request.userOccupation = "";
//        request.userEmail = "testkeith";
//        request.userPassword = "Abc123456";
//        request.userConfirmPassword = "Abc123456";
//
//        MTWebInterface.sharedInstance().getUserService().registerUser(request, new Callback() {
//            @Override
//            public void success(Object o, Response response) {
//                LOGD("runBadEmailRegisterUserTest","Successfully registered user");
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                LOGD("runBadEmailRegisterUserTest","Failed to register user");
//                retrofitError.printStackTrace();
//                Toast.makeText(LogInActivity.this, MTWebInterface.getErrorMessage(retrofitError), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void runBadEmailRegisterUserTest() {
//
//        MTUserRegistrationRequest request = new MTUserRegistrationRequest();
//        request.userFirstName = "TestKeith";
//        request.userLastName = "TestMcDonald";
//        request.userOptIn = false;
//        request.userOccupation = "DIY";
//        request.userEmail = "testkeith";
//        request.userPassword = "Abc123456";
//        request.userConfirmPassword = "Abc123456";
//
//        MTWebInterface.sharedInstance().getUserService().registerUser(request, new Callback() {
//            @Override
//            public void success(Object o, Response response) {
//                LOGD("runBadEmailRegisterUserTest","Successfully registered user");
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                LOGD("runBadEmailRegisterUserTest","Failed to register user");
//                retrofitError.printStackTrace();
//                Toast.makeText(LogInActivity.this, MTWebInterface.getErrorMessage(retrofitError), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void runShortPasswordRegisterUserTest() {
//
//        MTUserRegistrationRequest request = new MTUserRegistrationRequest();
//        request.userFirstName = "TestKeith";
//        request.userLastName = "TestMcDonald";
//        request.userOptIn = false;
//        request.userOccupation = "DIY";
//        request.userEmail = "testkeith@centare.com";
//        request.userPassword = "Abc";
//        request.userConfirmPassword = "Abc";
//
//        MTWebInterface.sharedInstance().getUserService().registerUser(request, new Callback() {
//            @Override
//            public void success(Object o, Response response) {
//                LOGD("runShortPasswordRegisterUserTest","Successfully registered user");
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                LOGD("runShortPasswordRegisterUserTest","Failed to register user");
//                retrofitError.printStackTrace();
//                Toast.makeText(LogInActivity.this, MTWebInterface.getErrorMessage(retrofitError), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void runMismatchPasswordRegisterUserTest() {
//
//        MTUserRegistrationRequest request = new MTUserRegistrationRequest();
//        request.userFirstName = "TestKeith";
//        request.userLastName = "TestMcDonald";
//        request.userOptIn = false;
//        request.userOccupation = "DIY";
//        request.userEmail = "testkeith@centare.com";
//        request.userPassword = "Abc123456";
//        request.userConfirmPassword = "Abc";
//
//        MTWebInterface.sharedInstance().getUserService().registerUser(request, new Callback() {
//            @Override
//            public void success(Object o, Response response) {
//                LOGD("runMismatchPasswordRegisterUserTest","Successfully registered user");
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                LOGD("runMismatchPasswordRegisterUserTest","Failed to register user");
//                retrofitError.printStackTrace();
//                Toast.makeText(LogInActivity.this, MTWebInterface.getErrorMessage(retrofitError), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
