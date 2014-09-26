package com.anyfetch.companion.api;

import android.test.suitebuilder.annotation.LargeTest;

import com.anyfetch.companion.api.pojo.Document;
import com.anyfetch.companion.api.pojo.DocumentsList;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

@LargeTest
public class DocumentsRequestTest extends BaseRequestTest {
    private DocumentsRequest mRequest;
    private MockWebServer mMockServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockServer = new MockWebServer();
        MockResponse response = new MockResponse();
        response.setBody("[{}]");
        mMockServer.enqueue(response);
        mMockServer.play();
        String url = mMockServer.getUrl("/").toString();
        setApiUrl(url);

        mRequest = new DocumentsRequest(getTestContext(), "test");
        mRequest.setRestTemplate(new JacksonSpringAndroidSpiceService().createRestTemplate());
    }

    public void test_loadDataFromNetwork() throws Exception {
        DocumentsList docs = mRequest.loadDataFromNetwork();
        assertEquals(docs.size(), 1);

        Document doc = docs.get(0);
    }

    @Override
    protected void tearDown() throws Exception {
        mMockServer.shutdown();
    }
}
