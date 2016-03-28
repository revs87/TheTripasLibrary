package com.tripasfactory.thetripaslibrary.Communications;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tripasfactory.thetripaslibrary.ConfigData;

import java.net.CookieManager;

public class CommunicationCenter {

    /* ENDPOINTS ---------(set here ONLY.)--------- */
    public static final String TEST_SERVER_URL = "http://example.com";

    /* -------------------------------------------- */


    private static final CookieManager cookieManager = new CookieManager();

    public static CookieManager getCookieManager() {
        return cookieManager;
    }

    public static String getBaseUrl() {
        if (ConfigData.getEndpoint() != null) {
            return ConfigData.getEndpoint();
        } else {
            return getServerPath();
        }
    }

    public static String getDefaultBaseUrl() {
        return getServerPath();
    }

    private static String getServerPath() {
        return TEST_SERVER_URL;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected())
                || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

}
