package com.tripasfactory.thetripaslibrary;

import android.content.Context;
import android.content.res.Configuration;

import com.tripasfactory.thetripaslibrary.Base.BaseActivity;
import com.tripasfactory.thetripaslibrary.Utils.L;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.Locale;

public class LocaleConfiguration {

    private static final String TAG = LocaleConfiguration.class
            .getSimpleName();

    //-------------- Change language ----------------

    public static void setLocale(Context context) {
        String storedLanguage = ConfigData.getLanguage();
        Locale locale = null;
        if (storedLanguage == null) {
            locale = context.getResources().getConfiguration().locale;
            storedLanguage = locale.toString();
            L.d(TAG, "Language: " + storedLanguage);

            if (storedLanguage.startsWith("pt")) {
                locale = new Locale("pt", "PT");
            } else {
                locale = new Locale("en", "EN");
            }
            storedLanguage = locale.toString();
            ConfigData.setLanguage(storedLanguage);
        } else {
            locale = new Locale(storedLanguage);
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);

    }

    public static void setLanguageEN(BaseActivity activity) {
        // On language change clear cache
        activity.getSpiceManager().removeAllDataFromCache();
        try {
            FileUtils.cleanDirectory(activity.getCacheDir());
        } catch (IOException e) {
            L.e(TAG, "Could not clean cache directory: " + e.getMessage());
        }

        Locale tempLocale = new Locale("en");
        Locale.setDefault(tempLocale);
        ConfigData.setLanguage(tempLocale.toString());
    }

    public static void setLanguagePT(BaseActivity activity) {
        // On language change clear cache
        activity.getSpiceManager().removeAllDataFromCache();
        try {
            FileUtils.cleanDirectory(activity.getCacheDir());
        } catch (IOException e) {
            L.e(TAG, "Could not clean cache directory: " + e.getMessage());
        }

        Locale tempLocale = new Locale("pt");
        Locale.setDefault(tempLocale);
        ConfigData.setLanguage(tempLocale.toString());
    }
}
