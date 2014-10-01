package com.anyfetch.companion.api.pojo;

import com.anyfetch.companion.helpers.DatedItem;

import java.util.Date;

/**
 * A class used to deflate AnyFetch documents
 */
public class Document implements DatedItem {
    private final String type;
    private final String documentId;
    private final String companyId;
    private final String eventId;
    private final Date date;

    public Document() {
        this.type = "";
        this.documentId = "";
        this.companyId = "";
        this.eventId = "";
        this.date = new Date();
        this.title = "";
        this.snippet = "";
        this.full = "";
    }

    public Document(String type, String documentId, String companyId, String eventId, Date date, String title, String snippet, String full) {
        this.type = type;
        this.documentId = documentId;
        this.companyId = companyId;
        this.eventId = eventId;
        this.date = date;
        this.title = title;
        this.snippet = snippet;
        this.full = full;
    }

    private String title;
    private String snippet;
    private String full;

    public String getType() {
        return type;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getEventId() {
        return eventId;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getFull() {
        return full;
    }
}
