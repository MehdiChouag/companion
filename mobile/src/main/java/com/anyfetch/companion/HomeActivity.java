package com.anyfetch.companion;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.anyfetch.companion.adapters.EventsListAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.api.requests.GetStartRequest;
import com.anyfetch.companion.fragments.ContextFragment;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.newrelic.agent.android.NewRelic;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONObject;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class HomeActivity extends ActionBarActivity {
    private static final int REQUEST_CONTACTPICKER = 1;

    private final SpiceManager mSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private final SpiceManager mHttpSpiceManager = new SpiceManager(HttpSpiceService.class);
    private StickyListHeadersListView mListView;
    private EventsListAdapter mListAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private MixpanelAPI mixpanel;

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
        mixpanel = MixPanel.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Start newrelic monitoring
        NewRelic.withApplicationToken("AA8f2983b4af8f945810684414d40a161c400b7569").start(this.getApplication());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serverUrl = preferences.getString(BaseRequestBuilder.PREF_SERVER_URL, BaseRequestBuilder.DEFAULT_SERVER_URL);
        String apiToken = preferences.getString(BaseRequestBuilder.PREF_API_TOKEN, null);

        // Log out the user if no userId set (coming from version 2.5.0 or before)
        if (preferences.getString("userId", "").isEmpty()) {
            apiToken = null;
        }

        if (apiToken == null) {
            openAuthActivity();
        } else {
            GetStartRequest startRequest = new GetStartRequest(serverUrl, apiToken);
            mHttpSpiceManager.execute(startRequest, null, 0, new RequestListener<Object>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    Toast.makeText(HomeActivity.this, String.format(getString(R.string.auth_issue), spiceException.getMessage()), Toast.LENGTH_LONG).show();
                    if (spiceException.getMessage().equals("403") || spiceException.getMessage().equals("401")) {
                        openAuthActivity();
                    }
                }

                @Override
                public void onRequestSuccess(Object o) {
                    Log.i("LambdaRequestListener", "Company Account Updated");
                }
            });

            setContentView(R.layout.activity_home);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.toolbar_elevation));
            setSupportActionBar(toolbar);
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                JSONObject props = MixPanel.buildProp("companyId", prefs.getString("companyId", "<unknown>"));
                mixpanel.track("Sign out", props);

                SharedPreferences.Editor editor = prefs.edit();
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
                intent.putExtra(ContextActivity.ORIGIN, "contactPicker");
                startActivity(intent);
            }
        }
    }

    private void openAuthActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}