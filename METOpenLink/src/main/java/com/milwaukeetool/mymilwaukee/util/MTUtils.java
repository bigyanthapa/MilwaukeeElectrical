package com.milwaukeetool.mymilwaukee.util;

import android.text.TextUtils;

import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;

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

    public static boolean isLoggedIn() {
        if (PrefUtils.isLoginDone(MilwaukeeToolApplication.getAppContext())) {
            if (!TextUtils.isEmpty(PrefUtils.getAuthToken(MilwaukeeToolApplication.getAppContext()))) {
                return true;
            }
        }
        return false;
    }
}
