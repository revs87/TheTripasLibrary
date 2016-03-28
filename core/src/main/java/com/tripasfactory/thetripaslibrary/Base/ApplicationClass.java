package com.tripasfactory.thetripaslibrary.Base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.tripasfactory.thetripaslibrary.ConfigData;
import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Database.PersistentData;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.Utils.L;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationClass extends MultiDexApplication {

    /*
     * FLAGS
     */
    private static final String TAG = ApplicationClass.class.getSimpleName();

    private static Context context;
    private static int device;
    public static final int DEVICE_PHONE = 1;
    public static final int DEVICE_SW_600 = 600;
    public static final int DEVICE_SW_720 = 720;

    public final static String ACTIVATED_LOGIN_TAG = "ActivatedLoginTagForSharedPrefs";
    public final static String ENROLLED_LOGIN_TAG = "ActivatedLoginTagForSharedPrefs2";

    public static final String STANDARD_CURRENCY = "MZN";

    public static String FAV_DIALOG = "FAV_DIALOG";


    // preferences
    SharedPreferences sharedPref;

    /* Record current Activity */
    private static BaseActivity currentActivity;

    public static BaseActivity getCurrentActivity() {
        return ApplicationClass.currentActivity;
    }

    protected static void setCurrentActivity(BaseActivity currentActivity) {
        ApplicationClass.currentActivity = currentActivity;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor editor = sharedPref.edit();
        editor.putBoolean(FAV_DIALOG, false);
        editor.commit();

        ApplicationClass.context = getApplicationContext();

        if (getResources().getBoolean(R.bool.isSw720)) {
            setDevice(DEVICE_SW_720);
        } else if (getResources().getBoolean(R.bool.isSw600)) {
            setDevice(DEVICE_SW_600);
        } else {
            setDevice(DEVICE_PHONE);
        }

        // Start calligraphy library
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        // Inits the database
        PersistentData.getSingleton().initDatabaseHelper(context);

        L.v(TAG, "ApplicationClass onCreate");

        /*
         * // Start LocalServer if (useLocalServer) { Intent localServerIntent =
         * new Intent(this, LocalServer.class); startService(localServerIntent);
         * }
         */

        // Start crashlytics
        if (Configs.CRASHLYTICS) {
//            Crashlytics.start(this);
            Fabric.with(this, new Crashlytics());
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    public String getLocale() {
        return getBaseContext().getResources().getConfiguration().locale.toString();
    }

    public void setLocale() {
        String storedLanguage = ConfigData.getLanguage();
        Locale locale = null;
        if (storedLanguage == null) {
            locale = getBaseContext().getResources().getConfiguration().locale;
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
        getBaseContext().getResources().updateConfiguration(config, null);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return ApplicationClass.context;
    }

    public static void setDevice(int device) {
        ApplicationClass.device = device;
    }

    public static int getDevice() {
        return device;
    }

}
