package com.tripasfactory.thetripaslibrary.Utils;

import android.app.Activity;
import android.view.WindowManager;

import com.tripasfactory.thetripaslibrary.Configs;

public class BlockScreenshots {

    private static final int FLAG = WindowManager.LayoutParams.FLAG_SECURE;

    public static void blockScreenShot(Activity activity) {
        if (Configs.SHOULD_BLOCK_SCREENSHOTS) {
            activity.getWindow().setFlags(FLAG, FLAG);
        }
    }
}
