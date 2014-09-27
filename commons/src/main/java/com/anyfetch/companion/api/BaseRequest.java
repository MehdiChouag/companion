package com.anyfetch.companion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * Defines a base request to the API
 */
public abstract class BaseRequest<T> extends SpringAndroidSpiceRequest<T> {
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
}
