package com.anyfetch.companion.mobile;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String apiToken = preferences.getString("apiToken", null);

        if(apiToken == null) {
            openAuthActivity();
        } else {
            setContentView(R.layout.activity_upcoming_events);

            GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getApplicationContext());
            spiceManager.execute(request, null, 0, this);
        }
    }

    private void openAuthActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        startActivity(intent);
        finish();
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
        switch(id) {
            case R.id.action_settings:
                break;
            case R.id.action_log_out:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("apiToken", null);
                editor.commit();
                openAuthActivity();
                break;
        }
        if (id == R.id.action_settings) {
            openAuthActivity();
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
