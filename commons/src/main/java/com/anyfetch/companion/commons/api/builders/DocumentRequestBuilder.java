package com.anyfetch.companion.commons.api.builders;

import android.content.Context;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.requests.DeleteImportantDocumentRequest;
import com.anyfetch.companion.commons.api.requests.GetDocumentRequest;
import com.anyfetch.companion.commons.api.requests.PostImportantDocumentRequest;

/**
 * Creates a request to operate on documents
 */
public class DocumentRequestBuilder extends BaseRequestBuilder<Document> {
    private static final int GET = 0;
    private static final int MARK = 1;
    private static final int UNMARK = 2;
    private static final int TOGGLE = 3;

    private int mAction;
    private Document mDocument;

    /**
     * Creates a new Document Request Builder
     *
     * @param context The app/activity context
     */
    public DocumentRequestBuilder(Context context) {
        super(context);
        mAction = GET;
        mDocument = null;
    }

    /**
     * Sets the associated document
     *
     * @param document The document
     * @return A chainable builder
     */
    public DocumentRequestBuilder setDocument(Document document) {
        mDocument = document;
        return this;
    }

    /**
     * Sets the action to GET
     *
     * @return A chainable builder
     */
    public DocumentRequestBuilder actionGet() {
        mAction = GET;
        return this;
    }

    /**
     * Sets the action to POST
     *
     * @return A chainable builder
     */
    public DocumentRequestBuilder actionMark() {
        mAction = MARK;
        return this;
    }

    /**
     * Sets the action to DELETE
     *
     * @return A chainable builder
     */
    public DocumentRequestBuilder actionUnmark() {
        mAction = UNMARK;
        return this;
    }

    /**
     * Sets the action to either POST or DELETE depending from the state of the document
     *
     * @return A chainable builder
     */
    public DocumentRequestBuilder actionToggle() {
        mAction = TOGGLE;
        return this;
    }

    @Override
    public BaseRequest<Document> build() {
        String sq = "";
        if (getContextualObject() != null) {
            sq = getContextualObject().getSearchQuery(getTailedEmails());
            if (getContextualObject() instanceof Event) {
                Event contextual = (Event) getContextualObject();
                String eId = Long.toString(contextual.getId());
                if (mAction == MARK) {
                    return markRequest(eId);
                } else if (mAction == UNMARK) {
                    return unmarkRequest(eId);
                } else if (mAction == TOGGLE) {
                    if (mDocument.isImportant()) {
                        return unmarkRequest(eId);
                    } else {
                        return markRequest(eId);
                    }
                }
            }
        }
        return new GetDocumentRequest(getServerUrl(), getApiToken(), mDocument.getDocumentId(), sq);
    }

    private BaseRequest<Document> markRequest(String eId) {
        return new PostImportantDocumentRequest(getServerUrl(), getApiToken(), eId, mDocument.getDocumentId());
    }

    private BaseRequest<Document> unmarkRequest(String eId) {
        return new DeleteImportantDocumentRequest(getServerUrl(), getApiToken(), eId, mDocument.getDocumentId());
    }
}
