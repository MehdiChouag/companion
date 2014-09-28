package com.anyfetch.companion.api.pojo;

import android.test.InstrumentationTestCase;

import com.google.gson.Gson;

public class TestDocumentsList extends InstrumentationTestCase{
    public void test_json() throws Exception {
        Gson gson = new Gson();
        DocumentsList docs = gson.fromJson("[{\"documentId\": \"documentId\"}, {\"title\": \"title\"}]", DocumentsList.class);

        assertEquals(2, docs.size());

        assertEquals("documentId", docs.get(0).getDocumentId());
        assertEquals("title", docs.get(1).getTitle());
    }
}
