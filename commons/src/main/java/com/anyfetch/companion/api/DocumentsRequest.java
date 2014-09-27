package com.anyfetch.companion.api;

import android.content.Context;

import com.anyfetch.companion.api.pojo.DocumentsList;

import java.util.HashMap;
import java.util.Map;

/**
 * A Request for getting documents from a search query
 */
public class DocumentsRequest extends BaseRequest<DocumentsList> {
    private final String mContextQuery;

    /**
     * Constructs new documents search context
     *
     * @param context An Android Context
     * @param contextQuery An Anyfetch search query
     */
    public DocumentsRequest(Context context, String contextQuery) {
        super(DocumentsList.class, context);
        mContextQuery = contextQuery;
    }

    @Override
    public DocumentsList loadDataFromNetwork() throws Exception {
        String url = String.format("%s/documents", getApiUrl());
        Map<String, String> queryString = new HashMap<String, String>();
        queryString.put("access_token", getApiToken());
        queryString.put("context", mContextQuery);

        return getRestTemplate().getForObject(url, DocumentsList.class);
    }

    public String createCacheKey() {
        return "documents." + mContextQuery;
    }
}
