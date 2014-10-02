package com.anyfetch.companion.commons.api;

import android.content.Context;

import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;

/**
 * Unflag an important document
 */
public class DeleteImportantDocumentRequest extends BaseRequest<DocumentsList> {
    private final String mEventId;
    private final String mDocumentId;

    /**
     * Constructs a new important document unflagging request
     *
     * @param context  An Android Context
     * @param eventId  The event ID
     * @param document The document to unflag as important
     */
    public DeleteImportantDocumentRequest(Context context, String eventId, Document document) {
        super(DocumentsList.class, context);
        mEventId = eventId;
        mDocumentId = document.getDocumentId();
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
