package com.anyfetch.companion.api.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import org.apache.commons.io.IOUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a base request to the API
 */
public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T> {
    private static final String ENCODING = "UTF-8";
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

    @Override
    public T loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory().buildRequest(getMethod(), getUrl(), getContent());
        request.setHeaders(getHeaders());
        HttpResponse response = request.execute();
        return new Gson().fromJson(IOUtils.toString(response.getContent(), ENCODING), getResultType());
    }

    /**
     * Gets the content of the request (could be overridden)
     * @return No content
     */
    protected HttpContent getContent() {
        return null;
    }

    /**
     * Gets the globally set Anyfetch API URL (defaults to https://anyfetch-companion.herokuapp.com)
     * @return An URL prefix
     */
    protected String getApiUrl() {
        return mApiUrl;
    }

    /**
     * Gets the gloablly set API token
     * @return A Bearer token
     */
    protected String getApiToken() {
        return mApiToken;
    }

    /**
     * Gets the request headers (could be overridden)
     * @return A set of HTTP headers containing the auth header
     */
    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAuthorization("Bearer " + getApiToken());
        return headers;
    }

    /**
     * Gets the query string map (could be overridden)
     * @return An empty map
     */
    protected Map<String, String> getQueryParameters() {
        return new HashMap<String, String>();
    }

    /**
     * Gets the http method for the request
     * @return An HTTP verb
     */
    protected abstract String getMethod();

    /**
     * Gets the path to the resource
     * @return The resource path starting with a slash
     */
    protected abstract String getPath();

    private GenericUrl getUrl() throws UnsupportedEncodingException {
        Map<String, String> qp = getQueryParameters();
        String qs = "";
        if(!qp.isEmpty()) {
            qs = "?";
        }
        for(String key : qp.keySet()) {
            qs += key + "=" + URLEncoder.encode(qp.get(key), ENCODING) + "&";
        }
        if(qs.length() > 0) {
            qs = qs.substring(0, qs.length()-1);
        }


        return new GenericUrl(getApiUrl() + getPath() + qs);
    }
}
