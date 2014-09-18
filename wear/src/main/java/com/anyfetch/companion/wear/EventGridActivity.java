package com.anyfetch.companion.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.activity.InsetActivity;
import android.support.wearable.view.GridViewPager;

import com.anyfetch.companion.commons.models.Event;
import com.anyfetch.companion.commons.models.demo.DemoEvent;

public class EventGridActivity extends Activity {

    private GridViewPager mGridViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String eventId = this.getIntent().getStringExtra(PostEventNotificationReceiver.CONTENT_KEY);
        Event event;
        if(eventId.equals("demo")) {
            event = new DemoEvent(this);
        } else {
            // TODO: Handle real data
            event = null;
        }

        setContentView(R.layout.activity_event_grid);
        mGridViewPager = (GridViewPager) findViewById(R.id.event_view_pager);
        mGridViewPager.setAdapter(new EventGridViewPagerAdapter(this, this.getFragmentManager(), event));
    }

}
