package com.tripasfactory.thetripaslibrary.Controllers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.tripasfactory.thetripaslibrary.Interfaces.EndlessScrollViewListener;

/**
 * Created by rvieira on 09/12/2015.
 */
public class EndlessScrollView extends ScrollView {

    private EndlessScrollViewListener scrollViewListener = null;
    private boolean first = true;

    public EndlessScrollView(Context context) {
        super(context);
    }

    public EndlessScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EndlessScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(EndlessScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            onScrollChangedSuper(this, l, t, oldl, oldt);
        }
    }

    protected void onScrollChangedSuper(EndlessScrollView scrollView, int x, int y, int oldx, int oldy) {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            if (first) {
                first = false;
                scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
            }
        } else {
            first = true;
        }

    }
}