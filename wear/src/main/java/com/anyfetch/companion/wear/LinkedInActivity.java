package com.anyfetch.companion.wear;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;

import com.anyfetch.companion.commons.models.LinkedInProfile;
import com.anyfetch.companion.commons.models.demo.DemoLinkedBenioff;

public class LinkedInActivity extends Activity {

    private GridViewPager mViewPager;
    private GridPagerAdapter mViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_in);
        mViewPager = (GridViewPager) findViewById(R.id.linked_in_view_pager);
        // TODO: demo choice, use real data next
        LinkedInProfile profile = new DemoLinkedBenioff(this);
        mViewPagerAdapter = new LinkedInViewPagerAdapter(this, getFragmentManager(), profile);
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}
