package com.tripasfactory.thetripaslibrary.Database;

import android.content.Context;

/**
 * @description Class used to memory cache management.
 */

public class PersistentData {

    private static final String TAG = PersistentData.class.getSimpleName();

    // Persistent Data
    private static PersistentData persistentData;
    private String username;
    private static CustomSQLiteHelper databaseHelper = null;
    private int numberOfUnreadMessages = 0;

    /**
     * Constructor
     */
    private PersistentData() {
    }

    /*
     * Get Singleton
     */
    public static synchronized PersistentData getSingleton() {
        if (persistentData == null) {
            persistentData = new PersistentData();
        }
        return persistentData;
    }

    public CustomSQLiteHelper getDatabaseHelper() {
        return databaseHelper;
    }
    public void initDatabaseHelper(Context context) {
        databaseHelper = new CustomSQLiteHelper(context);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
