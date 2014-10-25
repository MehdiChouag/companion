package com.anyfetch.companion;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.anyfetch.companion.adapters.EventsListAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.requests.GetUpcomingEventsRequest;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;
import com.anyfetch.companion.fragments.ContextFragment;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class UpcomingEventsActivity extends ActionBarActivity implements RequestListener<EventsList>, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    protected SpiceManager mSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private StickyListHeadersListView mListView;
    private EventsListAdapter mListAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private Toolbar mToolbar;

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
        String apiToken = preferences.getString(BaseRequestBuilder.PREF_API_TOKEN, null);

        if (apiToken == null) {
            openAuthActivity();
        } else {
            setContentView(R.layout.activity_upcoming_events);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            ViewCompat.setElevation(mToolbar, 1);
            setSupportActionBar(mToolbar);

            mListView = (StickyListHeadersListView) findViewById(R.id.listView);

            mListAdapter = new EventsListAdapter(getApplicationContext(), new EventsList());
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(this);

            mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            mSwipeLayout.setOnRefreshListener(this);
            mSwipeLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark);

            GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getApplicationContext());
            mSpiceManager.execute(request, request.createCacheKey(), 15 * DurationInMillis.ONE_MINUTE, this);
            mSwipeLayout.setRefreshing(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upcoming_events, menu);
        ActionBar bar = getSupportActionBar();
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
        mSwipeLayout.setRefreshing(false);
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
        intent.putExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT, event);
        startActivity(intent);
    }

    private void openAuthActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {
        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(this);
        mSpiceManager.execute(request, null, 0, this);
    }
}
