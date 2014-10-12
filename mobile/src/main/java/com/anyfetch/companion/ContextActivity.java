package com.anyfetch.companion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import com.anyfetch.companion.commons.android.Event;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;
import com.anyfetch.companion.fragments.ContextFragment;

/**
 * Launches ContextFragment
 */
public class ContextActivity extends Activity {
    private String mTitle;
    private Event mEvent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        Intent originIntent = getIntent();

        String type = originIntent.getStringExtra(ContextFragment.ARG_TYPE);
        Parcelable parcelable = originIntent.getParcelableExtra(ContextFragment.ARG_PARCELABLE);
        if (type.equals(ContextFragment.TYPE_EVENT)) {
            Event event = (Event) parcelable;
            mTitle = event.getTitle();
            mEvent = event;
        } else {
            mTitle = "";
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ContextFragment.newInstance(type, parcelable))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context, menu);
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayShowHomeEnabled(false);
            bar.setTitle(mTitle);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_prepare_on_wear:
                if (mEvent != null) {
                    Intent i = new Intent();
                    i.setAction("com.anyfetch.companion.SHOW_NOTIFICATION");
                    i.putExtra(MeetingPreparationAlarm.ARG_EVENT, mEvent);
                    this.sendBroadcast(i);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
