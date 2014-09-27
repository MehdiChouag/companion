package com.anyfetch.companion.api;

import android.test.suitebuilder.annotation.LargeTest;

import com.anyfetch.companion.api.pojo.Document;
import com.anyfetch.companion.api.pojo.DocumentsList;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

@LargeTest
public class DocumentsRequestTest extends BaseRequestTest {
    private DocumentsRequest mRequest;
    private MockWebServer mMockServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockServer = MockApiFactory.create(BaseRequestTest.DEFAULT_API_TOKEN);
        mMockServer.play();
        setApiUrl(mMockServer.getUrl("").toString());

        mRequest = new DocumentsRequest(getContext(), "test");
        mRequest.setRestTemplate(new JacksonSpringAndroidSpiceService().createRestTemplate());
    }

    public void test_loadDataFromNetwork() throws Exception {
        DocumentsList docs = mRequest.loadDataFromNetwork();
        RecordedRequest record = mMockServer.takeRequest();
        assertEquals("/documents", record.getPath());

        assertEquals(docs.size(), 1);

        Document doc = docs.get(0);
        assertEquals(doc.getTitle(), "test");
    }

    @Override
    protected void tearDown() throws Exception {
        mMockServer.shutdown();
    }
}
