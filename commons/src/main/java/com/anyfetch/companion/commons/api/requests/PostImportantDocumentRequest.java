package com.anyfetch.companion.commons.api.requests;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.Document;


/**
 * Flag a new document as important
 */
public class PostImportantDocumentRequest extends BaseRequest<Document> {
    private final String mEventId;
    private final String mDocumentId;

    /**
     * Constructs a new important document flagging request
     *
     * @param serverUrl  The companion-server url
     * @param apiToken   The API token
     * @param eventId    The event ID
     * @param documentId The document id to flag as important
     */
    public PostImportantDocumentRequest(String serverUrl, String apiToken, String eventId, String documentId) {
        super(Document.class, serverUrl, apiToken);
        mEventId = eventId;
        mDocumentId = documentId;
    }

    @Override
    protected String getMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
        return "/events/" + mEventId + "/importants/" + mDocumentId;
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
