package com.anyfetch.companion.commons.api.requests;

import com.anyfetch.companion.commons.api.testhelpers.BaseRequestTest;
import com.squareup.okhttp.OkHttpClient;

public class PostImportantDocumentRequestTest extends BaseRequestTest {
    private PostImportantDocumentRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRequest = new PostImportantDocumentRequest(getServerUrl(), getApiToken(), "eventId", "docId");
        mRequest.setOkHttpClient(new OkHttpClient());
    }

    public void test_loadDataFromNetwork() throws Exception {
        mRequest.loadDataFromNetwork(); // Fails if other than 204 code
    }
}