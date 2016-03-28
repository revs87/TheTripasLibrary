package com.tripasfactory.thetripaslibrary.Controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.tripasfactory.thetripaslibrary.R;


/**
 * Created by Rui Vieira on 02/03/2015.
 */
public class CustomSwitchButton extends RelativeLayout implements CompoundButton.OnCheckedChangeListener {

    private int dimen;
    private float startDimen;
    private float endDimen;
    private Animator oaLeft;
    private Animator oaRight;
    private boolean crossfadeRunning;

    private AttributeSet mAttrs;
    private Context mContext;

    private boolean isChecked;
    private View toggleCircle;
    private View backgroundOff;
    private View backgroundOn;

    private Switch mSwitch;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private View rootView;
    private boolean pref;

    public CustomSwitchButton(Context context) {
        super(context);
        mContext = context;
        initializeView();
    }

    public CustomSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        initializeView();
    }

    public CustomSwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        initializeView();
    }

    // -----------------------------------------------------------------------
    // helpers

    private void initializeView() {
        if (mContext == null) {
            return;
        }

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInflater.inflate(R.layout.switch_custom_button, this, true);

        backgroundOff = findViewById(R.id.switch_custom_background_oval_off);
        backgroundOn = findViewById(R.id.switch_custom_background_oval_on);
        toggleCircle = findViewById(R.id.switch_custom_toggle_circle);

        setSwitch();

        getAttributesFromXmlAndStoreLocally();

        setAnimators();

        setRootViewClick();
    }

    private void setAnimators() {
        dimen = getResources().getDimensionPixelSize(R.dimen.settings_toggle_width_animation);
        startDimen = getResources().getDimensionPixelSize(R.dimen.settings_toggle_margin);
        endDimen = (dimen / 2) + getResources().getDimensionPixelSize(R.dimen.settings_toggle_margin_plus);
        oaLeft = ObjectAnimator.ofFloat(toggleCircle, "x", endDimen, startDimen).setDuration(250);
        oaRight = ObjectAnimator.ofFloat(toggleCircle, "x", startDimen, endDimen).setDuration(250);
    }

    private void setRootViewClick() {
        if (isChecked) {
            mSwitch.setChecked(true);
            if (!(oaLeft.isRunning() || oaRight.isRunning() || crossfadeRunning)) {
                oaRight.start();
                crossfadeViews(backgroundOff, backgroundOn, 400);
            }
        } else {
            mSwitch.setChecked(false);
            if (!(oaLeft.isRunning() || oaRight.isRunning() || crossfadeRunning)) {
                oaLeft.start();
                crossfadeViews(backgroundOn, backgroundOff, 110);
            }
        }

        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oaLeft.isRunning() || oaRight.isRunning() || crossfadeRunning) return;

                pref = isChecked();

                if (pref) {
                    oaLeft.start();
                    crossfadeViews(backgroundOn, backgroundOff, 110);
                } else {
                    oaRight.start();
                    crossfadeViews(backgroundOff, backgroundOn, 400);
                }

                mSwitch.setChecked(!pref);
            }
        });
    }

    private void setSwitch() {
        mSwitch = new Switch(mContext);
    }

    private void getAttributesFromXmlAndStoreLocally() {
        TypedArray attributesFromXmlLayout = mContext.obtainStyledAttributes(mAttrs,
                R.styleable.CustomSwitchButton);
        if (attributesFromXmlLayout == null) {
            return;
        }
        isChecked = attributesFromXmlLayout.getBoolean(R.styleable.CustomSwitchButton_isChecked, false);
        attributesFromXmlLayout.recycle();
    }

    public void setChecked(boolean bool) {
        if (bool == true) {
            oaRight.start();
            crossfadeViews(backgroundOff, backgroundOn, 400);
        } else {
            oaLeft.start();
            crossfadeViews(backgroundOn, backgroundOff, 110);
        }
        mSwitch.setChecked(bool);
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    private void crossfadeViews(final View begin, View end, int duration) {
        crossfadeRunning = true;

        end.setAlpha(0f);
        end.setVisibility(View.VISIBLE);
        end.animate().alpha(1f).setDuration(duration).setListener(null);
        begin.animate().alpha(0f).setDuration(duration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                begin.setVisibility(View.GONE);
                crossfadeRunning = false;
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setChecked(isChecked);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        mSwitch.setOnCheckedChangeListener(this.onCheckedChangeListener);
    }
}
