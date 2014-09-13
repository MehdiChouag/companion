package com.anyfetch.companion.wear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import com.anyfetch.companion.commons.models.Event;
import com.anyfetch.companion.commons.models.demo.DemoEvent;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventsActivity extends Activity implements WearableListView.ClickListener {

    private WearableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        mListView = (WearableListView) findViewById(R.id.upcoming_events_list);

        // TODO: for demo purposes only
        List<Event> fakeList = new ArrayList<Event>();
        fakeList.add(new DemoEvent(this));

        mListView.setAdapter(new UpcomingEventsAdapter(this, fakeList));
        mListView.setClickListener(this);
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        // TODO: for demo purposes only
        Intent prepareIntent = new Intent(this, EventGridActivity.class);
        prepareIntent.putExtra(PostEventNotificationReceiver.CONTENT_KEY, "demo");
        startActivity(prepareIntent);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
