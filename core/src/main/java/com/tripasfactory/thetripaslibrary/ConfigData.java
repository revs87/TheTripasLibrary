package com.tripasfactory.thetripaslibrary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tripasfactory.thetripaslibrary.Database.DatabaseConsts;
import com.tripasfactory.thetripaslibrary.Database.PersistentData;
import com.tripasfactory.thetripaslibrary.Utils.L;

public class ConfigData {

    private static final String TAG = "ConfigData";

    public static void setVersion(String version) {
        setValue(DatabaseConsts.CONFIG_KEY_VERSION, version);
    }

    public static String getVersion() {
        return getValueFromKey(DatabaseConsts.CONFIG_KEY_VERSION);
    }

    public static void setLanguage(String language) {
        setValue(DatabaseConsts.CONFIG_KEY_LANGUAGE, language);
    }

    public static String getLanguage() {
        return getValueFromKey(DatabaseConsts.CONFIG_KEY_LANGUAGE);
    }

    public static void setTimeout(String timeout) {
        L.d(TAG, "Set Timeout: " + timeout);
        setValue(DatabaseConsts.CONFIG_KEY_TIMEOUT, timeout);
    }

    public static String getTimeout() {
        L.d(TAG, "Get Timeout: "
                + getValueFromKey(DatabaseConsts.CONFIG_KEY_TIMEOUT));
        return getValueFromKey(DatabaseConsts.CONFIG_KEY_TIMEOUT);
    }

    public static void setEndpoint(String endpoint) {
        setValue(DatabaseConsts.CONFIG_KEY_ENDPOINT, endpoint);
    }

    public static String getEndpoint() {
        return getValueFromKey(DatabaseConsts.CONFIG_KEY_ENDPOINT);
    }

    private static void setValue(String key, String value) {
        SQLiteDatabase database = PersistentData.getSingleton()
                .getDatabaseHelper().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseConsts.CONFIG_KEY, key);
        values.put(DatabaseConsts.CONFIG_VALUE, value);

        database.insertWithOnConflict(DatabaseConsts.TABLE_CONFIG, null,
                values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    private static String getValueFromKey(String key) {
        SQLiteDatabase database = PersistentData.getSingleton()
                .getDatabaseHelper().getReadableDatabase();
        Cursor cursor = database.query(DatabaseConsts.TABLE_CONFIG,
                new String[]{DatabaseConsts.CONFIG_VALUE},
                DatabaseConsts.CONFIG_KEY + " = '" + key + "'", null, null,
                null, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        String value = cursor.getString(0);
        cursor.close();
        return value;
    }

}
