package com.anyfetch.companion.api;

import android.content.Context;

import com.anyfetch.companion.api.helpers.BaseRequest;
import com.anyfetch.companion.api.pojo.DocumentsList;

import java.util.Map;

/**
 * A request that gets the important documents for an event
 */
public class GetImportantDocumentsListRequest extends BaseRequest<DocumentsList> {
    private final String mEventId;
    private final String mContextQuery;

    /**
     * Constructs new documents search context
     *
     * @param context An Android Context
     * @param contextQuery An Anyfetch search query
     */
    public GetImportantDocumentsListRequest(Context context, String eventId, String contextQuery) {
        super(DocumentsList.class, context);
        mEventId = eventId;
        mContextQuery = contextQuery;
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/events/" + mEventId + "/importants";
    }

    @Override
    protected Map<String, String> getQueryParameters() {
        Map<String, String> qp = super.getQueryParameters();
        qp.put("context", mContextQuery);
        return qp;
    }
}
