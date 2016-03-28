package com.tripasfactory.thetripaslibrary;

public class Configs {

    // ----------- GENERIC ---------------

    // Flag that determinates if app if for production version
    public static final boolean HAS_ENDPOINT_SELECTOR = true;
    // Splash screen duration time (ms)
    public static final int SPLASH_SCREEN_DURATION_TIME = 3000;

    public static final int SESSION_TIMEOUT = 480000;

    // ----------- LOGGING ---------------

    // Enables the use of the crashlytics plugin
    public static final boolean CRASHLYTICS = false;

    // ----------- COMMS ---------------
    // Connection timeout duration
    public static final int TIMEOUT_CONNECTION = 60000;
    // Number of threads running in parallel
    public static final int NBR_OF_THREADS_TO_PROCESS_REQUESTS = 8;

    // ----------- SECURITY ---------------
    // Secure cache
    public static final boolean ENCRYPT_CACHE = true;

    // Secure screen
    public static final boolean SHOULD_BLOCK_TAPJACKING = true;

    // Secure screen
    public static final boolean SHOULD_BLOCK_SCREENSHOTS = false;

    // Should detect rooted devices
    public static final boolean SHOULD_DETECT_ROOTED = true;
    // Should block rooted devices
    public static final boolean SHOULD_BLOCK_ROOTED = false;

    // Should check public key
    public static final boolean SHOULD_CHECK_SSL_PINNING = true;
    // Should check certificate not server trusted
    public static final boolean SHOULD_CHECK_TRUSTED_ON_SSL_PINNING_FAIL = false;
    // Should block certificate not server trusted
    public static final boolean SHOULD_BLOCK_TRUSTED_ON_SSL_PINNING_FAIL = false;
    // Should block public key
    public static final boolean SHOULD_BLOCK_CONNECTION_ON_SSL_PINNING_FAIL = false;

    // Should check connectivity status
    public static final boolean SHOULD_DETECT_CONNECTIVITY_STATUS = true;
    // Should block connectivity
    public static final boolean SHOULD_BLOCK_CONNECTIVITY_STATUS = false;

    // Logs handling
    public static final boolean HIDE_VERBOSE_LOGGING = false;

    // Shows Login prompt dialog after app minimized thus put to foreground
    public static final boolean RESTORE_FOREGROUND_PROTECTION = false;

    // Device support: false -> only phone is allowed
    public static final boolean DEVICE_ALLOW_ALL = false;
}