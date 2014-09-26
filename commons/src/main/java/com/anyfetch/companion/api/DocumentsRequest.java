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
        super(DocumentsList.class,
                context,
                BaseRequest.GET,
                String.format("/documents"),
                getParameters(contextQuery));
        mContextQuery = contextQuery;
    }

    private static Map<String, String> getParameters(String contextQuery) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("context", contextQuery);
        return map;
    }

    public String createCacheKey() {
        return "documents." + mContextQuery;
    }
}
