package com.anyfetch.companion.api.pojo;

import android.test.InstrumentationTestCase;

import com.google.gson.Gson;

public class TestDocument extends InstrumentationTestCase {
    public void test_json() throws Exception {
        Gson gson = new Gson();
        Document doc = gson.fromJson("{\"id\": \"id\", \"title\": \"title\"}", Document.class);
        assertEquals("id", doc.getId());
        assertEquals("title", doc.getTitle());
    }
}
