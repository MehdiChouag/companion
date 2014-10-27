package com.anyfetch.companion.commons.api.requests;

import android.util.Log;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;

import java.util.Map;

/**
 * A Request for getting documents from a search query
 */
public class GetDocumentsListRequest extends BaseRequest<DocumentsList> {
    private static final String TAG = "GetDocumentsListRequest";
    private final String mContextQuery;

    /**
     * Constructs new documents search context
     *
     * @param serverUrl    The companion-server url
     * @param apiToken     The API token
     * @param contextQuery An Anyfetch search query
     */
    public GetDocumentsListRequest(String serverUrl, String apiToken, String contextQuery) {
        super(DocumentsList.class, serverUrl, apiToken);
        Log.i(TAG, "Create Get Docs Request");
        Log.i(TAG, "ContextQuery: " + contextQuery);
        mContextQuery = contextQuery;
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/documents";
    }

    @Override
    protected Map<String, String> getQueryParameters() {
        Map<String, String> qp = super.getQueryParameters();
        qp.put("context", mContextQuery);
        return qp;
    }

    public String createCacheKey() {
        return "documents." + mContextQuery;
    }
}
