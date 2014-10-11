package com.anyfetch.companion.commons.api;

import android.content.Context;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;

import java.util.Map;

/**
 * A Request for getting documents from a search query
 */
public class GetDocumentsListRequest extends BaseRequest<DocumentsList> {
    private final String mContextQuery;

    /**
     * Constructs new documents search context
     *
     * @param context      An Android Context
     * @param contextQuery An Anyfetch search query
     */
    public GetDocumentsListRequest(Context context, String contextQuery) {
        super(DocumentsList.class, context);
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
