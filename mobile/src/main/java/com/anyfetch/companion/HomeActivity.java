package com.anyfetch.companion;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;

import com.anyfetch.companion.adapters.HomeSlidePagerAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.api.pojo.ProvidersList;
import com.anyfetch.companion.commons.api.requests.GetProvidersRequest;
import com.anyfetch.companion.fragments.ContactPickerFragment;
import com.anyfetch.companion.fragments.ContextFragment;
import com.anyfetch.companion.helpers.Marketpace;
import com.anyfetch.companion.stats.MixPanel;
import com.anyfetch.companion.view.HomeViewPager;
import com.astuetz.PagerSlidingTabStrip;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.newrelic.agent.android.NewRelic;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONObject;

public class HomeActivity extends ActionBarActivity implements ContactPickerFragment.OnSearchView {
    private static final int REQUEST_CONTACTPICKER = 1;

    private final SpiceManager mSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private final SpiceManager mHttpSpiceManager = new SpiceManager(HttpSpiceService.class);
    private MixpanelAPI mixpanel;

    // Ui values
    private HomeViewPager mPager;
    private PagerSlidingTabStrip mSlidingTabLayout;

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
        mixpanel.track("HomeActivity", new JSONObject());
        bindView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Start newrelic monitoring
        NewRelic.withApplicationToken("AA8f2983b4af8f945810684414d40a161c400b7569").start(this.getApplication());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String apiToken = preferences.getString(BaseRequestBuilder.PREF_API_TOKEN, null);

        // Log out the user if no userId set (coming from version 2.5.0 or before)
        if (preferences.getString("userId", "").isEmpty()) {
            apiToken = null;
        }

        if (apiToken == null) {
            openAuthActivity();
            return;
        }
        doInitCall();
    }

    @Override
    public void onResume() {
        // Refresh snackbar (maybe we have new providers?)
        doInitCall();
        super.onResume();
    }


    /**
     * Request the number of documents currently connected
     */
    protected void doInitCall() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String serverUrl = preferences.getString(BaseRequestBuilder.PREF_SERVER_URL, BaseRequestBuilder.DEFAULT_SERVER_URL);
        String apiToken = preferences.getString(BaseRequestBuilder.PREF_API_TOKEN, null);

        GetProvidersRequest providersRequest = new GetProvidersRequest(serverUrl, apiToken);
        mHttpSpiceManager.execute(providersRequest, null, 0, new RequestListener<ProvidersList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Snackbar.with(getApplicationContext())
                        .text(getString(R.string.auth_issue))
                        .actionLabel(getString(R.string.auth_issue_action))
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .actionColor(getResources().getColor(R.color.anyfetchOpposite))
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                signOut();
                            }
                        })
                        .show(HomeActivity.this);
            }

            @Override
            public void onRequestSuccess(ProvidersList o) {
                Log.i("LambdaRequestListener", "Providers retrieved, count of " + o.getCount());

                if (o.getCount() == 0) {
                    Snackbar.with(getApplicationContext())
                            .text(getString(R.string.no_providers_yet))
                            .actionLabel(getString(R.string.no_providers_yet_action))
                            .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                            .actionColor(getResources().getColor(R.color.anyfetchOpposite))
                            .actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(Snackbar snackbar) {
                                    openMarketplace();
                                }
                            })
                            .show(HomeActivity.this);
                } else if (o.getCount() < 3) {
                    Snackbar.with(getApplicationContext())
                            .type(SnackbarType.MULTI_LINE)
                            .text(getString(R.string.few_providers_yet))
                            .actionLabel(getString(R.string.no_providers_yet_action))
                            .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                            .actionColor(getResources().getColor(R.color.anyfetchOpposite))
                            .actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(Snackbar snackbar) {
                                    openMarketplace();
                                }
                            })
                            .show(HomeActivity.this);
                }

            }
        });

    }

    private void bindView() {
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPager = (HomeViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(new HomeSlidePagerAdapter(getSupportFragmentManager(), this));

        mSlidingTabLayout = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);
        // Attach the view pager to the tab strip
        mSlidingTabLayout.setViewPager(mPager);

        ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.toolbar_elevation));
        ViewCompat.setElevation(mSlidingTabLayout, getResources().getDimension(R.dimen.toolbar_elevation));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(getString(R.string.title_activity_home));
            bar.setDisplayShowHomeEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_pick_contact:
                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactIntent, REQUEST_CONTACTPICKER);
                break;
            case R.id.action_pick_event:
                Intent eventIntent = new Intent(this, UpcomingEventsActivity.class);
                startActivity(eventIntent);
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_connect:
                openMarketplace();
                break;
            case R.id.action_log_out:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACTPICKER) {
            if (resultCode == RESULT_OK) {
                mixpanel.track("Pick contact", new JSONObject());

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

    private void openMarketplace() {
        new Marketpace(this, mixpanel).openMarketplace("Home");
    }

    private void signOut() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mixpanel.track("Sign out", new JSONObject());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("apiToken", null);
        editor.apply();
        openAuthActivity();
    }

    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    @Override
    public void showSlidingTabLayout() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        mSlidingTabLayout.startAnimation(animation);
        playAnimation(View.VISIBLE, animation);

        mPager.setSwipe(true);
    }

    @Override
    public void hideSlidingTabLayout() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        mSlidingTabLayout.startAnimation(animation);
        playAnimation(View.GONE, animation);

        mPager.setSwipe(false);
    }

    private void playAnimation(final int visibility, Animation animation) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (visibility == View.VISIBLE)
                    mSlidingTabLayout.setVisibility(visibility);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (visibility == View.GONE)
                    mSlidingTabLayout.setVisibility(visibility);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSlidingTabLayout.startAnimation(animation);
    }
}