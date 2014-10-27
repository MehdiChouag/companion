package com.anyfetch.companion.commons.api.requests;

import android.util.Log;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.Document;

import java.util.Map;

/**
 * Gets a single document
 */
public class GetDocumentRequest extends BaseRequest<Document> {
    private static final String TAG = "GetDocumentRequest";
    private final String mContextQuery;
    private final String mDocumentId;

    /**
     * Constructs new documents search context
     *
     * @param serverUrl    The companion-server url
     * @param apiToken     The API token
     * @param documentId   The id of the document
     * @param contextQuery An Anyfetch search query for highlighting
     */
    public GetDocumentRequest(String serverUrl, String apiToken, String documentId, String contextQuery) {
        super(Document.class, serverUrl, apiToken);
        Log.i(TAG, "Create Get Doc Request");
        Log.i(TAG, "DocumentId: " + documentId);
        Log.i(TAG, "ContextQuery: " + contextQuery);
        mDocumentId = documentId;
        mContextQuery = contextQuery;
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/documents/" + mDocumentId;
    }

    @Override
    protected Map<String, String> getQueryParameters() {
        Map<String, String> qp = super.getQueryParameters();
        qp.put("context", mContextQuery);
        return qp;
    }

    public String createCacheKey() {
        return "document." + mDocumentId;
    }
}
