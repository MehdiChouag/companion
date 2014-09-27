package com.anyfetch.companion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;

import com.squareup.okhttp.mockwebserver.MockWebServer;

public abstract class BaseRequestTest extends InstrumentationTestCase {
    public static final String DEFAULT_API_TOKEN = "testToken";

    private Context mContext;
    private MockWebServer mMockServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        setApiToken(DEFAULT_API_TOKEN);

        mMockServer = MockApiFactory.create(BaseRequestTest.DEFAULT_API_TOKEN);
        mMockServer.play();
        setApiUrl(mMockServer.getUrl("").toString());
    }


    @Override
    protected void tearDown() throws Exception {
        mMockServer.shutdown();
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
