package com.tripasfactory.thetripaslibrary.Components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripasfactory.thetripaslibrary.Base.BaseFragment;
import com.tripasfactory.thetripaslibrary.Interfaces.RestoreDialogFragmentUpdateInterface;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.Utils.L;


public class EmptyFragment extends BaseFragment {

    private static final String TAG = EmptyFragment.class.getSimpleName();

    private static boolean isLoadingView;
    private static boolean hasBlueDivider;

    public static View fragmentView;
    private RestoreDialogFragmentUpdateInterface updateInterface;

    public static EmptyFragment newInstance() {
        L.v(TAG, "newInstance");
        EmptyFragment myFragment = new EmptyFragment();

        return myFragment;
    }

    public static EmptyFragment newInstance(boolean useLoadingView) {
        L.v(TAG, "newInstance");
        EmptyFragment myFragment = new EmptyFragment();

        isLoadingView = useLoadingView;

        return myFragment;
    }

    public static EmptyFragment newInstance(boolean useLoadingView, boolean useBlueDivider) {
        L.v(TAG, "newInstance");
        EmptyFragment myFragment = new EmptyFragment();

        isLoadingView = useLoadingView;
        hasBlueDivider = useBlueDivider;

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.v(TAG, "onCreateView");
        fragmentView = inflater.inflate(R.layout.fragment_empty, container,
                false);

        return fragmentView;
    }

    public static View getFragmentView() {
        return fragmentView;
    }


    public void setUpdateInterface(RestoreDialogFragmentUpdateInterface updateInterface) {
        this.updateInterface = updateInterface;
    }
}