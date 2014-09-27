package com.anyfetch.companion.api;

import android.content.Context;

import com.anyfetch.companion.api.helpers.BaseRequest;
import com.anyfetch.companion.api.pojo.DocumentsList;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.net.URLEncoder;

/**
 * A Request for getting documents from a search query
 */
public class GetDocumentsListRequest extends BaseRequest<DocumentsList> {
    private final String mContextQuery;

    /**
     * Constructs new documents search context
     *
     * @param context An Android Context
     * @param contextQuery An Anyfetch search query
     */
    public GetDocumentsListRequest(Context context, String contextQuery) {
        super(DocumentsList.class, context);
        mContextQuery = contextQuery;
    }

    @Override
    public DocumentsList loadDataFromNetwork() throws Exception {
        GenericUrl url = new GenericUrl(
                getApiUrl() +
                "/documents?context=" +
                URLEncoder.encode(mContextQuery, "UTF-8"));
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(url);
        request.setHeaders(getHeaders());
        HttpResponse response = request.execute();
        return new Gson().fromJson(IOUtils.toString(response.getContent(), "UTF-8"), getResultType());
    }
}
