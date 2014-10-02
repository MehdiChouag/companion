package com.anyfetch.companion.mobile;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.anyfetch.companion.android.AndroidSpiceService;
import com.anyfetch.companion.android.Event;
import com.anyfetch.companion.android.EventsList;
import com.anyfetch.companion.android.GetUpcomingEventsRequest;
import com.anyfetch.companion.mobile.adapters.EventsListAdapter;
import com.anyfetch.companion.mobile.fragments.ContextFragment;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class UpcomingEventsActivity extends ListActivity implements RequestListener<EventsList> {
    protected SpiceManager spiceManager = new SpiceManager(AndroidSpiceService.class);
    private EventsListAdapter mListAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String apiToken = preferences.getString("apiToken", null);

        if(apiToken == null) {
            openAuthActivity();
        } else {
            setContentView(R.layout.activity_upcoming_events);

            mListAdapter = new EventsListAdapter(getApplicationContext(), new EventsList());
            setListAdapter(mListAdapter);

            GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getApplicationContext());
            spiceManager.execute(request, null, 0, this);
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
        switch(id) {
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
    public void onRequestFailure(SpiceException spiceException) {}

    @Override
    public void onRequestSuccess(EventsList events) {
        mListAdapter = new EventsListAdapter(getApplicationContext(), events);
        setListAdapter(mListAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Event event = mListAdapter.getElement(position);

        if (event != null) {
            Intent intent = new Intent(getApplicationContext(), ContextActivity.class);
            intent.putExtra(ContextFragment.ARG_TYPE, ContextFragment.TYPE_EVENT);
            intent.putExtra(ContextFragment.ARG_PARCELABLE, event);
            startActivity(intent);
        }
    }

    private void openAuthActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
