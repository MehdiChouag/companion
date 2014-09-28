package com.anyfetch.companion.api;

import android.content.Context;

import com.anyfetch.companion.api.helpers.BaseRequest;
import com.anyfetch.companion.api.pojo.Document;
import com.anyfetch.companion.api.pojo.DocumentsList;


/**
 * Flag a new document as important
 */
public class PostImportantDocumentRequest extends BaseRequest<DocumentsList> {
    private final String mEventId;
    private final String mDocumentId;

    /**
     * Constructs a new important document flagging request
     *
     * @param context An Android Context
     * @param eventId The event ID
     * @param document The document to flag as important
     */
    public PostImportantDocumentRequest(Context context, String eventId, Document document) {
        super(DocumentsList.class, context);
        mEventId = eventId;
        mDocumentId = document.getDocumentId();
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
