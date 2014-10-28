package com.anyfetch.companion.commons.api.builders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.requests.GetDocumentsListRequest;
import com.anyfetch.companion.commons.api.requests.GetImportantDocumentsListRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * Made for complex documents list requests generation
 */
public class DocumentsListRequestBuilder extends BaseRequestBuilder<DocumentsList> {

    public static final String TAILED_EMAILS = "tailed_emails";
    private boolean mWithImportants;
    private boolean mWithNotImportants;
    private Set<String> mTailedEmails;
    private String mSubcontextName;

    /**
     * Creates a new DocumentsListRequestBuilder
     *
     * @param context The application/activity context
     */
    public DocumentsListRequestBuilder(Context context) {
        super(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        mWithImportants = false; // TODO: temporary as false
        mWithNotImportants = true;
        mTailedEmails = prefs.getStringSet(TAILED_EMAILS, new HashSet<String>());
    }

    /**
     * Selects a specific subcontext
     *
     * @param name The subcontext's name
     * @return The chainable builder
     */
    public DocumentsListRequestBuilder selectSubContext(String name) {
        mSubcontextName = name;
        return this;
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
     * @return The chainable builder
     */
    public DocumentsListRequestBuilder setWithNotImportants(boolean withNotImportants) {
        mWithNotImportants = withNotImportants;
        return this;
    }

    /**
     * Sets the tailed emails
     *
     * @param tailedEmails A set of emails
     * @return The chainable builder
     */
    public DocumentsListRequestBuilder setWithNotImportants(Set<String> tailedEmails) {
        mTailedEmails = tailedEmails;
        return this;
    }

    @Override
    public BaseRequest<DocumentsList> build() {
        String sq = "";
        if (getContextualObject() != null) {
            if (mSubcontextName != null) {
                sq = getContextualObject().getAdditionalSearchQueries(mTailedEmails).get(mSubcontextName);
            } else {
                sq = getContextualObject().getSearchQuery(mTailedEmails);
            }
            if (getContextualObject() instanceof Event) {
                if (mWithImportants && mWithNotImportants) {
                    return null; // TODO: Client & server: batch request endpoints
                    // return new GetAllDocumentsListRequest(
                    //      getServerUrl(),
                    //      getApiToken(),
                    //      Long.toString(((Event) mContextualObject).getId()),
                    //      mContextualObject.getSearchQuery(mTailedEmails),
                    //      mTailedEmails
                    // );
                } else if (mWithImportants) {
                    return new GetImportantDocumentsListRequest(
                            getServerUrl(),
                            getApiToken(),
                            Long.toString(((Event) getContextualObject()).getId()),
                            getContextualObject().getSearchQuery(mTailedEmails)
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
