package com.anyfetch.companion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.anyfetch.companion.adapters.EventsListAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.Event;
import com.anyfetch.companion.commons.android.EventsList;
import com.anyfetch.companion.commons.android.GetUpcomingEventsRequest;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;
import com.anyfetch.companion.fragments.ContextFragment;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class UpcomingEventsActivity extends Activity implements RequestListener<EventsList>, AdapterView.OnItemClickListener {
    protected SpiceManager mSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private StickyListHeadersListView mListView;
    private EventsListAdapter mListAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String apiToken = preferences.getString("apiToken", null);

        if (apiToken == null) {
            openAuthActivity();
        } else {
            setContentView(R.layout.activity_upcoming_events);
            mListView = (StickyListHeadersListView) findViewById(R.id.listView);

            mListAdapter = new EventsListAdapter(getApplicationContext(), new EventsList());
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(this);

            GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getApplicationContext());
            mSpiceManager.execute(request, null, 0, this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upcoming_events, menu);
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setTitle(getString(R.string.title_upcoming_meetings));
            bar.setDisplayShowHomeEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_log_out:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("apiToken", null);
                editor.apply();
                openAuthActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
    }

    @Override
    public void onRequestSuccess(EventsList events) {
        if (events.size() > 0) {
            MeetingPreparationAlarm.setForEvent(this, events.get(0));
        }
        mListAdapter = new EventsListAdapter(getApplicationContext(), events);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        Event event = mListAdapter.getEvent(position);

        Intent intent = new Intent(getApplicationContext(), ContextActivity.class);
        intent.putExtra(ContextFragment.ARG_TYPE, ContextFragment.TYPE_EVENT);
        intent.putExtra(ContextFragment.ARG_PARCELABLE, event);
        startActivity(intent);
    }

    private void openAuthActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
