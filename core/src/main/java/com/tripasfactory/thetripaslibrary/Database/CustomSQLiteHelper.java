package com.tripasfactory.thetripaslibrary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tripasfactory.thetripaslibrary.Utils.L;


public class CustomSQLiteHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public CustomSQLiteHelper(Context context) {
        super(context.getApplicationContext(), DatabaseConsts.DATABASE_NAME,
                null, DatabaseConsts.DATABASE_VERSION);
        db = super.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(getConfigTableSQL());
        database.execSQL(getDataCacheTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.w(CustomSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
//		 db.execSQL("DROP TABLE IF EXISTS " + DatabaseConsts.TABLE_CONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConsts.TABLE_DATA_CACHE);
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        while (db.isDbLockedByCurrentThread()) {
            // db is locked, keep looping
        }
        return db;
    }

    ;

    @Override
    public SQLiteDatabase getReadableDatabase() {
        while (db.isDbLockedByCurrentThread()) {
            // db is locked, keep looping
        }
        return db;
    }

    ;

    private String getConfigTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + DatabaseConsts.TABLE_CONFIG
                + "(" + DatabaseConsts.CONFIG_KEY + " TEXT PRIMARY KEY, "
                + DatabaseConsts.CONFIG_VALUE + " TEXT);";
    }

    private String getDataCacheTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + DatabaseConsts.TABLE_DATA_CACHE
                + "(" + DatabaseConsts.DATA_CACHE_KEY + " TEXT PRIMARY KEY, "
                + DatabaseConsts.DATA_CACHE_OBJECT + " BLOB, "
                + DatabaseConsts.DATA_CACHE_TIMESTAMP + " INTEGER);";
    }

    @Override
    protected void finalize() throws Throwable {
        db.close();
        super.finalize();
    }
}
