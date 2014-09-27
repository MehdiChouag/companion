package com.anyfetch.companion.api;

import android.content.Context;

import com.anyfetch.companion.api.helpers.BaseRequest;
import com.anyfetch.companion.api.pojo.Document;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.net.URLEncoder;

/**
 * Gets a single document
 */
public class GetDocumentRequest extends BaseRequest<Document> {
    private final String mContextQuery;
    private final String mDocumentId;

    /**
     * Constructs new documents search context
     *
     * @param context An Android Context
     * @param documentId The id of the document
     * @param contextQuery An Anyfetch search query for highlighting
     */
    public GetDocumentRequest(Context context, String documentId, String contextQuery) {
        super(Document.class, context);
        mDocumentId = documentId;
        mContextQuery = contextQuery;
    }

    @Override
    public Document loadDataFromNetwork() throws Exception {
        GenericUrl url = new GenericUrl(
                getApiUrl() +
                        "/documents/" +
                        mDocumentId +
                        "?context=" +
                        URLEncoder.encode(mContextQuery, "UTF-8"));
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(url);
        request.setHeaders(getHeaders());
        HttpResponse response = request.execute();
        return new Gson().fromJson(IOUtils.toString(response.getContent(), "UTF-8"), getResultType());
    }

    public String createCacheKey() {
        return "document." + mDocumentId;
    }
}
