package com.tripasfactory.thetripaslibrary.Base;

import com.tripasfactory.thetripaslibrary.Base.BaseActivity;

/**
 * Created by Rui Vieira on 03/03/2016.
 * <p/>
 * Must be used whenever a new private activity is created during workflow.
 * This class instance type grants RestoreForeground Protection in the new private activity.
 * Configs.RESTORE_FOREGROUND_PROTECTION must be enabled.
 */
public class ProtectedBaseActivity extends BaseActivity {
}
