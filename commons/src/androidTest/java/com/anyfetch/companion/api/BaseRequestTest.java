package com.anyfetch.companion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;

public abstract class BaseRequestTest extends InstrumentationTestCase {
    public static final String DEFAULT_API_URL = "http://localhost";
    public static final String DEFAULT_API_TOKEN = "testToken";

    private Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        setApiUrl(DEFAULT_API_URL);
        setApiToken(DEFAULT_API_TOKEN);
    }

    protected void setApiUrl(String apiUrl) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString("apiUrl", apiUrl);
        editor.commit();
    }

    protected void setApiToken(String apiToken) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString("apiToken", apiToken);
        editor.commit();
    }

    protected Context getContext() {
        return mContext;
    }
}
