package com.tripasfactory.thetripaslibrary.Base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tripasfactory.thetripaslibrary.Base.BaseActivity;

@SuppressLint("ParcelCreator")
public class BaseIntent extends Intent {

    public BaseIntent() {
        super();
        putExtraMandatoryProtection();
    }

    public BaseIntent(Activity activity, Class<?> baseClass) {
        super(activity, baseClass);
        putExtraMandatoryProtection();
    }

    public BaseIntent(Context context, Class<?> baseClass) {
        super(context, baseClass);
        putExtraMandatoryProtection();
    }

    private void putExtraMandatoryProtection() {
        if (BaseActivity.isInBackground) {
            this.putExtra(BaseActivity.SHOW_PROTECTION, true);
        }
    }

}
