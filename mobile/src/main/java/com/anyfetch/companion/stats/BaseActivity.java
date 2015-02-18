package com.anyfetch.companion.stats;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.anyfetch.companion.R;

/**
 * Created by mehdichouag on 16/02/15.
 */
public  abstract class BaseActivity extends ActionBarActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRessource());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

    protected abstract int getLayoutRessource();
}
