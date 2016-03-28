package com.tripasfactory.thetripaslibrary.Components;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tripasfactory.thetripaslibrary.Base.BaseActivity;
import com.tripasfactory.thetripaslibrary.R;

import java.util.ArrayList;

public class EmptyAdapter extends PagerAdapter {

    private static final String TAG = EmptyAdapter.class.getSimpleName();

    private final BaseActivity activity;
    private final ArrayList<Object> responseProducts;
    private View emptyView;

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public EmptyAdapter(BaseActivity activity) {
        this.activity = activity;
        this.responseProducts = new ArrayList<>();
        this.responseProducts.add(new Object());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        emptyView = inflater.inflate(R.layout.fragment_empty, container, false);
        ImageView backgroundView = (ImageView) emptyView.findViewById(R.id.empty_view_background_iv);
        backgroundView.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        TextView messageEmptyView = (TextView) emptyView.findViewById(R.id.empty_view_message_tv);
        messageEmptyView.setText("No results.");

        container.addView(emptyView, 0);
        return emptyView;
    }
}