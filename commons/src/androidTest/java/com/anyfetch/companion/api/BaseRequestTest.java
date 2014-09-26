package com.anyfetch.companion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;

public abstract class BaseRequestTest extends InstrumentationTestCase {
    public static final String API_URL = "http://localhost:8000";
    private static final String API_TOKEN = "testToken";

    public Context getTestContext() {
        Context context = getInstrumentation().getContext();
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferences.putString("apiUrl", API_URL);
        preferences.putString("apiToken", API_TOKEN);
        preferences.commit();
        return context;
    }
}
