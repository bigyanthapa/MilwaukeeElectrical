package com.milwaukeetool.mymilwaukee.util;

import android.content.res.Resources;

import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;

/**
 * Created by cent146 on 11/11/14.
 */
public class MiscUtils {

    public interface RunDelayedCallback {
        void onFinished();
    }

    public static void runDelayed(int milliseconds, final RunDelayedCallback callbackInterface) {
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callbackInterface != null) {
                    callbackInterface.onFinished();
                }
            }
        }, milliseconds);
    }

    public static Resources getAppResources() {
        return MilwaukeeToolApplication.getAppContext().getResources();
    }

    public static String getString(int id) {
        return getAppResources().getString(id);
    }

}
