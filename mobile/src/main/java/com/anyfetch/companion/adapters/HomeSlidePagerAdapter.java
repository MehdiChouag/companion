package com.anyfetch.companion.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.anyfetch.companion.R;
import com.anyfetch.companion.fragments.ContactPickerFragment;
import com.anyfetch.companion.fragments.UpcomingEventsFragment;

/**
 * Created by mehdichouag on 16/02/15.
 */
public class HomeSlidePagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 2;

    // Tabs titles
    private String[] mTabsTitle;

    public HomeSlidePagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        mTabsTitle = ctx.getResources().getStringArray(R.array.slidingTabValues);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return ContactPickerFragment.newInstance();
            case 1:
                return UpcomingEventsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < mTabsTitle.length)
            return mTabsTitle[position];
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
