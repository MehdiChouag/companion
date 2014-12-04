package com.anyfetch.companion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.fragments.ContextFragment;

/**
 * Launches ContextFragment
 */
public class ContextActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        Intent originIntent = getIntent();

        ContextualObject contextualObject = originIntent.getParcelableExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT);

        if (savedInstanceState == null) {
            ContextFragment fragment = ContextFragment.newInstance((Parcelable) contextualObject);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
