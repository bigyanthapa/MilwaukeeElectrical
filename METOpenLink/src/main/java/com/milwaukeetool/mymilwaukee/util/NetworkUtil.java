package com.milwaukeetool.mymilwaukee.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTTextView;

import de.greenrobot.event.EventBus;

/**
 * Created by scott.hopfensperger on 11/13/2014.
 */
public class NetworkUtil {
    public enum NetworkType {
        TYPE_NOT_CONNECTED, TYPE_WIFI, TYPE_MOBILE
    }

    public static void checkNetworkConnectivity(Context context) {

        boolean isConnected = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                isConnected = true;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                isConnected = true;
            }
        }

        EventBus.getDefault().post(new MTNetworkAvailabilityEvent(context, isConnected));
    }

    public static NetworkType getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return NetworkType.TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return NetworkType.TYPE_MOBILE;
        }
        return NetworkType.TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        NetworkType conn = NetworkUtil.getConnectivityStatus(context);
        String status = "";

        switch (conn) {
            case TYPE_WIFI:
                status = "Wifi enabled";
                break;
            case TYPE_MOBILE:
                status = "Mobile data enabled";
                break;
            default:
                status = "Not connected to Internet";
        }

        return status;
    }

    public static void setConnectivityDisplay(MTTextView textView) {
        NetworkType type = NetworkUtil.getConnectivityStatus(textView.getContext());

        switch (type) {
            case TYPE_NOT_CONNECTED:
                NetworkUtil.showConnectivityDisplay(textView);
                break;
            default:
                NetworkUtil.hideConnectivityDisplay(textView);
        }
    }

    public static void hideConnectivityDisplay(MTTextView textView) {
        textView.setVisibility(View.INVISIBLE);
    }

    public static void showConnectivityDisplay(MTTextView textView) {
        textView.setVisibility(View.VISIBLE);
    }

    public static void setConnectivityDisplayAnimated(MTTextView textView) {
        NetworkType type = NetworkUtil.getConnectivityStatus(textView.getContext());

        switch (type) {
            case TYPE_NOT_CONNECTED:
                NetworkUtil.showConnectivityDisplayAnimated(textView);
                break;
            default:
                NetworkUtil.hideConnectivityDisplayAnimated(textView);
        }
    }

    public static void hideConnectivityDisplayAnimated(MTTextView textView) {
        UIUtils.hideView(textView,500);
    }

    public static void showConnectivityDisplayAnimated(MTTextView textView) {
        UIUtils.showView(textView,500);
    }
}
