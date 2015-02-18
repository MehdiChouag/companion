package com.anyfetch.companion.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mehdichouag on 18/02/15.
 */
public class HomeViewPager extends ViewPager {

    private boolean mSwipe = true;

    public HomeViewPager(Context context) {
        super(context);
    }

    public HomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mSwipe && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mSwipe && super.onInterceptTouchEvent(ev);
    }

    public void setSwipe(boolean value) {
        mSwipe = value;
    }
}
