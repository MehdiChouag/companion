package com.anyfetch.companion.api;

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
                if(!request.getPath().matches("access_token=" + testToken)) {
                    response = new MockResponse();
                    response.setResponseCode(403);
                }
                // 2. Route request
                if(response == null) {
                    if(request.getPath().startsWith("/documents")) {
                        response = new MockResponse();
                        response.setBody("[{\"title\": \"test\"}]");
                    }
                }
                // 3. 404 otherwise
                if(response == null) {
                    response = new MockResponse();
                    response.setResponseCode(404);
                }

                return response;
            }
        });

        return mock;
    }
}
