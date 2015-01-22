package com.anyfetch.companion;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.anyfetch.companion.adapters.EventsListAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.requests.GetUpcomingEventsRequest;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.fragments.ContextFragment;
import com.anyfetch.companion.meetings.ScheduleMeetingPreparationTask;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class UpcomingEventsActivity extends ActionBarActivity implements RequestListener<EventsList>, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private final SpiceManager mSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private final SpiceManager mHttpSpiceManager = new SpiceManager(HttpSpiceService.class);
    private StickyListHeadersListView mListView;
    private EventsListAdapter mListAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mEmptyView;
    private MixpanelAPI mixpanel;

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
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mixpanel = MixPanel.getInstance(this);

        setContentView(R.layout.activity_upcoming_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.toolbar_elevation));
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(this);
        setSupportActionBar(toolbar);

        mListView = (StickyListHeadersListView) findViewById(R.id.listView);

        mEmptyView = (TextView) findViewById(android.R.id.empty);
        mEmptyView.setText(getString(R.string.loading));

        mListAdapter = new EventsListAdapter(getApplicationContext(), new EventsList());
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setAreHeadersSticky(true);
        mListView.setDividerHeight(0);
        mListView.setEmptyView(mEmptyView);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark);


        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getApplicationContext());
        mSpiceManager.execute(request, request.createCacheKey(), 15 * DurationInMillis.ONE_MINUTE, this);
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    public void onClick(View v) {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(getString(R.string.title_upcoming_meetings));
            bar.setDisplayShowHomeEnabled(true);
        }
        return true;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Toast.makeText(this, getString(R.string.calendar_error), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestSuccess(EventsList events) {
        MixPanel.getInstance(this).getPeople().set("EventsCount", events.size());
        mSwipeLayout.setRefreshing(false);
        if (events.size() > 0) {
            new ScheduleMeetingPreparationTask(this).execute(null, null, null);
        }
        mListAdapter = new EventsListAdapter(getApplicationContext(), events);
        mListView.setAdapter(mListAdapter);

        mEmptyView.setText(getString(R.string.no_upcoming_shared_events));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        Event event = mListAdapter.getEvent(position);
        View backgroundView = view.findViewById(R.id.main_container);
        View imageView = view.findViewById(R.id.imageView);
        View titleView = view.findViewById(R.id.titleView);
        View locationView = view.findViewById(R.id.locationView);

        Intent intent = new Intent(getApplicationContext(), ContextActivity.class);
        intent.putExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT, event);
        intent.putExtra(ContextActivity.ORIGIN, "upcomingEventList");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions animation = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(imageView, "imageView"),
                    Pair.create(backgroundView, "backgroundView"),
                    Pair.create(locationView, "locationView"),
                    Pair.create(titleView, "titleView")
            );
            startActivity(intent, animation.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(this);
        mSpiceManager.execute(request, null, 0, this);
    }

    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
