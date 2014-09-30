package com.anyfetch.companion.mobile;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anyfetch.companion.android.AndroidSpiceService;
import com.anyfetch.companion.android.EventsList;
import com.anyfetch.companion.android.GetUpcomingEventsRequest;
import com.anyfetch.companion.mobile.adapters.EventsListAdapter;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class UpcomingEventsActivity extends ListActivity implements RequestListener<EventsList> {
    protected SpiceManager spiceManager = new SpiceManager(AndroidSpiceService.class);

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
        setContentView(R.layout.activity_upcoming_events);

        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getApplicationContext());
        spiceManager.execute(request, null, 0, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upcoming_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {}

    @Override
    public void onRequestSuccess(EventsList events) {
        setListAdapter(new EventsListAdapter(getApplicationContext(), events));
    }
}
