package com.anyfetch.companion;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;
import com.anyfetch.companion.fragments.ContextFragment;
import com.anyfetch.companion.fragments.PersonChooserFragment;

/**
 * Launches ContextFragment
 */
public class ContextActivity extends Activity {
    private ContextualObject mContextualObject;
    private ContextFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        Intent originIntent = getIntent();

        mContextualObject = (ContextualObject) originIntent.getParcelableExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT);

        if (savedInstanceState == null) {
            mFragment = ContextFragment.newInstance((Parcelable) mContextualObject);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment)
                    .commit();
        }
    }
}
