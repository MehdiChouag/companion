package com.anyfetch.companion.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.anyfetch.companion.mobile.R;

public class AuthActivity extends Activity {
    private String mApiUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String apiToken = preferences.getString("apiToken", null);

        if(apiToken != null) {
            openEventsActivity(apiToken);
        }

        // TODO: get rid of -staging before merging
        mApiUrl = preferences.getString("apiUrl", "https://anyfetch-companion-staging.herokuapp.com");

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient() {
            // Ignore redirection to browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url)  {
                if(url.startsWith("https://localhost/done/")) {
                    String apiToken = url.substring(0, url.lastIndexOf('/') + 1);
                    openEventsActivity(apiToken);
                }
            }
        });
        mWebView.loadUrl(mApiUrl + "/init/connect");
    }

    private void openEventsActivity(String apiToken) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString("apiToken", apiToken);
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), UpcomingEventsActivity.class);
        startActivity(intent);    }
}
