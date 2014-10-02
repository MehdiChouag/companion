package com.anyfetch.companion.commons.api;

import android.content.Context;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.Document;

import java.util.Map;

/**
 * Gets a single document
 */
public class GetDocumentRequest extends BaseRequest<Document> {
    private final String mContextQuery;
    private final String mDocumentId;

    /**
     * Constructs new documents search context
     *
     * @param context      An Android Context
     * @param documentId   The id of the document
     * @param contextQuery An Anyfetch search query for highlighting
     */
    public GetDocumentRequest(Context context, String documentId, String contextQuery) {
        super(Document.class, context);
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
