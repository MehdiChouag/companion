package com.anyfetch.companion.commons.api.requests;

import android.util.Log;
import com.anyfetch.companion.commons.api.helpers.BaseRequest;

/**
 * Hook on server when the application is started
 */
public class GetStartRequest extends BaseRequest<Object> {
    private static final String TAG = "GetStartRequest";

    /**
     * Constructs a new request
     *
     * @param serverUrl The companion-server url
     * @param apiToken  The API token
     */
    public GetStartRequest(String serverUrl, String apiToken) {
        super(Object.class, serverUrl, apiToken);
        Log.i(TAG, "Create Start Request");
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/start";
    }

    @Override
    protected boolean getParseJson() {
        return false;
    }

    @Override
    protected int getExpectedCode() {
        return 204;
    }
}
