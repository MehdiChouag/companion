package com.anyfetch.companion.commons.api.requests;

import android.util.Log;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;

/**
 * Hook on server when the application is started
 */
public class GetProvidersRequest extends BaseRequest<Object> {
    private static final String TAG = "GetProvidersRequest";

    /**
     * Constructs a new request
     *
     * @param serverUrl The companion-server url
     * @param apiToken  The API token
     */
    public GetProvidersRequest(String serverUrl, String apiToken) {
        super(Object.class, serverUrl, apiToken);
        Log.i(TAG, "Create Providers Request");
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/providers";
    }

    @Override
    protected int getExpectedCode() {
        return 200;
    }
}
