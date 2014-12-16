package com.anyfetch.companion.commons.api.builders;

import android.content.Context;
import android.test.InstrumentationTestCase;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.requests.DeleteImportantDocumentRequest;
import com.anyfetch.companion.commons.api.requests.GetDocumentRequest;
import com.anyfetch.companion.commons.api.requests.PostImportantDocumentRequest;

import java.util.ArrayList;
import java.util.Date;

public class DocumentRequestBuilderTest extends InstrumentationTestCase {
    private Context mContext;
    private Document mDocument;
    private Event mEvent;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        mDocument = new Document("a", "b", "c", "d", "e", new Date(), "f", "g", "h", true);
        mEvent = new Event(1, "a", "b", new Date(0), new Date(0), new ArrayList<Person>(), "c", 0);
    }

    public void test_build_simple() throws Exception {
        BaseRequest<Document> req = new DocumentRequestBuilder(mContext).setDocument(mDocument).build();
        assertTrue(req instanceof GetDocumentRequest);
    }

    public void test_build_withContext() throws Exception {
        BaseRequest<Document> req = new DocumentRequestBuilder(mContext)
                .setDocument(mDocument)
                .setContextualObject(mEvent)
                .build();
        assertTrue(req instanceof GetDocumentRequest);
    }

    public void test_build_mark() throws Exception {
        BaseRequest<Document> req = new DocumentRequestBuilder(mContext)
                .actionMark()
                .setDocument(mDocument)
                .setContextualObject(mEvent)
                .build();
        assertTrue(req instanceof PostImportantDocumentRequest);
    }

    public void test_build_unmark() throws Exception {
        BaseRequest<Document> req = new DocumentRequestBuilder(mContext)
                .actionUnmark()
                .setDocument(mDocument)
                .setContextualObject(mEvent)
                .build();
        assertTrue(req instanceof DeleteImportantDocumentRequest);
    }

    public void test_build_toggle() throws Exception {
        BaseRequest<Document> req = new DocumentRequestBuilder(mContext)
                .actionToggle()
                .setDocument(mDocument)
                .setContextualObject(mEvent)
                .build();
        assertTrue(req instanceof DeleteImportantDocumentRequest);
    }
}
