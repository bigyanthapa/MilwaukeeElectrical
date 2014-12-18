package com.milwaukeetool.mymilwaukee.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.LogInActivity;
import com.milwaukeetool.mymilwaukee.activity.MainActivity;
import com.milwaukeetool.mymilwaukee.config.MTConfig;
import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.view.MTDialog;
import com.milwaukeetool.mymilwaukee.view.MTSimpleTextDialog;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Design;

import java.net.SocketTimeoutException;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;

/**
 * Created by cent146 on 11/12/14.
 */
public class MTUtils {

    public static void setLoginInfo(String user, String authToken, String tokenType) {
        PrefUtils.setAuthToken(MilwaukeeToolApplication.getAppContext(),authToken);
        PrefUtils.setUserName(MilwaukeeToolApplication.getAppContext(),user);
        PrefUtils.setLoginDone(MilwaukeeToolApplication.getAppContext(),true);
        PrefUtils.setTokenType(MilwaukeeToolApplication.getAppContext(),tokenType);
        PrefUtils.setTokenExpiration(MilwaukeeToolApplication.getAppContext(),0);
    }

    public static void clearLoginInfo() {
        PrefUtils.setAuthToken(MilwaukeeToolApplication.getAppContext(), "");
        PrefUtils.setUserName(MilwaukeeToolApplication.getAppContext(),"");
        PrefUtils.setLoginDone(MilwaukeeToolApplication.getAppContext(),false);
        PrefUtils.setTokenExpiration(MilwaukeeToolApplication.getAppContext(), 0);
        PrefUtils.setTokenType(MilwaukeeToolApplication.getAppContext(), "");
    }

    public static String getAuthHeaderForBearerToken() {
        StringBuilder header = new StringBuilder(100);

        if (MTUtils.isLoggedIn()) {
            header.append(MTConfig.MT_BEARER);
            header.append(PrefUtils.getAuthToken(MilwaukeeToolApplication.getAppContext()));
        }

        return header.toString();
    }

    public static boolean isLoggedIn() {
        if (PrefUtils.isLoginDone(MilwaukeeToolApplication.getAppContext())) {
            if (!TextUtils.isEmpty(PrefUtils.getAuthToken(MilwaukeeToolApplication.getAppContext()))) {
                return true;
            }
        }
        return false;
    }

    public static void displayUserMessage(Activity activity, String msgTitle, String msgDescription) {
        if (activity != null) {
//            PostOffice.newMail(activity)
//                    .setTitle(msgTitle)
//                    .setMessage(msgDescription)
//                    .setThemeColor(MiscUtils.getAppResources().getColor(R.color.mt_red))
//                    .setDesign(Design.HOLO_LIGHT)
//                    .show(activity.getFragmentManager());

            MTDialog dialog = new MTSimpleTextDialog(activity, msgTitle, msgDescription,
                    MiscUtils.getString(R.string.action_ok), true, false, true);
            dialog.showDialog(null);
        }
    }

    public static void handleRetrofitError(RetrofitError retrofitError, Activity activity, String errorTitle) {

        // Handle timeout
        if (retrofitError.getKind() == RetrofitError.Kind.NETWORK) {
            if (retrofitError.getCause() instanceof SocketTimeoutException) {
                // Timeout
                MTUtils.displayUserMessage(activity,
                        errorTitle,
                        MiscUtils.getString(R.string.error_text_offline));
                return;
            } else {
                // No Connection
                EventBus.getDefault().post(new MTNetworkAvailabilityEvent(activity,false));
                MTUtils.displayUserMessage(activity,
                        errorTitle,
                        MiscUtils.getString(R.string.error_text_offline));
                return;
            }
        } else {
            retrofitError.printStackTrace();
        }

        String errorMessage = "";

        if (activity instanceof LogInActivity) {
            errorMessage = MTWebInterface.getLogInErrorMessage(retrofitError);
        } else {
            errorMessage = MTWebInterface.getErrorMessage(retrofitError);
        }

        // Handle standard error
        MTUtils.displayUserMessage(activity, errorTitle, errorMessage);
    }

    public static void launchMainActivityAndFinishCurrent(Activity activity) {

        Intent mainIntent = new Intent(activity, MainActivity.class);
        activity.startActivity(mainIntent);

        if (activity.getParent() == null) {
            activity.setResult(Activity.RESULT_OK);
        } else {
            activity.getParent().setResult(Activity.RESULT_OK);
        }
        activity.finish();
    }

    public static void showDialogMessage(Activity activity, String title, String message) {
        PostOffice.newMail(activity)
                .setTitle(title)
                .setMessage(message)
                .setThemeColor(MiscUtils.getAppResources().getColor(R.color.mt_red))
                .setDesign(Design.HOLO_LIGHT)
                .show(activity.getFragmentManager());
    }
}
