package com.anyfetch.companion.commons.api.testhelpers;

import android.test.InstrumentationTestCase;
import com.squareup.okhttp.mockwebserver.MockWebServer;

public abstract class BaseRequestTest extends InstrumentationTestCase {
    public static final String DEFAULT_API_TOKEN = "testToken";

    private MockWebServer mMockServer;
    private String mApiToken;
    private String mServerUrl;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mApiToken = DEFAULT_API_TOKEN;

        mMockServer = MockApiFactory.create(BaseRequestTest.DEFAULT_API_TOKEN);
        mMockServer.play();

        mServerUrl = mMockServer.getUrl("").toString();
    }


    @Override
    protected void tearDown() throws Exception {
        mMockServer.shutdown();
    }

    public String getApiToken() {
        return mApiToken;
    }

    public String getServerUrl() {
        return mServerUrl;
    }
}
