package com.anyfetch.companion.mobile;

import android.test.ActivityInstrumentationTestCase2;

public class UpcomingEventsActivityTest extends ActivityInstrumentationTestCase2<UpcomingEventsActivity> {
    private UpcomingEventsActivity mActivity;

    public UpcomingEventsActivityTest() {
        super(UpcomingEventsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        mActivity = getActivity();
    }
}
