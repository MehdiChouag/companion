package com.anyfetch.companion.commons.api.requests;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;

import java.util.Map;

/**
 * A request that gets the important documents for an event
 */
public class GetImportantDocumentsListRequest extends BaseRequest<DocumentsList> {
    private final String mEventId;
    private final String mContextQuery;

    /**
     * Constructs new important documents context
     *
     * @param serverUrl    The companion-server url
     * @param apiToken     The API token
     * @param eventId      The event ID
     * @param contextQuery An Anyfetch search query
     */
    public GetImportantDocumentsListRequest(String serverUrl, String apiToken, String eventId, String contextQuery) {
        super(DocumentsList.class, serverUrl, apiToken);
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

    public String createCacheKey() {
        return "importants." + mEventId + "." + mContextQuery;
    }
}
