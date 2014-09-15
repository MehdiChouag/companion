package com.anyfetch.companion.wear.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.anyfetch.companion.wear.R;

public abstract class WearMenuActivity extends Activity {

    private WearMenuAction[] mWearMenuActions;
    private ViewPager mViewPager;
    private WearMenuViewPagerAdapter mViewPagerAdapter;

    protected abstract WearMenuAction[] getMenuActions();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWearMenuActions = getMenuActions();
        setContentView(R.layout.activity_wear_menu);
        mViewPager = (ViewPager) findViewById(R.id.menu_view_pager);
        mViewPagerAdapter = new WearMenuViewPagerAdapter(mWearMenuActions);
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    public int getSelectedIndex() {
        return mViewPager.getCurrentItem();
    }

}
