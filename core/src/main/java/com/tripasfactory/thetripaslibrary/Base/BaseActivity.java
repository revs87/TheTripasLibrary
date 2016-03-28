package com.tripasfactory.thetripaslibrary.Base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Controllers.AutoResizeTextView;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.RoboSpice.CacheableSpringAndroidSpiceService;
import com.tripasfactory.thetripaslibrary.RoboSpice.SpiceManagerEncrypted;
import com.tripasfactory.thetripaslibrary.Utils.L;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    /* Restore Foreground protection screen */
    public static final String SHOW_PROTECTION = "SHOW_PROTECTION";
    public static boolean isInBackground = false;
    public static boolean showRestoreForegroundLoginScreen = false;
    public static boolean pushedBackButtonBackToolbar = false;
    private static RestoreForegroundDialogFragment restoreForegroundDialogFragment;
    private boolean showMandatoryProtection;


    private SpiceManagerEncrypted spiceManager = new SpiceManagerEncrypted(
            CacheableSpringAndroidSpiceService.class);
    private Spinner menuIconSpinner;
    private int selectedPosition;
    private int checked;
    private boolean userIsInteracting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showRestoreForegroundLoginScreen = false;

        /*
        * Case scenario:
        *    Events: TaskExecute -> Minimize -> IntentInflateActivityOnSuccess (opens unprotected activity)
        *    Usage: BaseIntent
        * */
        if (getIntent() != null) {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                showMandatoryProtection = b.getBoolean(BaseActivity.SHOW_PROTECTION, false);
                if (showMandatoryProtection) {
                    showRestoreForegroundLoginScreen = true;
                }
            }
        }

        /*
         * if (ApplicationClass.getDevice() == ApplicationClass.DEVICE_PHONE) {
         * setRequestedOrientation
         * (ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT); } else if
         * (ApplicationClass.getDevice() == ApplicationClass.DEVICE_SW_600) {
         * setRequestedOrientation
         * (ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); }
         */

        if (ApplicationClass.getDevice() == ApplicationClass.DEVICE_PHONE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        /* DARKSCREEN preview and SCREENSHOTS blockage */
        if (Configs.SHOULD_BLOCK_SCREENSHOTS) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        L.v(TAG, "BaseActivity onCreate");

        spiceManager.start(this);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    public void setupToolbar(@Nullable Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_apps_white_24dp);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setActionBarTitle(String title) {
        AutoResizeTextView toolBarTitleTv = ((AutoResizeTextView) findViewById(R.id.toolbar_title_tv));
        toolBarTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        String titleCaps = title/*.toUpperCase()*/;
        toolBarTitleTv.setText(titleCaps);
    }

    public void setActionBarAsUpEnabled(int id) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setHomeButtonEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pushedBackButtonBackToolbar = true;
    }

    @Override
    public void finish() {
        super.finish();
        pushedBackButtonBackToolbar = true;
    }

    /* Whenever the click occurs outside the soft-keyboard, the soft-keyboard closes. */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if (view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;

                if (viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0]
                                    + view.getWidth(),
                            coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if (rect.contains(x, y)) {
                        return consumed;
                    }
                } else if (viewNew instanceof EditText) {
                    return consumed;
                }

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(
                        viewNew.getWindowToken(), 0);

                viewNew.clearFocus();

                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        L.v(TAG, "BaseActivity onDestroy");
        try {
            spiceManager.shouldStop();
        } catch (Exception e) {
            L.w(TAG, "SpiceManager couldn't stop", e);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        ApplicationClass.setCurrentActivity(this);
        super.onResume();
        L.v(TAG, "BaseActivity onResume");
        isInBackground = false;

        if ((Configs.RESTORE_FOREGROUND_PROTECTION
                && showMandatoryProtection)
                || (Configs.RESTORE_FOREGROUND_PROTECTION
                && showRestoreForegroundLoginScreen
                && !pushedBackButtonBackToolbar
                && (
//                        ApplicationClass.getCurrentActivity() instanceof PrivateActivity ||
                ApplicationClass.getCurrentActivity() instanceof ProtectedBaseActivity))
                ) {

            /*
            * Restore Foreground
            * Background to Foreground DialogFragment
            * */
            if (restoreForegroundDialogFragment == null) {
                restoreForegroundDialogFragment = RestoreForegroundDialogFragment.newInstance();
                restoreForegroundDialogFragment.setCancelable(false);
            }

            if (!restoreForegroundDialogFragment.isVisible()) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                restoreForegroundDialogFragment.show(fragmentManager, TAG);
            }
        }

        if (pushedBackButtonBackToolbar) {
            pushedBackButtonBackToolbar = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.v(TAG, "onPause");
        isInBackground = true;
        showRestoreForegroundLoginScreen = true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.v(TAG, "onActivityResult");
        showRestoreForegroundLoginScreen = false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public SpiceManagerEncrypted getSpiceManager() {
        return spiceManager;
    }

}
