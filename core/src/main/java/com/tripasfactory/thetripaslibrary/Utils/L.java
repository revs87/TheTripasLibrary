package com.tripasfactory.thetripaslibrary.Utils;


import android.util.Log;

import com.tripasfactory.thetripaslibrary.Configs;


/**
 * Created by Rui on 02/09/2015.
 */
public final class L {

    public static void v(String a, String b) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.v(a, b);
        }
    }

    public static void v(String a, String b, Throwable tr) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.v(a, b, tr);
        }
    }

    public static void i(String a, String b) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.i(a, b);
        }
    }

    public static void i(String a, String b, Throwable tr) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.i(a, b, tr);
        }
    }

    public static void d(String a, String b) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.d(a, b);
        }
    }

    public static void d(String a, String b, Throwable tr) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.d(a, b, tr);
        }
    }

    public static void e(String a, String b) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.e(a, b);
        }
    }

    public static void e(String a, String b, Throwable tr) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.e(a, b, tr);
        }
    }

    public static void w(String a, String b) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.w(a, b);
        }
    }

    public static void w(String a, String b, Throwable tr) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.w(a, b, tr);
        }
    }

    public static void wtf(String a, String b) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.wtf(a, b);
        }
    }

    public static void wtf(String a, String b, Throwable tr) {
        if (!Configs.HIDE_VERBOSE_LOGGING) {
            Log.wtf(a, b, tr);
        }
    }
}
