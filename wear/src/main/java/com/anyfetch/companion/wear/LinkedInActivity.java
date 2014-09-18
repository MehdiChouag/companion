package com.anyfetch.companion.wear;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.activity.InsetActivity;

import com.anyfetch.companion.commons.models.LinkedInProfile;
import com.anyfetch.companion.commons.models.demo.DemoLinkedBenioff;

public class LinkedInActivity extends Activity {

    private ViewPager mViewPager;
    private PagerAdapter mViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_in);
        mViewPager = (ViewPager) findViewById(R.id.linked_in_view_pager);
        // TODO: demo choice, use real data next
        LinkedInProfile profile = new DemoLinkedBenioff(this);
        mViewPagerAdapter = new LinkedInViewPagerAdapter(this, getFragmentManager(), profile);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setBackground(new BitmapDrawable(getResources(), profile.getFace()));
    }
}
