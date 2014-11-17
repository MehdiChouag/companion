package com.anyfetch.companion.commons.api.requests;

import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.testhelpers.BaseRequestTest;
import com.squareup.okhttp.OkHttpClient;

public class GetImportantDocumentsListRequestTest extends BaseRequestTest {
    private GetImportantDocumentsListRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRequest = new GetImportantDocumentsListRequest(getServerUrl(), getApiToken(), "eventId", "test context");
        mRequest.setOkHttpClient(new OkHttpClient());
    }

    public void test_loadDataFromNetwork() throws Exception {
        DocumentsList docs = mRequest.loadDataFromNetwork();

        assertEquals(2, docs.size());

        assertEquals("doc0", docs.get(0).getDocumentId());
        assertEquals("event0", docs.get(0).getEventId());
        assertEquals("company0", docs.get(0).getCompanyId());
        assertEquals("doc1", docs.get(1).getDocumentId());
        assertEquals("event0", docs.get(1).getEventId());
        assertEquals("company0", docs.get(0).getCompanyId());
    }
}