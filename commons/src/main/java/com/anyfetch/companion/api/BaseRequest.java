package com.anyfetch.companion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Map;

/**
 * Defines a base request to the API
 */
public abstract class BaseRequest<T> extends SpringAndroidSpiceRequest<T> {
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int DELETE = 2;

    private final Class<T> mKlass;
    private final String mApiUrl;
    private final String mApiToken;
    private final int mMethod;
    private final String mPath;
    private Map<String, String> mQueryString;

    /**
     * Constructs a new request
     * @param klass The class used to deflate the result
     * @param context An Android Context
     */
    public BaseRequest(Class<T> klass, Context context, int method, String path, Map<String, String> queryString) {
        super(klass);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mKlass = klass;
        mApiUrl = preferences.getString("apiUrl", "https://anyfetch-companion.herokuapp.com");
        mApiToken = preferences.getString("apiToken", null);
        mMethod = method;
        mPath = path;
        mQueryString = queryString;
        queryString.put("access_token", mApiToken);
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        String url = String.format("%s/%s", mApiUrl, mPath);

        T object = null;
        switch (mMethod) {
            case GET:
                object = getRestTemplate().getForObject(url, mKlass, mQueryString);
                break;
            case POST:
                object = getRestTemplate().postForObject(url, null, mKlass, mQueryString);
                break;
            case DELETE:
                getRestTemplate().delete(url, mQueryString);
                break;
        }
        return object;
    }
}
