package com.anyfetch.companion.api;

import android.test.suitebuilder.annotation.LargeTest;

import com.anyfetch.companion.api.helpers.BaseRequestTest;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;

@LargeTest
public class GetDocumentRequestTest extends BaseRequestTest {
    private GetDocumentRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRequest = new GetDocumentRequest(getContext(), "docId", "test context");
        mRequest.setHttpRequestFactory(new GsonGoogleHttpClientSpiceService().createRequestFactory());
    }

    public void test_loadDataFromNetwork() throws Exception {
        Document doc = mRequest.loadDataFromNetwork();

        assertEquals("docId", doc.getId());
        assertEquals("test", doc.getTitle());
    }
}
