package com.milwaukeetool.mymilwaukee.config;

import android.util.Log;

import com.milwaukeetool.mymilwaukee.BuildConfig;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;

/**
 * Created by cent146 on 10/24/14.
 */
public class MTConfig {

    // Intents

    // Server
    public static String MT_API_END_POINT_QA = "http://openlink.qa12.centaredc.com/api";
    public static String MT_API_VERSION_QA = "v1";
    public static String MT_API_SECRET_QA = "bWlsd2F1a2VlX2lvczpiMGI4OTdlZDMxYmM2OGNhNjRiZDY4ZmU3NzcxZDkwMzQ1ODkzZTc4";
    public static String MT_API_SERVER_TOKEN_QA = "Basic bWlsd2F1a2VlX2FuZHJvaWQ6bmg1c2Z2dHgwcHF1bTM1cnpzNWdkNnJ4M3Q2bXQ1czN4d2JpaXRmeQ==";

    public static String MT_API_END_POINT_UAT = "http://openlink.uat12.centaredc.com/api";
    public static String MT_API_VERSION_UAT = "v1";
    public static String MT_API_SECRET_UAT = "bWlsd2F1a2VlX2lvczpiMGI4OTdlZDMxYmM2OGNhNjRiZDY4ZmU3NzcxZDkwMzQ1ODkzZTc4";
    public static String MT_API_SERVER_TOKEN_UAT = MT_API_SERVER_TOKEN_QA;

    public static String MT_API_END_POINT_DEV = "http://openlink.dev12.centaredc.com/api";
    public static String MT_API_VERSION_DEV = "v1";
    public static String MT_API_SECRET_DEV = "bWlsd2F1a2VlX2lvczpiMGI4OTdlZDMxYmM2OGNhNjRiZDY4ZmU3NzcxZDkwMzQ1ODkzZTc4";
    public static String MT_API_SERVER_TOKEN_DEV = MT_API_SERVER_TOKEN_QA;

    public static String MT_API_END_POINT_PROD = "http://openlink.uat12.centaredc.com/api";
    public static String MT_API_VERSION_PROD = "v1";
    public static String MT_API_SECRET_PROD = "bWlsd2F1a2VlX2lvczpiMGI4OTdlZDMxYmM2OGNhNjRiZDY4ZmU3NzcxZDkwMzQ1ODkzZTc4";
    public static String MT_API_SERVER_TOKEN_PROD = MT_API_SERVER_TOKEN_QA;

    public static String MT_API_END_POINT_BETA = "http://openlink.uat12.centaredc.com/api";
    public static String MT_API_VERSION_BETA = "v1";
    public static String MT_API_SECRET_BETA = "bWlsd2F1a2VlX2lvczpiMGI4OTdlZDMxYmM2OGNhNjRiZDY4ZmU3NzcxZDkwMzQ1ODkzZTc4";
    public static String MT_API_SERVER_TOKEN_BETA = MT_API_SERVER_TOKEN_QA;

    // JSON

    // Build info
    public static final String MT_BUILD_TYPE_DEBUG = "debug";
    public static final String MT_BUILD_TYPE_RELEASE = "release";
    public static final String MT_BUILD_TYPE_MONKEYTALK= "monkeytalk";

    public static final String MT_DISTRIBUTION_TYPE_DEV = "DEV";
    public static final String MT_DISTRIBUTION_TYPE_QA = "QA";
    public static final String MT_DISTRIBUTION_TYPE_UAT = "UAT";
    public static final String MT_DISTRIBUTION_TYPE_BETA = "BETA";
    public static final String MT_DISTRIBUTION_TYPE_PROD = "PROD";

    // HockeyApp
    public static final String MT_HOCKEY_APP_ID_QA = "3f1e886329de8dde358e49a0e3b08682";
    public static final String MT_HOCKEY_APP_ID_PROD = "";
    public static final String MT_HOCKEY_APP_ID_BETA = "";
    public static final String MT_HOCKEY_APP_ID_DEV = MT_HOCKEY_APP_ID_QA;
    public static final String MT_HOCKEY_APP_ID_DEFAULT = MT_HOCKEY_APP_ID_QA;
    public static final String MT_HOCKEY_APP_ID_UAT = "05472a98222110a16f1d98bc20226693";

    // Google Analytics
    public static final String MT_GOOGLE_ANALYTICS_APP_ID_PROD = "UA-22972315-5";
    public static final String MT_GOOGLE_ANALYTICS_APP_ID_DEV = "UA-52492759-1";
    public static final String MT_GOOGLE_ANALYTICS_APP_ID_QA = "UA-52492759-2";
    public static final String MT_GOOGLE_ANALYTICS_APP_ID_DEFAULT = MT_GOOGLE_ANALYTICS_APP_ID_QA;
    public static final String MT_GOOGLE_ANALYTICS_APP_ID_UAT = "UA-22972315-5";
    public static final String MT_GOOGLE_ANALYTICS_APP_ID_BETA = "UA-22972315-5";

    // OpenLink


    public static String getWebServicesBaseURL() {
        String webServicesBaseURL = getWebServicesAPILocation();
        webServicesBaseURL = webServicesBaseURL.concat("/");
        webServicesBaseURL = webServicesBaseURL.concat(getCurrentWebAPIVersion());
        Log.d("MTConfig","Web service base URL: " + webServicesBaseURL);
        return webServicesBaseURL;
    }

    public static String getWebServicesAPILocation() {
        String apiEndPoint = null;

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_DEBUG) || BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_MONKEYTALK)) {
            if (BuildConfig.MT_DISTRIBUTION_TYPE == MT_DISTRIBUTION_TYPE_QA) {
                apiEndPoint = MT_API_END_POINT_QA;
            } else {
                apiEndPoint = MT_API_END_POINT_DEV;
            }

        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_RELEASE)) {
            switch (BuildConfig.MT_DISTRIBUTION_TYPE) {
                case MT_DISTRIBUTION_TYPE_PROD:
                    apiEndPoint = MT_API_END_POINT_PROD;
                    break;
                case MT_DISTRIBUTION_TYPE_QA:
                    apiEndPoint = MT_API_END_POINT_QA;
                    break;
                case MT_DISTRIBUTION_TYPE_UAT:
                    apiEndPoint = MT_API_END_POINT_UAT;
                    break;
                case MT_DISTRIBUTION_TYPE_BETA:
                    apiEndPoint = MT_API_END_POINT_BETA;
                    break;
                default:
                    apiEndPoint = MT_API_END_POINT_DEV;
            }
        }

        return apiEndPoint;
    }

    public static String getCurrentWebAPIVersion() {
        String apiEndPoint = null;

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_DEBUG) || BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_MONKEYTALK)) {
            if (BuildConfig.MT_DISTRIBUTION_TYPE == MT_DISTRIBUTION_TYPE_QA) {
                apiEndPoint = MT_API_VERSION_QA;
            } else {
                apiEndPoint = MT_API_VERSION_DEV;
            }

        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_RELEASE)) {
            switch (BuildConfig.MT_DISTRIBUTION_TYPE) {
                case MT_DISTRIBUTION_TYPE_PROD:
                    apiEndPoint = MT_API_VERSION_PROD;
                    break;
                case MT_DISTRIBUTION_TYPE_QA:
                    apiEndPoint = MT_API_VERSION_QA;
                    break;
                case MT_DISTRIBUTION_TYPE_UAT:
                    apiEndPoint = MT_API_VERSION_UAT;
                    break;
                case MT_DISTRIBUTION_TYPE_BETA:
                    apiEndPoint = MT_API_VERSION_BETA;
                    break;
                default:
                    apiEndPoint = MT_API_VERSION_DEV;
            }
        }

        return apiEndPoint;
    }

    public static String getAPISecret() {
        String apiSecret = null;

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_DEBUG) || BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_MONKEYTALK)) {
            if (BuildConfig.MT_DISTRIBUTION_TYPE == MT_DISTRIBUTION_TYPE_QA) {
                apiSecret = MT_API_SECRET_QA;
            } else {
                apiSecret = MT_API_SECRET_DEV;
            }
        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_RELEASE)) {
            switch (BuildConfig.MT_DISTRIBUTION_TYPE) {
                case MT_DISTRIBUTION_TYPE_PROD:
                    apiSecret = MT_API_SECRET_PROD;
                    break;
                case MT_DISTRIBUTION_TYPE_QA:
                    apiSecret = MT_API_SECRET_QA;
                    break;
                case MT_DISTRIBUTION_TYPE_UAT:
                    apiSecret = MT_API_SECRET_UAT;
                    break;
                case MT_DISTRIBUTION_TYPE_BETA:
                    apiSecret = MT_API_SECRET_BETA;
                    break;
                default:
                    apiSecret = MT_API_SECRET_DEV;
            }
        }

        return apiSecret;
    }

    public static String getServerToken() {
        String serverToken = null;

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_DEBUG) || BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_MONKEYTALK)) {
            if (BuildConfig.MT_DISTRIBUTION_TYPE == MT_DISTRIBUTION_TYPE_QA) {
                serverToken = MT_API_SERVER_TOKEN_QA;
            } else {
                serverToken = MT_API_SERVER_TOKEN_DEV;
            }
        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_RELEASE)) {
            switch (BuildConfig.MT_DISTRIBUTION_TYPE) {
                case MT_DISTRIBUTION_TYPE_PROD:
                    serverToken = MT_API_SERVER_TOKEN_PROD;
                    break;
                case MT_DISTRIBUTION_TYPE_QA:
                    serverToken = MT_API_SERVER_TOKEN_QA;
                    break;
                case MT_DISTRIBUTION_TYPE_UAT:
                    serverToken = MT_API_SERVER_TOKEN_UAT;
                    break;
                case MT_DISTRIBUTION_TYPE_BETA:
                    serverToken = MT_API_SERVER_TOKEN_BETA;
                    break;
                default:
                    serverToken = MT_API_SERVER_TOKEN_DEV;
            }
        }

        return serverToken;
    }

    public static String getGoogleAnalyticsId() {
        String id = MT_GOOGLE_ANALYTICS_APP_ID_DEFAULT;

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_DEBUG) || BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_MONKEYTALK)) {
            if (BuildConfig.MT_DISTRIBUTION_TYPE == MT_DISTRIBUTION_TYPE_QA) {
                id = MT_GOOGLE_ANALYTICS_APP_ID_QA;
            } else {
                id = MT_GOOGLE_ANALYTICS_APP_ID_DEV;
            }

        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_RELEASE)) {
            switch (BuildConfig.MT_DISTRIBUTION_TYPE) {
                case MT_DISTRIBUTION_TYPE_PROD:
                    id = MT_GOOGLE_ANALYTICS_APP_ID_PROD;
                    break;
                case MT_DISTRIBUTION_TYPE_QA:
                    id = MT_GOOGLE_ANALYTICS_APP_ID_QA;
                    break;
                case MT_DISTRIBUTION_TYPE_UAT:
                    id = MT_GOOGLE_ANALYTICS_APP_ID_UAT;
                    break;
                case MT_DISTRIBUTION_TYPE_BETA:
                    id = MT_GOOGLE_ANALYTICS_APP_ID_BETA;
                    break;
                default:
                    id = MT_GOOGLE_ANALYTICS_APP_ID_DEV;
            }
        }

        return id;
    }

    public static String getHockeyAppID() {

        Log.i("MTConfig", "BuildType: " + BuildConfig.BUILD_TYPE + " DistributionType: " + BuildConfig.MT_DISTRIBUTION_TYPE);

        String appID = MT_HOCKEY_APP_ID_DEFAULT;

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_DEBUG) || BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_MONKEYTALK)) {
            if (BuildConfig.MT_DISTRIBUTION_TYPE == MT_DISTRIBUTION_TYPE_QA) {
                appID = MT_HOCKEY_APP_ID_QA;
            } else {
                // Standard dev build
                appID = MT_HOCKEY_APP_ID_DEV;
            }
        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(MT_BUILD_TYPE_RELEASE)) {

            // Release build
            if (BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_PROD)) {
                appID = MT_HOCKEY_APP_ID_PROD;
            } else if (BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_QA)) {
                appID = MT_HOCKEY_APP_ID_QA;
            } else if (BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_UAT)) {
                appID = MT_HOCKEY_APP_ID_UAT;
            } else if (BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_BETA)) {
                appID = MT_HOCKEY_APP_ID_BETA;
            } else {
                appID = MT_HOCKEY_APP_ID_DEV;
            }
        }

        Log.i("MTConfig", "HockeyApp ID: " + appID + " for distribution type: " + BuildConfig.MT_DISTRIBUTION_TYPE);

        return appID;
    }

    public static boolean isProduction() {
        return (BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_PROD));
    }

    public static boolean isBeta() {
        return (BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_BETA));
    }

    public static boolean isExternalRelease() {
        return (BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_PROD) ||
                BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_UAT)) ||
                BuildConfig.MT_DISTRIBUTION_TYPE.equalsIgnoreCase(MT_DISTRIBUTION_TYPE_BETA);
    }

    public static String getDistributionTypeVersionString() {
        String versionString = BuildConfig.MT_DISTRIBUTION_TYPE + " : v" +
                MilwaukeeToolApplication.getApplicationVersionName(MilwaukeeToolApplication.getAppContext()) + " (" +
                MilwaukeeToolApplication.getApplicationVersionCode(MilwaukeeToolApplication.getAppContext()) + ")";
        return versionString;
    }
}
