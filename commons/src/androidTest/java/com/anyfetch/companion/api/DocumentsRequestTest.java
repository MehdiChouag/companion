package com.anyfetch.companion.api;

import android.test.suitebuilder.annotation.LargeTest;

import com.anyfetch.companion.api.pojo.Document;
import com.anyfetch.companion.api.pojo.DocumentsList;

@LargeTest
public class DocumentsRequestTest extends BaseRequestTest {
    private DocumentsRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRequest = new DocumentsRequest(getTestContext(), "test");
    }

    public void test_loadDataFromNetwork() throws Exception {
        DocumentsList docs = mRequest.loadDataFromNetwork();
        assertEquals(docs.size(), 1);

        Document doc = docs.get(0);
    }
}
