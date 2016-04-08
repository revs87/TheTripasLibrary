package com.tripasfactory.thetripaslibrary.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.provider.Settings;

import com.tripasfactory.thetripaslibrary.Base.BaseActivity;
import com.tripasfactory.thetripaslibrary.Manager.Contextor;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rvieira on 03/03/2016.
 */
public class Utils {

    private static Utils instance;
    private Context mContext;

    public static Utils getInstance() {
        if (instance == null)
            instance = new Utils();
        return instance;
    }

    private Utils() {
        mContext = Contextor.getInstance().getContext();
    }

    public String getDeviceId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getVersionName() {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            return "1.0";
        }
    }

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    private final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /*
    * Prompts Messaging dialog
    * */
    public void sendSMS(Activity activity, String smsContent) {
        BaseActivity.pushedBackButtonBackToolbar = true;

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("sms_body", smsContent);
        sendIntent.putExtra("compose_mode", true);
        activity.startActivity(Intent.createChooser(sendIntent, "SMS: "));
    }

    /*
    * Prompts Email dialog
    * */
    public void sendEmail(Activity activity, String emailSubject, String emailContent) {
        BaseActivity.pushedBackButtonBackToolbar = true;

        Intent mEmail = new Intent(Intent.ACTION_SEND);
        mEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{});
        mEmail.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        mEmail.putExtra(Intent.EXTRA_TEXT, emailContent);
        // prompts to choose email client
        mEmail.setType("message/rfc822");
        activity.startActivity(Intent.createChooser(mEmail, "Choose an email client to send your"));
    }


    /**
     * Useful for XML objects merge during incremental workflow
     */
    public void merge(Object obj, Object update) {
        if (!obj.getClass().isAssignableFrom(update.getClass())) {
            return;
        }

        Method[] methods = obj.getClass().getMethods();

        for (Method fromMethod : methods) {
            if (fromMethod.getDeclaringClass().equals(obj.getClass())
                    && fromMethod.getName().startsWith("get")
                    && !fromMethod.getName().equals("getCategoriesList")
                    ) {

                String fromName = fromMethod.getName();
                String toName = fromName.replace("get", "set");

                try {
                    Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
                    Object value = fromMethod.invoke(update, (Object[]) null);
                    if (value != null) {
                        toMetod.invoke(obj, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
