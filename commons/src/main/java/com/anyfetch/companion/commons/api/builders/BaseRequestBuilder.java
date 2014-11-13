package com.anyfetch.companion.commons.api.builders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.anyfetch.companion.commons.api.helpers.BaseRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for request building
 */
public abstract class BaseRequestBuilder<T> {

    public static final String TAILED_EMAILS = "tailed_emails";
    public static final String PREF_SERVER_URL = "serverUrl";
    public static final String DEFAULT_SERVER_URL = "https://anyfetch-companion.herokuapp.com";
    public static final String PREF_API_TOKEN = "apiToken";
    private Set<String> mTailedEmails;
    private String mServerUrl;
    private String mApiToken;
    private ContextualObject mContextualObject;

    /**
     * Creates a new Request Builder
     *
     * @param context The app/activity context
     */
    public BaseRequestBuilder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        mServerUrl = prefs.getString(PREF_SERVER_URL, DEFAULT_SERVER_URL);
        mApiToken = prefs.getString(PREF_API_TOKEN, "");
        mContextualObject = null;
        mTailedEmails = prefs.getStringSet(TAILED_EMAILS, new HashSet<String>());
    }

    /**
     * Build the request
     *
     * @return A base request for DocumentsList
     */
    public abstract BaseRequest<T> build();

    protected String getServerUrl() {
        return mServerUrl;
    }

    /**
     * Sets the server URL
     *
     * @param serverUrl An URL
     * @return A chainable builder object
     */
    public BaseRequestBuilder<T> setServerUrl(String serverUrl) {
        mServerUrl = serverUrl;
        return this;
    }

    protected String getApiToken() {
        return mApiToken;
    }

    /**
     * Sets the API token
     *
     * @param apiToken A token
     * @return A chainable builder object
     */
    public BaseRequestBuilder<T> setApiToken(String apiToken) {
        mApiToken = apiToken;
        return this;
    }

    public ContextualObject getContextualObject() {
        return mContextualObject;
    }

    /**
     * Sets the contextual object to be based upon
     *
     * @param contextualObject A contextual object
     */
    public BaseRequestBuilder<T> setContextualObject(ContextualObject contextualObject) {
        mContextualObject = contextualObject;
        return this;
    }

    protected Set<String> getTailedEmails() {
        return mTailedEmails;
    }

    /**
     * Sets the tailed emails
     *
     * @param tailedEmails A set of emails
     * @return The chainable builder
     */
    public BaseRequestBuilder setTailedEmails(Set<String> tailedEmails) {
        mTailedEmails = tailedEmails;
        return this;
    }
}
