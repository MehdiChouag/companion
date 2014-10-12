package com.anyfetch.companion.commons.api.requests;

import com.anyfetch.companion.commons.api.helpers.BaseRequestTest;
import com.squareup.okhttp.OkHttpClient;

public class DeleteImportantDocumentRequestTest extends BaseRequestTest {
    private DeleteImportantDocumentRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRequest = new DeleteImportantDocumentRequest(getServerUrl(), getApiToken(), "eventId", "docId");
        mRequest.setOkHttpClient(new OkHttpClient());
    }

    public void test_loadDataFromNetwork() throws Exception {
        mRequest.loadDataFromNetwork(); // Fails if other than 204 code
    }
}