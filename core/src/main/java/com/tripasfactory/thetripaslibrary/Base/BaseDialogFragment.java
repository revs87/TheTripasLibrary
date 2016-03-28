package com.tripasfactory.thetripaslibrary.Base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.octo.android.robospice.SpiceManager;
import com.tripasfactory.thetripaslibrary.RoboSpice.CacheableSpringAndroidSpiceService;

public class BaseDialogFragment extends DialogFragment {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();

    private SpiceManager spiceManager;
    private boolean shown;
    private FragmentManager fragmentManager;
    private String tag;

    public Context getContext() {
        return context;
    }

    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ApplicationClass.getCurrentActivity();

        if (ApplicationClass.getCurrentActivity() != null
                && ApplicationClass.getCurrentActivity().getSpiceManager() == null) {
            spiceManager = new SpiceManager(
                    CacheableSpringAndroidSpiceService.class);
            spiceManager.start(getActivity());
        }

    }

    public SpiceManager getSpiceManager() {
        SpiceManager dbSpiceManager = ApplicationClass.getCurrentActivity().getSpiceManager();
        if (dbSpiceManager != null) {
            return dbSpiceManager;
        }

        return spiceManager;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) return;

        this.tag = tag;

        if (tag != null
                && tag.equals(FirstScreenActivity.class
                .getSimpleName())) {

            shown = true;
            super.show(manager, tag);

            return;
        }

        if (!BaseActivity.isInBackground) {

            this.fragmentManager = manager;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (getFragmentManager() != null
                    && tag != null) {
                Fragment prev = getFragmentManager().findFragmentByTag(tag);
                if (prev != null) {
                    ft.remove(prev);
                }
            }
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();

            shown = true;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (tag != null
                && tag.equals(FirstScreenActivity.class
                .getSimpleName())) {

            shown = false;
            super.dismiss();

            return;
        }

        if (!BaseActivity.isInBackground) {
            if (isAdded()
                    && fragmentManager != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.remove(this);
                ft.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }

            shown = false;
        }

    }

    @Override
    public void dismiss() {
        if (tag != null
                && tag.equals(FirstScreenActivity.class
                .getSimpleName())) {

            shown = false;
            super.dismiss();

            return;
        }

        if (isAdded()
                && fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(this);
            ft.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }

        shown = false;
    }

    protected FragmentManager getSupportFragmentManager() {
        return getChildFragmentManager();
    }
}
