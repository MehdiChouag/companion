package com.anyfetch.companion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;

public abstract class BaseRequestTest extends InstrumentationTestCase {
    private static final String API_TOKEN = "testToken";

    private String mApiUrl = "/";

    public void setApiUrl(String apiUrl) {
        mApiUrl = apiUrl;
    }

    public Context getTestContext() {
        Context context = getInstrumentation().getContext();
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferences.putString("apiUrl", mApiUrl);
        preferences.putString("apiToken", API_TOKEN);
        preferences.commit();
        return context;
    }
}
