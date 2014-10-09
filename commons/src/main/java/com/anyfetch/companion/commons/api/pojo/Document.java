package com.anyfetch.companion.commons.api.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * A class used to deflate AnyFetch documents
 */
public class Document implements Parcelable {
    public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() {

        @Override
        public Document createFromParcel(Parcel source) {
            String documentId = source.readString();
            String companyId = source.readString();
            String eventId = source.readString();
            String type = source.readString();
            Date date = new Date(source.readLong());
            String title = source.readString();
            String snippet = source.readString();
            String full = source.readString();
            return new Document(type, documentId, companyId, eventId, date, title, snippet, full);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };

    private static final int DOCUMENT_PARCELABLE = 15;
    private final String type;
    private final String documentId;
    private final String companyId;
    private final String eventId;
    private final Date date;
    private String title;
    private String snippet;
    private String full;

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

    @Override
    public int describeContents() {
        return DOCUMENT_PARCELABLE;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeString(companyId);
        dest.writeString(eventId);
        dest.writeString(type);
        dest.writeLong(date.getTime());
        dest.writeString(title);
        dest.writeString(snippet);
        dest.writeString(full);
    }
}
