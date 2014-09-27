package com.anyfetch.companion.api.helpers;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

public class MockApiFactory {
    public static MockWebServer create(final String testToken) {
        MockWebServer mock = new MockWebServer();
        mock.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
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
                        response.setBody("[{\"id\": \"docId\", \"title\": \"test\"}]");
                    }
                    if(request.getPath().startsWith("/documents/")) {
                        response = new MockResponse();
                        response.setBody("{\"id\": \"docId\", \"title\": \"test\"}");
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
