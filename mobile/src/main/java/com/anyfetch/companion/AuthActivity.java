package com.anyfetch.companion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.anyfetch.companion.commons.android.helpers.AccountsHelper;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.api.builders.DocumentsListRequestBuilder;
import com.anyfetch.companion.fragments.OnboardingFragment;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Date;
import java.util.Set;

public class AuthActivity extends Activity {
    private static final String LOCAL_URL = "https://localhost/done/";
    private static final String[] URLS = new String[]{
            ".anyfetch.com/sign_in",
            ".anyfetch.com/sign_up",
            ".anyfetch.com/sign_out",
            ".anyfetch.com/oauth/authorize",
            ".herokuapp.com/init/callback",
    };

    private MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mixpanel = MixPanel.getInstance(this);
        mixpanel.track("AuthActivity", new JSONObject());
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serverUrl = preferences.getString(BaseRequestBuilder.PREF_SERVER_URL, BaseRequestBuilder.DEFAULT_SERVER_URL);

        if (!preferences.contains(OnboardingFragment.PREF_ONBOARDING_DISPLAYED)) {
            Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
            startActivity(intent);
        }

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            // Ignore redirection to browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // User has signed in, we can retrieve the token
                if (url.startsWith(LOCAL_URL)) {
                    try {
                        JSONObject data = new JSONObject(URLDecoder.decode(url.replace(LOCAL_URL, ""), "UTF-8"));
                        backToUpcoming(data);

                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Is it a link we want to allow?
                for (String validUrl : URLS) {
                    if (url.contains(validUrl)) {
                        return false;
                    }
                }

                // External link, opening in default browser.
                Log.i("LeavingApp", "Leaving app to " + url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                Toast.makeText(AuthActivity.this, R.string.auth_leaving_to_external_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        webView.loadUrl(serverUrl + "/init/connect");
    }

    private void backToUpcoming(JSONObject data) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        try {
            // Save the token
            editor.putString(BaseRequestBuilder.PREF_API_TOKEN, data.getString("token"));
            editor.putString("userEmail", data.getString("userEmail"));
            editor.putString("userId", data.getString("userId"));
            editor.putString("companyId", data.getString("companyId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Save a list of ignored emails
        Set<String> emails = new AccountsHelper().getOwnerEmails(this);
        editor.putStringSet(DocumentsListRequestBuilder.TAILED_EMAILS, emails);
        editor.apply();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        MixPanel.identify(mixpanel, this);
        mixpanel.getPeople().set("$email", prefs.getString("userEmail", "unknown@email.com"));
        mixpanel.getPeople().set("$name", prefs.getString("userEmail", "Unknown"));
        mixpanel.getPeople().set("userId", prefs.getString("userId", "<unknown>"));
        mixpanel.getPeople().set("companyId", prefs.getString("companyId", "<unknown>"));
        mixpanel.getPeople().set("$last_login", new Date().toString());
        mixpanel.getPeople().setOnce("$created", new Date().toString());

        JSONObject props = MixPanel.buildProp("companyId", prefs.getString("companyId", "<unknown>"));
        mixpanel.track("Sign in", props);

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
