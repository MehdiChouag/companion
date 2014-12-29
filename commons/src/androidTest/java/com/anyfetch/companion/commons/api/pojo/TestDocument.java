package com.anyfetch.companion.commons.api.pojo;

import android.os.Bundle;
import android.test.InstrumentationTestCase;

import com.google.gson.Gson;

import java.util.Date;

public class TestDocument extends InstrumentationTestCase {
    private Document mDocument;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mDocument = new Document("a", "b", "c", "e", "f", new Date(), "g", "h", "i", "j", true);
    }

    public void test_json() throws Exception {
        Gson gson = new Gson();
        Document doc = gson.fromJson(gson.toJson(new Document("file", "Dropbox", "x", "y", "z", new Date(0), "Doc", "Docu", "Document", "url", false)), Document.class);
        assertEquals("file", doc.getType());
        assertEquals("x", doc.getDocumentId());
        assertEquals("y", doc.getCompanyId());
        assertEquals("z", doc.getEventId());
        assertEquals(new Date(0), doc.getDate());
        assertEquals("Doc", doc.getTitle());
        assertEquals("Docu", doc.getSnippet());
        assertEquals("Document", doc.getFull());
        assertEquals("url", doc.getLink());

    }

    public void testParcels() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcel", mDocument);
        Document target = bundle.getParcelable("parcel");
        assertEquals(mDocument.getTitle(), target.getTitle());
        assertEquals(mDocument.getDate(), target.getDate());
        assertEquals(mDocument.isImportant(), target.isImportant());
    }

    public void test_requireJavascript() throws Exception {
        Document jsDoc = new Document("a", "b", "c", "e", "f", new Date(), "g", "anyfetch-date", "anyfetch-date", "url", true);
        assertTrue(jsDoc.fullRequireJavascript());
        assertTrue(jsDoc.snippetRequireJavascript());
        assertFalse(mDocument.fullRequireJavascript());
        assertFalse(mDocument.snippetRequireJavascript());
    }
}
