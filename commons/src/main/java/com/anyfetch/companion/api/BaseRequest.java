package com.anyfetch.companion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ObjectParser;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

/**
 * Defines a base request to the API
 */
public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T> {
    private final String mApiUrl;
    private final String mApiToken;

    /**
     * Constructs a new request
     * @param klass The class used to deflate the result
     * @param context An Android Context
     */
    public BaseRequest(Class<T> klass, Context context) {
        super(klass);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mApiUrl = preferences.getString("apiUrl", "https://anyfetch-companion.herokuapp.com");
        mApiToken = preferences.getString("apiToken", null);
    }

    /**
     * Gets the globally set Anyfetch API URL (defaults to https://anyfetch-companion.herokuapp.com)
     * @return An URL prefix
     */
    public String getApiUrl() {
        return mApiUrl;
    }

    /**
     * Gets the gloablly set API token
     * @return A Bearer token
     */
    public String getApiToken() {
        return mApiToken;
    }

    /**
     * Returns the authentication header
     * @return A set of HTTP headers
     */
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAuthorization("Bearer " + getApiToken());
        return headers;
    }

    /**
     * Returns the Gson JSON Parser
     * @return An Object parser
     */
    public ObjectParser getParser() {
        return new GsonFactory().createJsonObjectParser();
    }
}
