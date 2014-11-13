package com.milwaukeetool.mymilwaukee.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cent146 on 10/24/14.
 */
public class PrefUtils {

    /**
     * String preference that represents the user's specific auth token.
     */
    public static final String AUTH_TOKEN = "mt_auth_token";

    /**
     * Boolean preference that when checked, indicates that the user has completed account
     * authentication and the initial set up flow.
     */
    public static final String LOGIN_DONE = "mt_login_completed";

    /**
     * String preference that represents the user's email address.
     */
    public static final String USER_NAME = "mt_user_name";

    public static final String TOKEN_EXPIRATION = "mt_token_expiration";

    public static final String TOKEN_TYPE = "mt_token_type";

    public static void clear(final Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static void resetAll(final Context context) {

        // Clear all
        PrefUtils.clear(context);

        String userName = getUserName(context);

        // Reset to default values
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(LOGIN_DONE, false).commit();
        sp.edit().putString(USER_NAME, userName).commit();
        sp.edit().putString(AUTH_TOKEN, "").commit();
        sp.edit().putInt(TOKEN_EXPIRATION, 0).commit();
        sp.edit().putString(TOKEN_TYPE, "").commit();
    }

    public static String dumpPrefs(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        StringBuilder sb = new StringBuilder("");
        sb.append("\n\nCurrent Shared Preferences:\n" + "  ---------------------------\n");
        sb.append("Is Login Done: " + sp.getBoolean(LOGIN_DONE, false));
        sb.append("\n");
        sb.append("User Name: " + sp.getString(USER_NAME, ""));
        sb.append("\n");
        sb.append("Auth Token: " + sp.getString(AUTH_TOKEN, ""));
        sb.append("\n");
        sb.append("Token Type: " + sp.getString(TOKEN_TYPE, ""));
        sb.append("\n");
        sb.append("Token Expiration (seconds): " + sp.getInt(TOKEN_EXPIRATION, 0));
        sb.append("\n");

        return sb.toString();
    }

    public static void setLoginDone(final Context context, final boolean isLoggedIn) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(LOGIN_DONE, isLoggedIn).commit();
    }

    public static boolean isLoginDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(LOGIN_DONE, false);
    }

    public static void setUserName(final Context context, final String userName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(USER_NAME, userName).commit();
    }

    public static String getUserName(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(USER_NAME, "");
    }

    public static void setAuthToken(final Context context, final String authToken) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(AUTH_TOKEN, authToken).commit();
    }

    public static String getAuthToken(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(AUTH_TOKEN, "");
    }

    public static void setTokenType(final Context context, final String tokenType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(TOKEN_TYPE, tokenType).commit();
    }

    public static String getTokenType(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(TOKEN_TYPE, "");
    }

    public static void setTokenExpiration(final Context context, final int tokenExpiration) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(TOKEN_EXPIRATION, tokenExpiration).commit();
    }

    public static int getTokenExpiration(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(TOKEN_EXPIRATION, 0);
    }
}
