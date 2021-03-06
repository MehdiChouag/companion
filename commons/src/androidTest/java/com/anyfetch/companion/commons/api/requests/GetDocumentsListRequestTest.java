package com.anyfetch.companion.commons.api.requests;

import android.test.suitebuilder.annotation.LargeTest;

import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.testhelpers.BaseRequestTest;
import com.squareup.okhttp.OkHttpClient;

@LargeTest
public class GetDocumentsListRequestTest extends BaseRequestTest {
    private GetDocumentsListRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRequest = new GetDocumentsListRequest(getServerUrl(), getApiToken(), "test context", 20);
        mRequest.setOkHttpClient(new OkHttpClient());
    }

    public void test_loadDataFromNetwork() throws Exception {
        DocumentsList docs = mRequest.loadDataFromNetwork();

        assertEquals(2, docs.size());

        assertEquals("doc0", docs.get(0).getDocumentId());
        assertEquals("doc1", docs.get(1).getDocumentId());
    }
}
