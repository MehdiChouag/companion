package com.anyfetch.companion;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.anyfetch.companion.adapters.EventsListAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.requests.GetUpcomingEventsRequest;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.api.requests.GetStartRequest;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;
import com.anyfetch.companion.fragments.ContextFragment;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class UpcomingEventsActivity extends ActionBarActivity implements RequestListener<EventsList>, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final SpiceManager mSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private final SpiceManager mHttpSpiceManager = new SpiceManager(HttpSpiceService.class);
    private StickyListHeadersListView mListView;
    private EventsListAdapter mListAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
        mHttpSpiceManager.start(this);
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        mHttpSpiceManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serverUrl = preferences.getString(BaseRequestBuilder.PREF_SERVER_URL, BaseRequestBuilder.DEFAULT_SERVER_URL);
        String apiToken = preferences.getString(BaseRequestBuilder.PREF_API_TOKEN, null);

        if (apiToken == null) {
            openAuthActivity();
        } else {
            GetStartRequest startRequest = new GetStartRequest(serverUrl, apiToken);
            mHttpSpiceManager.execute(startRequest, null, 0, new RequestListener<Object>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    Toast.makeText(UpcomingEventsActivity.this, String.format(getString(R.string.auth_issue), spiceException.getMessage()), Toast.LENGTH_LONG).show();
                    if (spiceException.getMessage().equals("403")) {
                        openAuthActivity();
                    }
                }

                @Override
                public void onRequestSuccess(Object o) {
                    Log.i("LambdaRequestListener", "Company Account Updated");
                }
            });

            setContentView(R.layout.activity_upcoming_events);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.toolbar_elevation));
            setSupportActionBar(toolbar);

            mListView = (StickyListHeadersListView) findViewById(R.id.listView);

            mListAdapter = new EventsListAdapter(getApplicationContext(), new EventsList());
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(this);
            mListView.setAreHeadersSticky(true);
            mListView.setDividerHeight(0);

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
        Toast.makeText(this, getString(R.string.calendar_error), Toast.LENGTH_LONG).show();
        finish();
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
        View imageView = view.findViewById(R.id.imageView);
        View titleView = view.findViewById(R.id.titleView);

        Intent intent = new Intent(getApplicationContext(), ContextActivity.class);
        intent.putExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT, event);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(imageView, "imageView"),
                    Pair.create(titleView, "titleView")).toBundle());
        } else {
            startActivity(intent);
        }
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
