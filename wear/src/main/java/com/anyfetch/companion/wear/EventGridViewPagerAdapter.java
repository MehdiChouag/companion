package com.anyfetch.companion.wear;

import android.support.wearable.view.GridPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rricard on 12/09/14.
 */
public class EventGridViewPagerAdapter extends GridPagerAdapter {
    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount(int i) {
        return 0;
    }

    @Override
    protected Object instantiateItem(ViewGroup viewGroup, int i, int i2) {
        return null;
    }

    @Override
    protected void destroyItem(ViewGroup viewGroup, int i, int i2, Object o) {

    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return false;
    }
}
