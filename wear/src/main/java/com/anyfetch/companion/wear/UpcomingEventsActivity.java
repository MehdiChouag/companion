package com.anyfetch.companion.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.models.Event;
import com.anyfetch.companion.commons.models.demo.DemoEvent;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventsActivity extends Activity {

    private WearableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        mListView = (WearableListView) findViewById(R.id.upcoming_events_list);

        // TODO: for demo purposes only
        List<Event> fakeList = new ArrayList<Event>();
        fakeList.add(new DemoEvent());
        fakeList.add(new DemoEvent());
        fakeList.add(new DemoEvent());
        fakeList.add(new DemoEvent());
        fakeList.add(new DemoEvent());

        mListView.setAdapter(new UpcomingEventsAdapter(this, fakeList));
    }
}
