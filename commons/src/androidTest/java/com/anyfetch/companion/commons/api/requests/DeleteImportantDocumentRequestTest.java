package com.anyfetch.companion.commons.api.requests;

import com.anyfetch.companion.commons.api.helpers.BaseRequestTest;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;

public class DeleteImportantDocumentRequestTest extends BaseRequestTest {
    private DeleteImportantDocumentRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Document document = new Document("file", "Dropbox", "docId", "companyId", "eventId", new Date(), "Doc", "Docu", "Document");
        mRequest = new DeleteImportantDocumentRequest(getContext(), "eventId", document);
        mRequest.setOkHttpClient(new OkHttpClient());
    }

    public void test_loadDataFromNetwork() throws Exception {
        mRequest.loadDataFromNetwork(); // Fails if other than 204 code
    }
}