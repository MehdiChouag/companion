package com.anyfetch.companion.api.pojo;

/**
 * A class used to deflate AnyFetch documents
 */
public class Document {
    private String title;
    private String id;

    public Document() {
        title = "";
        id = "";
    }

    public Document(String id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}
