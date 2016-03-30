package com.tripasfactory.thetripaslibrary.Base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tripasfactory.thetripaslibrary.Communications.CommunicationCenter;
import com.tripasfactory.thetripaslibrary.Components.CustomDialogFragment;
import com.tripasfactory.thetripaslibrary.ConfigData;
import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Interfaces.CertificateValidationInterface;
import com.tripasfactory.thetripaslibrary.LocaleConfiguration;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.Security.FetchPubKeyTask;
import com.tripasfactory.thetripaslibrary.Security.RootUtil;
import com.tripasfactory.thetripaslibrary.Utils.L;

public class FirstScreenActivity extends FragmentActivity {

    private static final String TAG = FirstScreenActivity.class
            .getSimpleName();

    private boolean isPinnedEvaluated;
    private boolean isRootedEvaluated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_first_screen);

        // use a default value using new Date()
        String storedEndpoint = ConfigData.getEndpoint();
        if (storedEndpoint == null) {
            storedEndpoint = CommunicationCenter.getDefaultBaseUrl();
            L.d(TAG, "Endpoint: " + storedEndpoint);

            ConfigData.setEndpoint(storedEndpoint);
        }

        // use a default value using new Date()
        LocaleConfiguration.setLocale(this);


        if (isOnline(this)) {
            configCertificateCheck();
            configRootDetection();
        } else {
            final CustomDialogFragment dialog =
                    CustomDialogFragment
                            .newInstance(
                                    getResources()
                                            .getString(
                                                    R.string.error_generic_title),
                                    "Timeout",
                                    false);

            dialog.setConfirmButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Configs.SHOULD_BLOCK_CONNECTIVITY_STATUS) {
                        dialog.dismiss();
                        finish();
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), TAG);
            dialog.setCancelable(false);
        }

    }


    private void configCertificateCheck() {
        if (Configs.SHOULD_CHECK_SSL_PINNING) {
            new FetchPubKeyTask(this, ConfigData.getEndpoint(), new CertificateValidationInterface() {
                @Override
                public void onAccept() {
                    isPinnedEvaluated = true;
                    continueToActivity();
                }

                @Override
                public void onTimeout() {
                    final CustomDialogFragment dialog;
                    dialog =
                            CustomDialogFragment
                                    .newInstance(
                                            getResources()
                                                    .getString(
                                                            R.string.error_generic_title),
                                            "Timeout",
                                            false);

                    dialog.setConfirmButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Configs.SHOULD_BLOCK_CONNECTION_ON_SSL_PINNING_FAIL) {
                                dialog.dismiss();
                                finish();
                            } else {
                                isPinnedEvaluated = true;
                                continueToActivity();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setCancelable(false);
                    try {
                        dialog.show(getSupportFragmentManager(), TAG);
                    } catch (IllegalStateException e) {

                    }
                }

                @Override
                public void onDeny() {
                    final CustomDialogFragment dialog;
                    dialog =
                            CustomDialogFragment
                                    .newInstance(
                                            getResources()
                                                    .getString(
                                                            R.string.error_generic_title),
                                            "Insecure network",
                                            false);

                    dialog.setConfirmButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Configs.SHOULD_BLOCK_CONNECTION_ON_SSL_PINNING_FAIL) {
                                dialog.dismiss();
                                finish();
                            } else {
                                isPinnedEvaluated = true;
                                continueToActivity();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setCancelable(false);
                    try {
                        dialog.show(getSupportFragmentManager(), TAG);
                    } catch (IllegalStateException e) {

                    }
                }
            }).execute();
        } else {
            isPinnedEvaluated = true;
            continueToActivity();
        }
    }

    private void configRootDetection() {
        if (Configs.SHOULD_DETECT_ROOTED && RootUtil.isDeviceRooted()) {
            final CustomDialogFragment dialog;
            dialog =
                    CustomDialogFragment
                            .newInstance(
                                    getResources()
                                            .getString(
                                                    R.string.error_generic_title),
                                    "Device rooted",
                                    false);

            dialog.setConfirmButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Configs.SHOULD_BLOCK_ROOTED) {
                        dialog.dismiss();
                        finish();
                    } else {
                        isRootedEvaluated = true;
                        continueToActivity();
                        dialog.dismiss();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), TAG);
            dialog.setCancelable(false);
        } else {
            isRootedEvaluated = true;
            continueToActivity();
        }
    }


    private void continueToActivity() {
        if (isPinnedEvaluated
                && isRootedEvaluated) {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    if (Configs.DEVICE_ALLOW_ALL) {
                        proceedToPublicActivity();
                    } else {
                        /* Allow only phone */
                        if (ApplicationClass.getDevice() != ApplicationClass.DEVICE_PHONE) {
                            final CustomDialogFragment dialog = CustomDialogFragment
                                    .newInstance(
                                            getResources().getString(
                                                    R.string.error_generic_title),
                                            "Device not supported"
                                            , false);

                            dialog.setConfirmButtonListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();
                                }

                            });

                            dialog.show(getSupportFragmentManager(), "Not supported");

                        } else {
                            proceedToPublicActivity();
                        }
                    }

                    overridePendingTransition(R.animator.fade_in,
                            R.animator.fade_out);

                }
            }, Configs.SPLASH_SCREEN_DURATION_TIME);
        }
    }

    public static boolean isOnline(Activity context) {
        if (Configs.SHOULD_DETECT_CONNECTIVITY_STATUS) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null) {
                return netInfo.isConnectedOrConnecting();
            }
            return false;
        }
        return true;
    }

    protected void proceedToPublicActivity() {
        // TODO - proceed
    }

}
