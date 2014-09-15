package com.anyfetch.companion.wear.ui;


import android.view.View;

public class WearMenuAction {
    private String mName;
    private int mIcon;
    private View.OnClickListener mClickListener;

    public WearMenuAction(String name, int icon, View.OnClickListener clickListener) {
        this.mName = name;
        this.mIcon = icon;
        this.mClickListener = clickListener;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    public View.OnClickListener getClickListener() {
        return mClickListener;
    }
}
