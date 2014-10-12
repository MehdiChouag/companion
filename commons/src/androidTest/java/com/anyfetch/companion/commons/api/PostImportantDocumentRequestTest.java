package com.anyfetch.companion.commons.api;

import com.anyfetch.companion.commons.api.helpers.BaseRequestTest;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.requests.PostImportantDocumentRequest;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;

public class PostImportantDocumentRequestTest extends BaseRequestTest {
    private PostImportantDocumentRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Document document = new Document("file", "docId", "companyId", "eventId", new Date(), "Doc", "Docu", "Document");
        mRequest = new PostImportantDocumentRequest(getContext(), "eventId", document);
        mRequest.setOkHttpClient(new OkHttpClient());
    }

    public void test_loadDataFromNetwork() throws Exception {
        mRequest.loadDataFromNetwork(); // Fails if other than 204 code
    }
}