package com.tripasfactory.thetripaslibrary.Database;

public class DatabaseConsts {

	// Database Name
	public static final String DATABASE_NAME = "tripas.db";
	// Database Version
	public static final int DATABASE_VERSION = 4;

	// Tables Names
	public static final String TABLE_CONFIG = "config";
	public static final String TABLE_DATA_CACHE = "cache";

	// Field Names for Table TABLE_CONFIG
	public static final String CONFIG_KEY = "key";
	public static final String CONFIG_VALUE = "value";
	
	// Columns for Table TABLE_CONFIG
	public static final String CONFIG_KEY_VERSION = "version";
	public static final String CONFIG_KEY_LANGUAGE = "language";
	public static final String CONFIG_KEY_TIMEOUT = "timeout";
	public static final String CONFIG_KEY_ENDPOINT = "timeout";
	
	// Columns for TABLE_DATA_CACHE
	public static final String DATA_CACHE_KEY = "key";
	public static final String DATA_CACHE_OBJECT = "object";
	public static final String DATA_CACHE_TIMESTAMP = "timestamp";
	
}
