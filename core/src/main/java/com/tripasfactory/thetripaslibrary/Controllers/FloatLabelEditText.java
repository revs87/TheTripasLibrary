package com.tripasfactory.thetripaslibrary.Controllers;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tripasfactory.thetripaslibrary.Base.ApplicationClass;
import com.tripasfactory.thetripaslibrary.Components.HelpPopup;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.Utils.FormatterClass;
import com.tripasfactory.thetripaslibrary.Utils.UnitConverterClass;

import java.math.RoundingMode;
import java.text.NumberFormat;

@TargetApi(11)
public class FloatLabelEditText extends LinearLayout {

    public static int INPUT_TYPE_TEXT = 0;
    public static int INPUT_TYPE_TEXT_PASSWORD = 1;
    public static int INPUT_TYPE_NUMBER = 2;
    public static int INPUT_TYPE_NUMBER_PASSWORD = 3;
    public static int INPUT_TYPE_AMOUNT = 4;
    public static int INPUT_TYPE_GROUPBY4 = 5;
    public static int INPUT_TYPE_DESCRIPTION = 7;
    public static int INPUT_TYPE_BLOCKED = 8;
    public static int INPUT_TYPE_GROUPBY4_NUMBERS = 9;
    public static int INPUT_TYPE_GROUPBY4_IBAN = 10;
    public static int INPUT_TYPE_PHONE_NUMBER = 11;

    // TODO implement normal and vertical types
    public static int LAYOUT_TYPE_NORMAL = 0;
    public static int LAYOUT_TYPE_FLOATING = 1;
    public static int LAYOUT_TYPE_HORIZONTAL = 2;
    public static int LAYOUT_TYPE_VERTICAL = 3;
    public static int LAYOUT_TYPE_HORIZONTAL_DESCRIPTION = 4;
    public static int LAYOUT_TYPE_FLOATING_DESCRIPTION = 5;

    private int mCurrentApiVersion = android.os.Build.VERSION.SDK_INT, mFocusedColor, mUnFocusedColor, mFitScreenWidth, mGravity;
    private float mTextSizeInSp;
    private String mHintText, mEditText, mTooltipText;
    private int mInputType = 0;
    private int mLayoutType = LAYOUT_TYPE_FLOATING;
    private boolean mHasClear = false;
    private boolean mHasShowPassword = false;

    private AttributeSet mAttrs;
    private Context mContext;
    private EditText mEditTextView;
    private TextView mFloatingLabel;
    private TextView mFloatingLabelError;
    private View mFloatingLabelLl;

    private View mTooltip;
    private View mClear;
    private View mShowPassword;

    public static final int MAX_AMOUNT_DIGITS = 13;
    public static final int MAX_AMOUNT_DECIMAL_PLACES = 2;

    private NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private String temp = "";
    private int moveCaretTo;
    private int maxLength = MAX_AMOUNT_DIGITS + MAX_AMOUNT_DECIMAL_PLACES + 1;

    private boolean hasError = false;

    private ValueAnimator mFocusToUnfocusAnimation, mUnfocusToFocusAnimation;

    // -----------------------------------------------------------------------

    private String mCurrencyFromAccount;
    private View mFloatingLabelsRl;
    public static final int DEFAULT_MAX_LENGTH = 100;
    private int mLimit = DEFAULT_MAX_LENGTH;


    // -----------------------------------------------------------------------
    // default constructors

    public FloatLabelEditText(Context context) {
        super(context);
        mContext = context;
        initializeView();
    }

    public FloatLabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        initializeView();
    }

    public FloatLabelEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        initializeView();
    }

    // -----------------------------------------------------------------------
    // public interface

    public EditText getEditText() {
        return mEditTextView;
    }

    public String getText() {

        if (mInputType == INPUT_TYPE_AMOUNT && getEditTextString() != null &&
                getEditTextString().toString() != null &&
                getEditTextString().toString().length() > 0) {
            String amount = mEditTextView.getText().toString();
            if (amount == null) {
                amount = "";
            }
            if (amount != null && amount.contains(",")) {
                amount = amount.replace(',', '.');
            }

            return amount;
        } else if (mInputType == INPUT_TYPE_GROUPBY4 && getEditTextString() != null &&
                getEditTextString().toString() != null &&
                getEditTextString().toString().length() > 0) {
            String value = removeSpaces(getEditTextString().toString());

            return removeSpaces(value);
        } else if (mInputType == INPUT_TYPE_GROUPBY4_NUMBERS && getEditTextString() != null &&
                getEditTextString().toString() != null &&
                getEditTextString().toString().length() > 0) {
            String value = removeSpaces(getEditTextString().toString());

            return removeSpaces(value);
        } else if (mInputType == INPUT_TYPE_GROUPBY4_IBAN && getEditTextString() != null &&
                getEditTextString().toString() != null &&
                getEditTextString().toString().length() > 0) {
            String value = removeSpaces(getEditTextString().toString());

            return removeSpaces(value);
        } else if (mInputType == INPUT_TYPE_PHONE_NUMBER && getEditTextString() != null &&
                getEditTextString().toString() != null &&
                getEditTextString().toString().length() > 0) {

            String value = removeStrokes(getEditTextString().toString());

            return removeStrokes(value);
        } else if (getEditTextString() != null &&
                getEditTextString().toString() != null &&
                getEditTextString().toString().length() > 0) {

            return getEditTextString().toString();
        }

        return "";
    }

    public void setHint(String hintText) {
        mHintText = hintText.toUpperCase();
        mFloatingLabel.setText(hintText.toUpperCase());
        setupEditTextView();
    }

    public void setText(String text) {
        mEditTextView.setText(text);
    }

    public void setError(String errorText) {
        if (errorText != null) {
            mFloatingLabelError.setText(errorText);
            mFloatingLabelError.setVisibility(View.VISIBLE);
        }
        mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_error_shape);
        hasError = true;
    }

    public void clearError() {
        if (hasError) {
            mFloatingLabelError.setText("");
            mFloatingLabelError.setVisibility(View.GONE);
            mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_focus_shape);
            hasError = false;
        }
    }

    // -----------------------------------------------------------------------
    // private helpers

    private void initializeView() {

        if (mContext == null) {
            return;
        }

        getLayoutType();

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mLayoutType == LAYOUT_TYPE_FLOATING) {
            mInflater.inflate(R.layout.floatlabel_edittext_floating, this, true);
        } else if (mLayoutType == LAYOUT_TYPE_HORIZONTAL) {
            mInflater.inflate(R.layout.floatlabel_edittext_horizontal, this, true);
        } else if (mLayoutType == LAYOUT_TYPE_FLOATING_DESCRIPTION) {
            mInflater.inflate(R.layout.floatlabel_edittext_floating_description, this, true);
        } else if (mLayoutType == LAYOUT_TYPE_HORIZONTAL_DESCRIPTION) {
            mInflater.inflate(R.layout.floatlabel_edittext_horizontal_description, this, true);
        } else {
            mInflater.inflate(R.layout.floatlabel_edittext_floating, this, true);
        }


        mFloatingLabel = (TextView) findViewById(R.id.floating_label_hint);
        mFloatingLabelError = (TextView) findViewById(R.id.floating_label_error);
        mFloatingLabelLl = findViewById(R.id.floating_label_ll);

        /*           For click whole area */
        if (ApplicationClass.getDevice() == ApplicationClass.DEVICE_PHONE) {
            mFloatingLabelsRl = findViewById(R.id.floating_labels_rl);
        } else {
            mFloatingLabelsRl = findViewById(R.id.floating_label_edit_text);
        }

        mEditTextView = (EditText) findViewById(R.id.floating_label_edit_text);
        mTooltip = findViewById(R.id.floating_label_tooltip);
        mShowPassword = findViewById(R.id.floating_label_show_password);
        mClear = findViewById(R.id.floating_label_clear);

        mEditTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) /*||
                        (i == EditorInfo.IME_ACTION_DONE)*/) {
                    mEditTextView.clearFocus();
                }
                return false;
            }
        });

        getAttributesFromXmlAndStoreLocally();
        setupEditTextInputType();
        if (mLayoutType != LAYOUT_TYPE_HORIZONTAL
                && mLayoutType != LAYOUT_TYPE_HORIZONTAL_DESCRIPTION) {
            setupEditTextView();
            setupFloatingLabel();
        } else {
            setupHorizontalEditTextView();
        }

        setupTooltip();
        setupShowPassword();
        setupClear();
    }

    public void formatLayoutType(int layoutType, int inputType) {
        mLayoutType = layoutType;

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mLayoutType == LAYOUT_TYPE_FLOATING) {
            mInflater.inflate(R.layout.floatlabel_edittext_floating, this, true);
        } else if (mLayoutType == LAYOUT_TYPE_HORIZONTAL) {
            mInflater.inflate(R.layout.floatlabel_edittext_horizontal, this, true);
        } else if (mLayoutType == LAYOUT_TYPE_FLOATING_DESCRIPTION) {
            mInflater.inflate(R.layout.floatlabel_edittext_floating_description, this, true);
        } else if (mLayoutType == LAYOUT_TYPE_HORIZONTAL_DESCRIPTION) {
            mInflater.inflate(R.layout.floatlabel_edittext_horizontal_description, this, true);
        } else {
            mInflater.inflate(R.layout.floatlabel_edittext_floating, this, true);
        }


        mFloatingLabel = (TextView) findViewById(R.id.floating_label_hint);
        mFloatingLabelError = (TextView) findViewById(R.id.floating_label_error);
        mFloatingLabelLl = findViewById(R.id.floating_label_ll);

        /*           For click whole area */
        if (ApplicationClass.getDevice() == ApplicationClass.DEVICE_PHONE) {
            mFloatingLabelsRl = findViewById(R.id.floating_labels_rl);
        } else {
            mFloatingLabelsRl = findViewById(R.id.floating_label_edit_text);
        }

        mEditTextView = (EditText) findViewById(R.id.floating_label_edit_text);
        mTooltip = findViewById(R.id.floating_label_tooltip);
        mShowPassword = findViewById(R.id.floating_label_show_password);
        mClear = findViewById(R.id.floating_label_clear);

        mEditTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) /*||
                        (i == EditorInfo.IME_ACTION_DONE)*/) {
                    mEditTextView.clearFocus();
                }
                return false;
            }
        });

        getAttributesFromXmlAndStoreLocally();
        mInputType = inputType;
        setupEditTextInputType();
        if (mLayoutType != LAYOUT_TYPE_HORIZONTAL
                && mLayoutType != LAYOUT_TYPE_HORIZONTAL_DESCRIPTION) {
            setupEditTextView();
            setupFloatingLabel();
        } else {
            setupHorizontalEditTextView();
        }

        setupTooltip();
        setupShowPassword();
        setupClear();
    }

    private void getLayoutType() {
        TypedArray attributesFromXmlLayout = mContext.obtainStyledAttributes(mAttrs,
                R.styleable.FloatLabelEditText);
        if (attributesFromXmlLayout == null) {
            return;
        }

        mLayoutType = (attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_layoutType,
                0));

    }

    private void getAttributesFromXmlAndStoreLocally() {
        TypedArray attributesFromXmlLayout = mContext.obtainStyledAttributes(mAttrs,
                R.styleable.FloatLabelEditText);
        if (attributesFromXmlLayout == null) {
            return;
        }

        mTooltipText = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_tooltip);
        mHasShowPassword = attributesFromXmlLayout.getBoolean(R.styleable.FloatLabelEditText_hasShowPassword, false);
        mHasClear = attributesFromXmlLayout.getBoolean(R.styleable.FloatLabelEditText_hasClear, false);
        mHintText = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_hint).toUpperCase();
        mEditText = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_text);
        mGravity = attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_gravity,
                Gravity.LEFT);
        mTextSizeInSp = getScaledFontSize(attributesFromXmlLayout.getDimensionPixelSize(R.styleable.FloatLabelEditText_textSize,
                (int) mEditTextView
                        .getTextSize()
        ));
        mFocusedColor = attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintFocused,
                mContext.getResources().getColor(R.color.text));
        mUnFocusedColor = attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintUnFocused,
                mContext.getResources().getColor(R.color.text));
        mFitScreenWidth = attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_fitScreenWidth,
                0);
        mInputType = (attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_inputType,
                0));
        mLimit = (attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_limit,
                DEFAULT_MAX_LENGTH));
        attributesFromXmlLayout.recycle();
    }

    private void setupEditTextInputType() {
        setLimit(mLimit); /* default max chars */
        if (mInputType == INPUT_TYPE_TEXT) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getTextWatcher());
        } else if (mInputType == INPUT_TYPE_BLOCKED) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getTextWatcher());
            mEditTextView.setFocusable(false);
            mEditTextView.setFocusableInTouchMode(false);
        } else if (mInputType == INPUT_TYPE_TEXT_PASSWORD) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getTextWatcher());
        } else if (mInputType == INPUT_TYPE_NUMBER) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getTextWatcher());
        } else if (mInputType == INPUT_TYPE_NUMBER_PASSWORD) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getTextWatcher());
        } else if (mInputType == INPUT_TYPE_AMOUNT) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            numberFormat.setMaximumIntegerDigits(MAX_AMOUNT_DIGITS);
            numberFormat.setMaximumFractionDigits(MAX_AMOUNT_DECIMAL_PLACES);
            numberFormat.setRoundingMode(RoundingMode.DOWN);
            numberFormat.setGroupingUsed(false);
            mEditTextView.addTextChangedListener(getAmountWatcher());
        } else if (mInputType == INPUT_TYPE_GROUPBY4) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getGroupBy4Watcher());
        } else if (mInputType == INPUT_TYPE_DESCRIPTION) {
            mEditTextView.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE); /* setRawInputType !!! */
            setLimitFormatted(32);
            mEditTextView.setTypeface(Typeface.DEFAULT);
//            mEditTextView.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
            mEditTextView.addTextChangedListener(getTextWatcher());
        } else if (mInputType == INPUT_TYPE_GROUPBY4_NUMBERS) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getGroupBy4Watcher());
        } else if (mInputType == INPUT_TYPE_GROUPBY4_IBAN) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
            setLimit(21);
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getGroupBy4NIBWatcher());
        } else if (mInputType == INPUT_TYPE_PHONE_NUMBER) {
            mEditTextView.setInputType(InputType.TYPE_CLASS_PHONE);
            setLimitFormattedForPhone();
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextView.addTextChangedListener(getPhoneNumberWatcher());
        }

    }

    public void setLimit(int nChars) {
        mEditTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nChars)});
    }

    public void setLimitFormatted() {
        final String blockCharacterSet = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        mEditTextView.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(DEFAULT_MAX_LENGTH)});
    }

    public void setLimitFormattedForPhone() {
        final String blockCharacterSet = "!@#$%^&*()~`-=_[]{}|:\";',./<>?";
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        if (mEditTextView.getText().length() > 0 && mEditTextView.getText().charAt(0) == '+') {
            mEditTextView.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(13)});
        } else {
            mEditTextView.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(9)});
        }
    }

    public void setLimitFormatted(int nChars) {
        final String blockCharacterSet = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        mEditTextView.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(nChars)});
    }


    private void setupEditTextView() {

        mEditTextView.setHint(mHintText);
//        mEditTextView.setHintTextColor(mUnFocusedColor);
        mEditTextView.setTextColor(getResources().getColor(R.color.text));
        mEditTextView.setText(mEditText);
        mEditTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSizeInSp);

        if (mFitScreenWidth > 0) {
            mEditTextView.setWidth(getSpecialWidth());
        }

        if (mCurrentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            mEditTextView.setOnFocusChangeListener(getFocusChangeListener());
        }

        mFloatingLabelsRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingLabelsRl.requestFocus(mEditTextView.getId());
            }
        });

        if (mHasClear) {
            mClear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditTextView.setText("");
                }
            });
        }

        if (mHasShowPassword) {

            final float passwordTextTextSizeInSp = UnitConverterClass.pixelsToSp(mContext,
                    mEditTextView.getTextSize());
            final Typeface passwordTextTypeface = mEditTextView.getTypeface();

            mShowPassword.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        // Typeface and Textsize has to be rewritten because
                        // setInputType clears it
                        mEditTextView.setTypeface(passwordTextTypeface);
                        mEditTextView.setTextSize(passwordTextTextSizeInSp);
                        mEditTextView.setSelection(mEditTextView.getText()
                                .length());
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        // Typeface and Textsize has to be rewritten because
                        // setInputType clears it
                        mEditTextView.setTypeface(passwordTextTypeface);
                        mEditTextView.setTextSize(passwordTextTextSizeInSp);
                        mEditTextView.setSelection(mEditTextView.getText()
                                .length());
                        return true;
                    }
                    return false;


                }
            });
        }

        /* Copy & Paste disable */
//        mEditTextView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//                return false;
//            }
//
//            public void onDestroyActionMode(ActionMode mode) {
//            }
//
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//        });
//        mEditTextView.setLongClickable(false);
//        mEditTextView.setTextIsSelectable(false);
    }

    private void setupHorizontalEditTextView() {

        mEditTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSizeInSp);
        mFloatingLabel.setText(mHintText);

        if (mFitScreenWidth > 0) {
            mEditTextView.setWidth(getSpecialWidth());
        }

        if (mCurrentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            mEditTextView.setOnFocusChangeListener(getFocusChangeListener());
        }

        if (mHasClear) {
            mClear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditTextView.setText("");
                }
            });
        }

        mFloatingLabelsRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingLabelsRl.requestFocus(mEditTextView.getId());
            }
        });

        if (mHasShowPassword) {

            final float passwordTextTextSizeInSp = UnitConverterClass.pixelsToSp(mContext,
                    mEditTextView.getTextSize());
            final Typeface passwordTextTypeface = mEditTextView.getTypeface();

            mShowPassword.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        // Typeface and Textsize has to be rewritten because
                        // setInputType clears it
                        mEditTextView.setTypeface(passwordTextTypeface);
                        mEditTextView.setTextSize(passwordTextTextSizeInSp);
                        mEditTextView.setSelection(mEditTextView.getText()
                                .length());
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        // Typeface and Textsize has to be rewritten because
                        // setInputType clears it
                        mEditTextView.setTypeface(passwordTextTypeface);
                        mEditTextView.setTextSize(passwordTextTextSizeInSp);
                        mEditTextView.setSelection(mEditTextView.getText()
                                .length());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void setupFloatingLabel() {
        mFloatingLabel.setText(mHintText);
        mFloatingLabel.setTextColor(mUnFocusedColor);
        mFloatingLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (mTextSizeInSp / 1.3));
        mFloatingLabel.setGravity(mGravity);
        mFloatingLabel.setPadding(mEditTextView.getPaddingLeft(), 0, 0, 0);

        if (getText().length() > 0) {
            showFloatingLabel();
        }
    }

    private void setupTooltip() {
        if (mTooltipText != null) {
            mTooltip.setVisibility(View.VISIBLE);
            mTooltip.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    HelpPopup userHelpPopup = new HelpPopup(mContext, mTooltipText);
                    userHelpPopup.show(v);
                }
            });
        } else {
            mTooltip.setVisibility(View.GONE);
        }

    }

    private void setupShowPassword() {
        if (mHasShowPassword) {
            mShowPassword.setVisibility(View.VISIBLE);
        } else {
            mShowPassword.setVisibility(View.GONE);
        }
    }

    private void setupClear() {
        if (mHasClear) {
            mClear.setVisibility(View.VISIBLE);
        } else {
            mClear.setVisibility(View.GONE);
        }
    }


    private void showFloatingLabel() {
        if (mLayoutType != LAYOUT_TYPE_HORIZONTAL
                && mLayoutType != LAYOUT_TYPE_HORIZONTAL_DESCRIPTION) {
            mFloatingLabel.setVisibility(VISIBLE);
            mFloatingLabel.startAnimation(AnimationUtils.loadAnimation(getContext(),
                    R.anim.floatlabel_slide_from_bottom));
        }
    }

    private void hideFloatingLabel() {
        if (mLayoutType != LAYOUT_TYPE_HORIZONTAL
                && mLayoutType != LAYOUT_TYPE_HORIZONTAL_DESCRIPTION) {
            mFloatingLabel.setVisibility(GONE);
            mFloatingLabel.startAnimation(AnimationUtils.loadAnimation(getContext(),
                    R.anim.floatlabel_slide_to_bottom));
        }
    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeFocus(hasFocus);
            }

        };
    }

    public void changeFocus(boolean hasFocus) {
        ValueAnimator lColorAnimation = null;

        if (!hasError) {
            if (hasFocus) {
                clearError();
                lColorAnimation = getFocusToUnfocusAnimation();
                mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_focus_shape);
            } else {
                clearError();
                lColorAnimation = getUnfocusToFocusAnimation();
                mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_shape);
            }

            lColorAnimation.setDuration(700);
            lColorAnimation.start();
        }

    }

    public void setActive() {
        mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_shape);
        mEditTextView.setEnabled(true);
        mFloatingLabel.setEnabled(true);
    }

    public void setInactive() {
        mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_inactive_shape);
        mEditTextView.setEnabled(false);
        mFloatingLabel.setEnabled(false);
        mEditTextView.clearFocus();
    }

    private ValueAnimator getFocusToUnfocusAnimation() {
        if (mFocusToUnfocusAnimation == null) {
            mFocusToUnfocusAnimation = getFocusAnimation(mUnFocusedColor, mFocusedColor);
        }
        return mFocusToUnfocusAnimation;
    }

    private ValueAnimator getUnfocusToFocusAnimation() {
        if (mUnfocusToFocusAnimation == null) {
            mUnfocusToFocusAnimation = getFocusAnimation(mFocusedColor, mUnFocusedColor);
        }
        return mUnfocusToFocusAnimation;
    }

    private ValueAnimator getFocusAnimation(int fromColor, int toColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                fromColor,
                toColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mFloatingLabel.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        return colorAnimation;
    }

    private Editable getEditTextString() {
        return mEditTextView.getText();
    }

    private float getScaledFontSize(float fontSizeFromAttributes) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return fontSizeFromAttributes / scaledDensity;
    }

    private int getSpecialWidth() {
        float screenWidth = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getWidth();
        int prevWidth = mEditTextView.getWidth();

        switch (mFitScreenWidth) {
            case 2:
                return (int) Math.round(screenWidth * 0.5);
            default:
                return Math.round(screenWidth);
        }
    }

// -----------------------------------------------------------------------
    // text watchers

    private void floatLabelMainBehaviour(Editable s) {
        clearError();
        if (s.length() > 0 && mFloatingLabel.getVisibility() == GONE) {
            showFloatingLabel();
        } else if (s.length() == 0 && mFloatingLabel.getVisibility() == VISIBLE) {
            hideFloatingLabel();
        }
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                floatLabelMainBehaviour(s);
            }
        };
    }

    private TextWatcher getAmountWatcher() {

        return new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // remove to prevent StackOverFlowException
                boolean isComma = false;
                mEditTextView.removeTextChangedListener(this);
                String ss = s.toString();
                int len = ss.length();
                int dots = countOccurrences(ss, '.');
                int commas = countOccurrences(ss, ',');
                if (commas > 0 && dots > 0) {
                    ss = ss.substring(0, ss.length() - 1);
                    mEditTextView.setText(ss);
                }
                if (dots == 0) {
                    dots = countOccurrences(ss, ',');
                    if (dots > 0) {
                        isComma = true;
                    }
                }
                boolean shouldParse = dots <= 1
                        && (dots == 0 ? len != (MAX_AMOUNT_DIGITS + 1) : len < (maxLength + 1));
                boolean x = false;
                if (dots == 1) {
                    int indexOf = ss.indexOf('.');
                    if (isComma) {
                        indexOf = ss.indexOf(',');
                    }
                    try {
                        if (ss.charAt(indexOf + 1) == '0') {
                            shouldParse = false;
                            x = true;
                            if (ss.substring(indexOf).length() > 2) {
                                shouldParse = true;
                                x = false;
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
                if (shouldParse) {
                    if (!isComma) {
                        if (len > 1 && ss.lastIndexOf(".") != len - 1) {
                            try {
                                Double d = Double.parseDouble(ss);
                                if (d != null) {
                                    mEditTextView.setText(numberFormat.format(d));
                                }
                            } catch (NumberFormatException e) {
                            }
                        }
                    } else {
                        if (len > 1 && ss.lastIndexOf(",") != len - 1) {
                            try {
                                ss = ss.replace(',', '.');
                                Double d = Double.parseDouble(ss);
                                if (d != null) {
                                    mEditTextView.setText(numberFormat.format(d));
                                }
                            } catch (NumberFormatException e) {
                            }
                        }
                    }

                } else {
                    if (x) {
                        mEditTextView.setText(ss);
                    } else {
                        mEditTextView.setText(temp);
                    }
                }
                mEditTextView.addTextChangedListener(this); // reset listener

                // tried to fix caret positioning after key type:
                if (mEditTextView.getText().toString().length() > 0) {
                    if (dots == 0 && len >= MAX_AMOUNT_DIGITS && moveCaretTo > MAX_AMOUNT_DIGITS) {
                        moveCaretTo = MAX_AMOUNT_DIGITS;
                    } else if (dots > 0 && len >= (maxLength) && moveCaretTo > (maxLength)) {
                        moveCaretTo = maxLength;
                    }
                    try {
                        mEditTextView.setSelection(mEditTextView.getText().toString().length());
                        // et.setSelection(moveCaretTo); <- almost had it :))
                    } catch (Exception e) {
                    }
                }

                floatLabelMainBehaviour(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                moveCaretTo = mEditTextView.getSelectionEnd();
                temp = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = mEditTextView.getText().toString().length();
                if (length > 0) {
                    moveCaretTo = start + count - before;
                }
            }

        };
    }

    private TextWatcher getGroupBy4Watcher() {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String updatedText = removeSpaces(mEditTextView.getText().toString());
                updatedText = updatedText.toUpperCase();
                mEditTextView.removeTextChangedListener(this);
                mEditTextView.setText(FormatterClass
                        .formatStringToGroupBy4(updatedText));
                mEditTextView.addTextChangedListener(this);
                mEditTextView.setSelection(mEditTextView.length());

                floatLabelMainBehaviour(s);
            }
        };
    }

    private TextWatcher getPhoneNumberWatcher() {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String updatedText = removeSpaces(mEditTextView.getText().toString());
                updatedText = updatedText.toUpperCase();
                mEditTextView.removeTextChangedListener(this);
                mEditTextView.addTextChangedListener(this);
                mEditTextView.setSelection(mEditTextView.length());
                setLimitFormattedForPhone();

                floatLabelMainBehaviour(s);
            }
        };
    }


    private TextWatcher getGroupBy4NIBWatcher() {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String updatedText = removeSpaces(mEditTextView.getText().toString());
                updatedText = updatedText.toUpperCase();
                mEditTextView.removeTextChangedListener(this);
                mEditTextView.setText(FormatterClass.formatStringToNIB(updatedText));
                mEditTextView.addTextChangedListener(this);
                mEditTextView.setSelection(mEditTextView.length());

                floatLabelMainBehaviour(s);
            }
        };
    }

    private TextWatcher getBankAccountWatcher() {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String updatedText = removeStrokes(mEditTextView.getText().toString());
                updatedText = updatedText.toUpperCase();
                mEditTextView.removeTextChangedListener(this);
                mEditTextView.setText(FormatterClass
                        .formatStringToBankAccount(updatedText));
                mEditTextView.addTextChangedListener(this);
                mEditTextView.setSelection(mEditTextView.length());

                floatLabelMainBehaviour(s);

            }
        };
    }


    private String removeSpaces(String str) {
        return str.replace(" ", "");
    }

    private String removeStrokes(String str) {
        return str.replace("-", "");
    }

    private int countOccurrences(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public void setDisabled() {
        mEditTextView.setEnabled(false);
        mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_inactive_shape);
        mEditTextView.setFocusable(false);
        mEditTextView.setFocusableInTouchMode(false);
    }

    public void setEnabled() {
        mEditTextView.setEnabled(true);
        mFloatingLabelLl.setBackgroundResource(R.drawable.edittext_background_shape);
        mEditTextView.setFocusable(true);
        mEditTextView.setFocusableInTouchMode(true);
    }

    /**
     * Request focus to nextFloatEditText on keyboard NXT button click.
     *
     * @param nextFloatEditText
     */
    public void setNextFocus(final FloatLabelEditText nextFloatEditText) {
        final int action = EditorInfo.IME_ACTION_NEXT;
        this.getEditText().setImeOptions(action);
        this.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == action) {
                    nextFloatEditText.getEditText().requestFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
