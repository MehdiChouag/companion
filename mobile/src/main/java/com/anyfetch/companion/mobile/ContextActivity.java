package com.anyfetch.companion.mobile;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import com.anyfetch.companion.android.Event;
import com.anyfetch.companion.mobile.fragments.ContextFragment;

/**
 * Launches ContextFragment
 */
public class ContextActivity extends Activity {
    private String mTitle;

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
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
