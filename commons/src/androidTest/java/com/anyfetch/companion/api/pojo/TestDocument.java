package com.anyfetch.companion.api.pojo;

import android.test.InstrumentationTestCase;

import com.google.gson.Gson;

import java.util.Date;

public class TestDocument extends InstrumentationTestCase {
    public void test_json() throws Exception {
        Gson gson = new Gson();
        Document doc = gson.fromJson(gson.toJson(new Document("file", "x", "y", "z", new Date(0), "Doc", "Docu", "Document")), Document.class);
        assertEquals("file", doc.getType());
        assertEquals("x", doc.getDocumentId());
        assertEquals("y", doc.getCompanyId());
        assertEquals("z", doc.getEventId());
        assertEquals(new Date(0), doc.getDate());
        assertEquals("Doc", doc.getTitle());
        assertEquals("Docu", doc.getSnippet());
        assertEquals("Document", doc.getFull());
    }
}
