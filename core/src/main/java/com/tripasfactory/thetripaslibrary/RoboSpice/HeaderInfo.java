package com.tripasfactory.thetripaslibrary.RoboSpice;


import com.tripasfactory.thetripaslibrary.Base.ApplicationClass;
import com.tripasfactory.thetripaslibrary.ConfigData;

public class HeaderInfo {

    public static final String ANDROID_PHONE = "ANDROIDPHONE";
    private static final String ANDROID_TABLET_SW600 = "ANDROIDTABLETSW600";
    private static final String ANDROID_TABLET_SW720 = "ANDROIDTABLET";

    public static final String ANDROID_DEVICE = "ANDROIDDEVICE";

    private static String appVersion;

    public static String getPhoneModel() {
        if (ApplicationClass.getDevice() == ApplicationClass.DEVICE_PHONE) {
            return ANDROID_PHONE;
        } else if (ApplicationClass.getDevice() == ApplicationClass.DEVICE_SW_600) {
            return ANDROID_TABLET_SW600;
        } else {
            return ANDROID_TABLET_SW720;
        }
    }

    public static String getDEVICE() {
        return ANDROID_DEVICE;
    }

    public static String getSO() {
        return getPhoneModel();
    }

    public static String getApiLevel() {
        return String.valueOf(android.os.Build.VERSION.SDK_INT);
    }

    public static String getAppVersion() {
        return appVersion;
    }

    public static void setAppVersion(String appVersionAux) {
        appVersion = appVersionAux;
    }

    public static String getLanguage() {
        if (ConfigData.getLanguage().equals("pt")) {
            return "pt-PT";
        } else if (ConfigData.getLanguage().equals("en")) {
            return "en-EN";
        }

        return ConfigData.getLanguage().replaceAll("_", "-");
    }

}
