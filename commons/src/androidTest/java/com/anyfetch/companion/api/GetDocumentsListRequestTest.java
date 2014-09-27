package com.anyfetch.companion.api;

import android.test.suitebuilder.annotation.LargeTest;

import com.anyfetch.companion.api.helpers.BaseRequestTest;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;

@LargeTest
public class GetDocumentsListRequestTest extends BaseRequestTest {
    private GetDocumentsListRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRequest = new GetDocumentsListRequest(getContext(), "test context");
        mRequest.setHttpRequestFactory(new GsonGoogleHttpClientSpiceService().createRequestFactory());
    }

    public void test_loadDataFromNetwork() throws Exception {
        DocumentsList docs = mRequest.loadDataFromNetwork();

        assertEquals(1, docs.size());

        Document doc = docs.get(0);
        assertEquals("docId", doc.getId());
        assertEquals("test", doc.getTitle());
    }
}
