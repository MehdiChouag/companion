package com.anyfetch.companion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // TODO: get rid of -staging before merging
        String apiUrl = preferences.getString("apiUrl", "https://anyfetch-companion-staging.herokuapp.com");

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            // Ignore redirection to browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.startsWith("https://localhost/done/")) {
                    String apiToken = url.substring(url.lastIndexOf('/') + 1, url.length());
                    backToUpcoming(apiToken);
                }
            }
        });
        webView.loadUrl(apiUrl + "/init/connect");
    }

    private void backToUpcoming(String apiToken) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString("apiToken", apiToken);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), UpcomingEventsActivity.class);
        startActivity(intent);
        finish();
    }
}
