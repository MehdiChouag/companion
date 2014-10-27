package com.anyfetch.companion.commons.api.requests;

import android.util.Log;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.Document;

/**
 * Unflag an important document
 */
public class DeleteImportantDocumentRequest extends BaseRequest<Document> {
    private static final String TAG = "DeleteImportantDocumentRequest";
    private final String mEventId;
    private final String mDocumentId;

    /**
     * Constructs a new important document unflagging request
     *
     * @param serverUrl  The companion-server url
     * @param apiToken   The API token
     * @param eventId    The event ID
     * @param documentId The document id to unflag as important
     */
    public DeleteImportantDocumentRequest(String serverUrl, String apiToken, String eventId, String documentId) {
        super(Document.class, serverUrl, apiToken);
        Log.i(TAG, "Create Del Imp Doc Request");
        Log.i(TAG, "EventId: " + eventId);
        Log.i(TAG, "DocumentId: " + documentId);
        mEventId = eventId;
        mDocumentId = documentId;
    }

    @Override
    protected String getMethod() {
        return "DELETE";
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
