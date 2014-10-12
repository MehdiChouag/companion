package com.anyfetch.companion.commons.api.helpers;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a base request to the API
 */
public abstract class BaseRequest<T> extends OkHttpSpiceRequest<T> {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");

    private final String mServerUrl;
    private final String mApiToken;

    /**
     * Constructs a new request
     *
     * @param klass     The class used to deflate the result
     * @param serverUrl The companion-server url
     * @param apiToken  The API token
     */
    protected BaseRequest(Class<T> klass, String serverUrl, String apiToken) {
        super(klass);
        mServerUrl = serverUrl;
        mApiToken = apiToken;
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        RequestBody body = null;
        String content = getContent();
        if (content != null) {
            body = RequestBody.create(JSON, content);
        } else if (!getMethod().equals("GET")) {
            body = RequestBody.create(TEXT, "");
        }
        Request.Builder builder = new Request.Builder().url(getUri().toURL()).method(getMethod(), body);
        Map<String, String> headers = getHeaders();
        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }
        Request request = builder.build();
        Response response = getOkHttpClient().newCall(request).execute();
        if (getExpectedCode() != response.code()) {
            throw new HttpException("Server returned code " + response.code());
        }
        if (getParseJson()) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            String stringBody = response.body().string();
            return gson.fromJson(stringBody, getResultType());
        } else {
            return null;
        }
    }

    /**
     * Gets the content of the request (could be overridden)
     *
     * @return No content
     */
    protected String getContent() {
        return null;
    }

    /**
     * Gets the companion server url
     *
     * @return An URL prefix
     */
    protected String getServerUrl() {
        return mServerUrl;
    }

    /**
     * Gets the gloablly set API token
     *
     * @return A Bearer token
     */
    protected String getApiToken() {
        return mApiToken;
    }

    /**
     * Gets the request headers (could be overridden)
     *
     * @return A set of HTTP headers containing the auth header
     */
    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + getApiToken());
        return headers;
    }

    /**
     * Gets the query string map (could be overridden)
     *
     * @return An empty map
     */
    protected Map<String, String> getQueryParameters() {
        return new HashMap<String, String>();
    }

    /**
     * Gets the http method for the request
     *
     * @return An HTTP verb
     */
    protected abstract String getMethod();

    /**
     * Gets the path to the resource
     *
     * @return The resource path starting with a slash
     */
    protected abstract String getPath();

    private URI getUri() throws URISyntaxException {
        Uri.Builder uriBuilder = Uri.parse(getServerUrl() + getPath()).buildUpon();
        Map<String, String> queryParameters = getQueryParameters();
        for (String key : queryParameters.keySet()) {
            uriBuilder.appendQueryParameter(key, queryParameters.get(key));
        }
        return new URI(uriBuilder.build().toString());
    }

    /**
     * Code asserted for the response (could be overridden)
     *
     * @return 200
     */
    protected int getExpectedCode() {
        return 200;
    }

    /**
     * Defines if the json has to be parsed
     *
     * @return true
     */
    protected boolean getParseJson() {
        return true;
    }

}
