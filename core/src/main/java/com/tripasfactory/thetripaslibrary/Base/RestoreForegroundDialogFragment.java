package com.tripasfactory.thetripaslibrary.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripasfactory.thetripaslibrary.Components.EmptyFragment;
import com.tripasfactory.thetripaslibrary.Interfaces.RestoreDialogFragmentUpdateInterface;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.Utils.L;


public class RestoreForegroundDialogFragment extends BaseDialogFragment {

    private static final String TAG = RestoreForegroundDialogFragment.class.getSimpleName();

    private FragmentManager fragmentManager;

    private View contentView;

    public static RestoreForegroundDialogFragment newInstance() {
        L.v(TAG, "newInstance");
        RestoreForegroundDialogFragment myFragment = new RestoreForegroundDialogFragment();

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.v(TAG, "onCreate");

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublicDialogFragment);

        // this setStyle is VERY important.
        // STYLE_NO_FRAME means that I will provide my own layout and style for
        // the whole dialog
        // so for example the size of the default dialog will not get in my way
        // the style extends the default one. see bellow.
    }

    @Override
    public void onStart() {
        super.onStart();
        /* Safety check */
        if (this.getDialog() == null)
            return;

        /* Fullscreen layout */
        this.getDialog().getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.v(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_dialog_restore_foreground, null);
        contentView = rootView.findViewById(R.id.content_frame);

        /*
        * We need to get the childFragmentManager from the DialogFragment, then from the child fragments
        * we can change the fragments via getParentFragment().getChildFragmentManager()
        * */
        fragmentManager = getChildFragmentManager();
        Fragment fragment = EmptyFragment.newInstance();
        ((EmptyFragment) fragment).setUpdateInterface(new RestoreDialogFragmentUpdateInterface() {

            @Override
            public void accept() {
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }

            @Override
            public void deny() {
                if (getDialog() != null) {
                    getDialog().dismiss();
                }

            }
        });
        fragmentManager
                .beginTransaction()
                .replace(contentView.getId(), fragment)
                .commit();

        return rootView;
    }

    /*
    * Dismiss present dialog upon minimized.
    * It will be inflated again when restored.
    * */
    @Override
    public void onPause() {
        super.onPause();
        L.v(TAG, "onPause");

        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }
}
