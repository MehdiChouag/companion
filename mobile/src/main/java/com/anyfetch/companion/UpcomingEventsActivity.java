package com.anyfetch.companion;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.anyfetch.companion.adapters.EventsListAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.android.requests.GetUpcomingEventsRequest;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.api.requests.GetStartRequest;
import com.anyfetch.companion.fragments.ContextFragment;
import com.anyfetch.companion.meetings.ScheduleMeetingPreparationTask;
import com.anyfetch.companion.stats.MixPanel;
import com.melnykov.fab.FloatingActionButton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.newrelic.agent.android.NewRelic;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class UpcomingEventsActivity extends ActionBarActivity implements RequestListener<EventsList>, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_CONTACTPICKER = 1;

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
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MixpanelAPI mixpanel = MixPanel.getInstance(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Start newrelic monitoring
        NewRelic.withApplicationToken("AA8f2983b4af8f945810684414d40a161c400b7569").start(this.getApplication());

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
                    if (spiceException.getMessage().equals("403") || spiceException.getMessage().equals("401")) {
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
            mListView.setEmptyView(findViewById(android.R.id.empty));

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.attachToListView(mListView.getWrappedList());
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CONTACTPICKER);
                }
            });
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
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_connect:
                String url = "https://manager.anyfetch.com/marketplace";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACTPICKER) {
            if (resultCode == RESULT_OK) {
                String personId = data.getData().getLastPathSegment();
                Log.i("PersonPicker", "User picked contact " + personId);
                Person person = Person.getPerson(this, Long.parseLong(personId));

                Intent intent = new Intent(getApplicationContext(), ContextActivity.class);
                intent.putExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT, person);
                startActivity(intent);
            }
        }
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
    protected void onDestroy() {
        MixPanel.getInstance(this).flush();
        super.onDestroy();
    }
}
