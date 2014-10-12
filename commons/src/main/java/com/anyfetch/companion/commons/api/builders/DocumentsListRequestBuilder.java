package com.anyfetch.companion.commons.api.builders;

import android.content.Context;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.requests.GetDocumentsListRequest;
import com.anyfetch.companion.commons.api.requests.GetImportantDocumentsListRequest;

/**
 * Made for complex documents list requests generation
 */
public class DocumentsListRequestBuilder extends BaseRequestBuilder<DocumentsList> {

    private boolean mWithImportants;
    private boolean mWithNotImportants;

    /**
     * Creates a new DocumentsListRequestBuilder
     *
     * @param context The application/activity context
     */
    public DocumentsListRequestBuilder(Context context) {
        super(context);
        mWithImportants = false; // TODO: temporary as false
        mWithNotImportants = true;
    }

    /**
     * Sets if we need important documents in the results (only for events)
     *
     * @param withImportants Take importants ?
     * @return The chainable builder
     */
    public DocumentsListRequestBuilder setWithImportants(boolean withImportants) {
        mWithImportants = withImportants;
        return this;
    }

    /**
     * Sets if we need not important documents in the results (only for events)
     *
     * @param withNotImportants Take not importants ?
     */
    public DocumentsListRequestBuilder setWithNotImportants(boolean withNotImportants) {
        mWithNotImportants = withNotImportants;
        return this;
    }

    @Override
    public BaseRequest<DocumentsList> build() {
        String sq = "";
        if (getContextualObject() != null) {
            sq = getContextualObject().getSearchQuery();
            if (getContextualObject() instanceof Event) {
                if (mWithImportants && mWithNotImportants) {
                    return null; // TODO: Client & server: batch request endpoints
                    // return new GetAllDocumentsListRequest(
                    //      getServerUrl(),
                    //      getApiToken(),
                    //      Long.toString(((Event) mContextualObject).getId()),
                    //      mContextualObject.getSearchQuery()
                    // );
                } else if (mWithImportants) {
                    return new GetImportantDocumentsListRequest(
                            getServerUrl(),
                            getApiToken(),
                            Long.toString(((Event) getContextualObject()).getId()),
                            getContextualObject().getSearchQuery()
                    );
                }
            }
        }

        return new GetDocumentsListRequest(
                getServerUrl(),
                getApiToken(),
                sq
        );
    }
}
