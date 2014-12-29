package com.anyfetch.companion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.anyfetch.companion.commons.android.helpers.AccountsHelper;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.api.builders.DocumentsListRequestBuilder;

import java.util.Set;

public class AuthActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serverUrl = preferences.getString(BaseRequestBuilder.PREF_SERVER_URL, BaseRequestBuilder.DEFAULT_SERVER_URL);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            // Ignore redirection to browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.startsWith("https://localhost/done/")) {
                    String apiToken = url.substring(url.lastIndexOf('/') + 1, url.length());
                    backToUpcoming(apiToken);
                }
            }
        });
        webView.loadUrl(serverUrl + "/init/connect");
    }

    private void backToUpcoming(String apiToken) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        // Save the token
        editor.putString(BaseRequestBuilder.PREF_API_TOKEN, apiToken);

        // Save a list of ignored emails
        Set<String> emails = new AccountsHelper().getOwnerEmails(this);
        editor.putStringSet(DocumentsListRequestBuilder.TAILED_EMAILS, emails);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), UpcomingEventsActivity.class);
        startActivity(intent);
        finish();
    }
}
