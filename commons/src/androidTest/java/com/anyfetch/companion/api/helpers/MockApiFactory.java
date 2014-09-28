package com.anyfetch.companion.api.helpers;

import com.anyfetch.companion.api.pojo.Document;
import com.anyfetch.companion.api.pojo.DocumentsList;
import com.google.gson.Gson;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.util.Date;

public class MockApiFactory {
    private static DocumentsList createFakeDocumentSet(int amount) {
        DocumentsList docs = new DocumentsList();
        for(int i = 0; i < amount; i ++) {
            docs.add(new Document(
                    "file",
                    "doc" + i,
                    null,
                    null,
                    new Date(0),
                    "Doc" + i,
                    "Docu" + i,
                    "Document" + i));
        }
        return docs;
    }

    public static MockWebServer create(final String testToken) {
        MockWebServer mock = new MockWebServer();
        mock.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                Gson gson = new Gson();
                MockResponse response = null;
                // 1. Check for Auth
                if(!request.getHeader("Authorization").equals("Bearer " + testToken)) {
                    response = new MockResponse();
                    response.setResponseCode(403);
                    response.setStatus("Forbidden");
                }
                // 2. Route request
                if(response == null) {
                    if(request.getPath().startsWith("/documents?")) {
                        response = new MockResponse();
                        response.setBody(gson.toJson(createFakeDocumentSet(2)));
                    }
                    if(request.getPath().startsWith("/documents/")) {
                        response = new MockResponse();
                        response.setBody(gson.toJson(createFakeDocumentSet(1).get(0)));
                    }
                }
                // 3. 404 otherwise
                if(response == null) {
                    response = new MockResponse();
                    response.setResponseCode(404);
                    response.setStatus("Not Found");
                }

                return response;
            }
        });

        return mock;
    }
}
