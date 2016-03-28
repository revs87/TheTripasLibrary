
package com.tripasfactory.thetripaslibrary.Components;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tripasfactory.thetripaslibrary.Base.BaseDialogFragment;
import com.tripasfactory.thetripaslibrary.R;
import com.tripasfactory.thetripaslibrary.Utils.L;

import java.util.ArrayList;

public class CustomDialogFragment extends BaseDialogFragment {

    private static final String TAG = CustomDialogFragment.class
            .getSimpleName();

    private static final String TITLE_TAG = "title";
    private static final String MESSAGE_TAG = "message";
    private static final String GO_BACK_ON_CLOSE_DIALOG = "goBackOnCloseDialog";
    private static final String REQUEST_CODE_FOR_RESULT = "requestCodeForResult";
    OnClickListener mListener;

    public static CustomDialogFragment newInstance(String title,
                                                   String message, boolean goBackOnCloseDialog) {
        L.v(TAG, "newInstance");
        CustomDialogFragment myFragment = new CustomDialogFragment();

        Bundle args = new Bundle();
        args.putString(TITLE_TAG, title);
        args.putString(MESSAGE_TAG, message);
        args.putBoolean(GO_BACK_ON_CLOSE_DIALOG, goBackOnCloseDialog);
        myFragment.setArguments(args);

        return myFragment;
    }

    public static CustomDialogFragment newInstance(String title,
                                                   String message,
                                                   boolean goBackOnCloseDialog,
                                                   int requestCodeForResult
    ) {
        L.v(TAG, "newInstance");
        CustomDialogFragment myFragment = new CustomDialogFragment();

        Bundle args = new Bundle();
        args.putString(TITLE_TAG, title);
        args.putString(MESSAGE_TAG, message);
        args.putBoolean(GO_BACK_ON_CLOSE_DIALOG, goBackOnCloseDialog);
        args.putInt(REQUEST_CODE_FOR_RESULT, requestCodeForResult);
        myFragment.setArguments(args);

        return myFragment;
    }

    public static CustomDialogFragment newInstance(String title,
                                                   ArrayList<String> messages) {

        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            strBuilder.append(messages.get(i));
            if (i < messages.size() - 1)
                strBuilder.append("\n");
        }

        return newInstance(title, strBuilder.toString(), false);
    }

    public CustomDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if (android.os.Build.VERSION.SDK_INT >=
        // android.os.Build.VERSION_CODES.HONEYCOMB) {
        // setStyle(SherlockDialogFragment.STYLE_NO_FRAME,
        // R.style.MyDialogFragment);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyDialogFragment);

        // this setStyle is VERY important.
        // STYLE_NO_FRAME means that I will provide my own layout and style for
        // the whole dialog
        // so for example the size of the default dialog will not get in my way
        // the style extends the default one. see bellow.
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (this.getDialog() == null)
            return;

        int dialogWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
                        .getDisplayMetrics());

        int dialogHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 180, getResources()
                        .getDisplayMetrics());

        this.getDialog().getWindow()
                .setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String title = getArguments().getString(TITLE_TAG);
        String message = getArguments().getString(MESSAGE_TAG);
        Boolean goBackOnCloseDialog = getArguments().getBoolean(
                GO_BACK_ON_CLOSE_DIALOG, false);
        final int requestCodeForResult = getArguments().getInt(REQUEST_CODE_FOR_RESULT, 0);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater
                .inflate(R.layout.custom_dialog_fragment, container);

        TextView headerTitle = (TextView) view.findViewById(R.id.header_title);
        headerTitle.setText(title);

        TextView bodyMessage = (TextView) view.findViewById(R.id.body_message);
        bodyMessage.setText(message);

        Button confirmButton = (Button) view.findViewById(R.id.confirm_button);

        if (goBackOnCloseDialog) {
            confirmButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestCodeForResult != 0) {
                        if (getActivity() != null) {
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finishActivity(requestCodeForResult);
                            getActivity().onBackPressed();
                        }
                        dismiss();
                    } else {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                        dismiss();
                    }
                }
            });
        } else {

            if (mListener == null) {
                confirmButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            } else {
                confirmButton.setOnClickListener(mListener);
            }
        }

        return view;
    }

    public void setConfirmButtonListener(OnClickListener listener) {
        mListener = listener;
    }
}
