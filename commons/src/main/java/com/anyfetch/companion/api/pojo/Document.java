package com.anyfetch.companion.api.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * A class used to deflate AnyFetch documents
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
