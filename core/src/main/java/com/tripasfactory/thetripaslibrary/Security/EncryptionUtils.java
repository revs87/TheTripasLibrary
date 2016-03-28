package com.tripasfactory.thetripaslibrary.Security;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.tripasfactory.thetripaslibrary.Base.ApplicationClass;

import java.security.GeneralSecurityException;


public class EncryptionUtils {

    public final static String TAG = EncryptionUtils.class.getSimpleName();

    public static String decryptData(String tag) {
        String messageAfterDecrypt = "";
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ApplicationClass.getAppContext());
            String encryptedData = sharedPref.getString(tag, null);

            if (encryptedData == null) {
                return null;
            }

            String android_id = Settings.Secure.getString(ApplicationClass.getAppContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            try {
                messageAfterDecrypt = AESCrypt.decrypt(android_id, encryptedData);
            } catch (GeneralSecurityException e) {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
        //Log.d(TAG, tag + ": Decrypted message: " + messageAfterDecrypt);

        return messageAfterDecrypt;
    }

    public static boolean encryptData(String data, String tag) {
        try {

            String android_id = Settings.Secure.getString(ApplicationClass.getAppContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            try {
                String encryptedMsg = AESCrypt.encrypt(android_id, data);

                //Log.d(TAG, tag + ": Encrypted message: " + encryptedMsg);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ApplicationClass.getAppContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(tag, encryptedMsg);
                editor.commit();

                return true;
            } catch (GeneralSecurityException e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean removeData(String tag) {
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ApplicationClass.getAppContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(tag);
            editor.commit();

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String encryptDataString(String data) {
        try {
            String android_id = Settings.Secure.getString(ApplicationClass.getAppContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            try {
                String encryptedMsg = AESCrypt.encrypt(android_id, data);
                return encryptedMsg;
            } catch (GeneralSecurityException e) {
                return data;
            }
        } catch (Exception e) {
            return data;
        }
    }
}
