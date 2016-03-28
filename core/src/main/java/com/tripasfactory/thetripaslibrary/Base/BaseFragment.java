package com.tripasfactory.thetripaslibrary.Base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Controllers.AutoResizeTextView;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.RoboSpice.CacheableSpringAndroidSpiceService;
import com.tripasfactory.thetripaslibrary.RoboSpice.SpiceManagerEncrypted;
import com.tripasfactory.thetripaslibrary.Utils.L;

public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    private SpiceManagerEncrypted spiceManager;

    public BaseActivity getBaseActivity() {
        return ApplicationClass.getCurrentActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getDashboardSpiceManager() == null) {
            spiceManager = new SpiceManagerEncrypted(
                    CacheableSpringAndroidSpiceService.class);
            spiceManager.start(getBaseActivity());
        }
    }

    /* TAPJACKING override */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (Configs.SHOULD_BLOCK_TAPJACKING) {
            if (view != null) {
                view.setFilterTouchesWhenObscured(true);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.v(TAG, "onActivityCreated");

        if (ApplicationClass.getDevice() == ApplicationClass.DEVICE_PHONE)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDestroy() {
        if (spiceManager != null) {
            try {
                spiceManager.shouldStop();
            } catch (Exception e) {
                L.w(TAG, "Couldn't stop spiceManager", e);
            }
        }
        try {
            super.onDestroy();
        } catch (Exception e) {
            L.e(TAG, "Error calling super on destroy");
        }

    }

    public SpiceManagerEncrypted getSpiceManager() {

        SpiceManagerEncrypted dbSpiceManager = getDashboardSpiceManager();
        if (dbSpiceManager != null) {
            return dbSpiceManager;
        }

        if (spiceManager == null) {
            spiceManager = new SpiceManagerEncrypted(
                    CacheableSpringAndroidSpiceService.class);
            spiceManager.start(getBaseActivity());
        }

        return spiceManager;
    }

    // If the fragment is in the dashboard context,
    // it return the DashboardFragment own spiceManager
    private SpiceManagerEncrypted getDashboardSpiceManager() {
        Fragment frag = null;
        try {
            frag = getBaseActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
        } catch (Exception e) {
            if (spiceManager == null) {
                spiceManager = new SpiceManagerEncrypted(
                        CacheableSpringAndroidSpiceService.class);
                spiceManager.start(getBaseActivity());
            }

            return spiceManager;
        }
//        if (frag != null && frag instanceof DashboardFragment) {
//            return ((DashboardFragment) frag).getSpiceManager();
//        }

        if (spiceManager == null) {
            spiceManager = new SpiceManagerEncrypted(
                    CacheableSpringAndroidSpiceService.class);
            spiceManager.start(getBaseActivity());
        }

        return spiceManager;
    }

    public ActionBar getSupportActionBar() {
        return ApplicationClass.getCurrentActivity().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getBaseActivity().setSupportActionBar(toolbar);
    }

    public void setActionBarTitle(String title) {
        if (ApplicationClass.getCurrentActivity() != null) {
            Activity activity = ApplicationClass.getCurrentActivity();
            AutoResizeTextView toolBarSubTitleTv = ((AutoResizeTextView) activity.findViewById(R.id.toolbar_subtitle_tv));
            toolBarSubTitleTv.setVisibility(View.GONE);
            AutoResizeTextView toolBarTitleTv = ((AutoResizeTextView) activity.findViewById(R.id.toolbar_title_tv));
            if (toolBarTitleTv != null) {
                toolBarTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                String titleCaps = title/*.toUpperCase()*/;
                toolBarTitleTv.setText(titleCaps);
            }
        }
    }

    public void setActionBarTitles(String title, String subTitle) {
        if (ApplicationClass.getCurrentActivity() != null) {
            Activity activity = ApplicationClass.getCurrentActivity();
            AutoResizeTextView toolBarTitleTv = ((AutoResizeTextView) activity.findViewById(R.id.toolbar_subtitle_tv));
            toolBarTitleTv.setVisibility(View.VISIBLE);
            if (toolBarTitleTv != null) {
                toolBarTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                toolBarTitleTv.setText(title);
            }
            AutoResizeTextView toolBarSubTitleTv = ((AutoResizeTextView) activity.findViewById(R.id.toolbar_title_tv));
            if (toolBarSubTitleTv != null) {
                toolBarSubTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                toolBarSubTitleTv.setText(subTitle);
            }
        }
    }

    /* FIXME - fixed! MUST use ChildFragmentManager! */
    public FragmentManager getSupportFragmentManager() {
        return getChildFragmentManager();
    }

//    public void setActionBarFavouriteImage(Favourite favourite, boolean isImageFavoriteActionBar) {
//        Activity activity = getActivity();
//        if (activity != null) {
//            activity.findViewById(R.id.toolbar_favourite_iv).setVisibility(View.VISIBLE);
//            Drawable favouriteImage = FavouriteOperationsHelper.getFavouriteOrOperationImage(
//                    activity, favourite, isImageFavoriteActionBar, false);
//            ((ImageView) activity.findViewById(R.id.toolbar_favourite_iv)).setImageDrawable(favouriteImage);
//        }
//    }
//
//    public void hideActionBarFavouriteImage() {
//        Activity activity = getActivity();
//        if (activity != null) {
//            ((ImageView) activity.findViewById(R.id.toolbar_favourite_iv)).setImageDrawable(null);
//        }
//    }
}
