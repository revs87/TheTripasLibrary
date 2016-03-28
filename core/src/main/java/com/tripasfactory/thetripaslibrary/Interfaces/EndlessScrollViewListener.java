/*
 * Copyright (c) 2011 WIT Software. All rights reserved.
 * 
 * WIT Software Confidential and Proprietary information. It is strictly forbidden for 3rd parties to modify, decompile,
 * disassemble, defeat, disable or circumvent any protection mechanism; to sell, license, lease, rent, redistribute or
 * make accessible to any third party, whether for profit or without charge.
 * 
 * pandre 2011/09/08
 */
package com.tripasfactory.thetripaslibrary.Interfaces;

import com.tripasfactory.thetripaslibrary.Controllers.EndlessScrollView;

/**
 * Created by rvieira on 09/12/2015.
 */

public interface EndlessScrollViewListener {
    void onScrollChanged(EndlessScrollView scrollView, int x, int y, int oldx, int oldy);
}


