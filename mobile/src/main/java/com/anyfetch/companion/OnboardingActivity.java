package com.anyfetch.companion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.anyfetch.companion.fragments.OnboardingFragment;

/**
 * Created by neamar on 1/19/15.
 */
public class OnboardingActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    private static final int[] LAYOUTS = new int[]{
            R.layout.fragment_onboarding_1,
            R.layout.fragment_onboarding_2,
            R.layout.fragment_onboarding_3,
    };

    private ImageView[] dots;

    private ImageView dot0;
    private ImageView dot1;
    private ImageView dot2;


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        dot0 = (ImageView) findViewById(R.id.dot0);
        dot1 = (ImageView) findViewById(R.id.dot1);
        dot2 = (ImageView) findViewById(R.id.dot2);

        dots = new ImageView[] {dot0, dot1, dot2};

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                dot0.setImageResource(R.drawable.dot);
                dot1.setImageResource(R.drawable.dot);
                dot2.setImageResource(R.drawable.dot);
                dots[i].setImageResource(R.drawable.dot_on);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            OnboardingFragment onboardingFragment = new OnboardingFragment();

            Bundle fragmentData = new Bundle();
            fragmentData.putInt(OnboardingFragment.LAYOUT_RESOURCE, LAYOUTS[position]);
            onboardingFragment.setArguments(fragmentData);

            return onboardingFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}