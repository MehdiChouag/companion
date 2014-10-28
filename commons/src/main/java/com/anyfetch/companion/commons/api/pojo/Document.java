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
            String provider = source.readString();
            Date date = new Date(source.readLong());
            String title = source.readString();
            String snippet = source.readString();
            String full = source.readString();
            boolean important = (source.readInt() > 0);
            return new Document(type, provider, documentId, companyId, eventId, date, title, snippet, full, important);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };

    private static final int DOCUMENT_PARCELABLE = 15;
    private final String type;
    private final String provider;
    private final String documentId;
    private final String companyId;
    private final String eventId;
    private final Date date;
    private final String title;
    private final String snippet;
    private final String full;
    private final boolean important;

    public Document() {
        this.type = "";
        this.provider = "";
        this.documentId = "";
        this.companyId = "";
        this.eventId = "";
        this.date = new Date();
        this.title = "";
        this.snippet = "";
        this.full = "";
        this.important = false;
    }

    public Document(String type, String provider, String documentId, String companyId, String eventId, Date date, String title, String snippet, String full, boolean important) {
        this.type = type;
        this.provider = provider;
        this.documentId = documentId;
        this.companyId = companyId;
        this.eventId = eventId;
        this.date = date;
        this.title = title;
        this.snippet = snippet;
        this.full = full;
        this.important = important;
    }

    public String getType() {
        return type;
    }

    public String getProvider() {
        return provider;
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

    public boolean isImportant() {
        return important;
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
        dest.writeString(provider);
        dest.writeLong(date.getTime());
        dest.writeString(title);
        dest.writeString(snippet);
        dest.writeString(full);
        dest.writeInt(important ? 1 : 0);
    }
}
