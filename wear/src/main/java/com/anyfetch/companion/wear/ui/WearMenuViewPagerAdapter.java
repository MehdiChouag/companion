package com.anyfetch.companion.wear.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.wearable.view.CircledImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyfetch.companion.wear.R;

class WearMenuViewPagerAdapter extends PagerAdapter {
    private static final int CIRCLE_COLOR = R.color.blue;
    private static final float CIRCLE_RADIUS = 128;
    private final WearMenuAction[] mWearMenuActions;

    public WearMenuViewPagerAdapter(WearMenuAction[] wearMenuActions) {
        mWearMenuActions = wearMenuActions;
    }

    @Override
    public int getCount() {
        return mWearMenuActions.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem (ViewGroup container, int position) {
        Context context = container.getContext();
        WearMenuAction menuAction = mWearMenuActions[position];
        View view = LayoutInflater.from(context).inflate(R.layout.item_wear_menu, container, false);
        TextView nameField = (TextView) view.findViewById(R.id.action_name);
        CircledImageView circleButton = (CircledImageView) view.findViewById(R.id.circle_button);
        nameField.setText(menuAction.getName());
        circleButton.setImageResource(menuAction.getIcon());
        circleButton.setOnClickListener(menuAction.getClickListener());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object view) {
        container.removeView((View)view);
    }
}
