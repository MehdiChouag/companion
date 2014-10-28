package com.anyfetch.companion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.fragments.FullFragment;

/**
 * Launches FullFragment
 */
public class FullActivity extends Activity {

    private Document mDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);
        Intent originIntent = getIntent();

        mDocument = originIntent.getParcelableExtra(FullFragment.ARG_DOCUMENT);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, FullFragment.newInstance(mDocument))
                    .commit();
        }
    }
}
