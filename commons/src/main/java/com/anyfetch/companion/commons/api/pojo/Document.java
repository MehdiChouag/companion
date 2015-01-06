package com.anyfetch.companion.commons.api.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.anyfetch.companion.commons.api.helpers.HtmlUtils;

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
            String typeId = source.readString();
            String provider = source.readString();
            Date date = new Date(source.readLong());
            String title = source.readString();
            String snippet = source.readString();
            String full = source.readString();
            String actions = source.readString();
            boolean important = (source.readInt() > 0);

            return new Document(typeId, provider, documentId, companyId, eventId, date, title, snippet, full, actions, important);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };

    private static final int DOCUMENT_PARCELABLE = 15;
    private final String typeId;
    private final String providerId;
    private final String documentId;
    private final String companyId;
    private final String eventId;
    private final Date date;
    private final String title;
    private final String snippet;
    private final String full;
    private final String link;
    private final boolean important;

    public Document() {
        this.typeId = "";
        this.providerId = "";
        this.documentId = "";
        this.companyId = "";
        this.eventId = "";
        this.date = new Date();
        this.title = "";
        this.snippet = "";
        this.full = "";
        this.link = "";
        this.important = false;
    }

    public Document(String typeId, String providerId, String documentId, String companyId, String eventId, Date date, String title, String snippet, String full, String link, boolean important) {
        this.typeId = typeId;
        this.providerId = providerId;
        this.documentId = documentId;
        this.companyId = companyId;
        this.eventId = eventId;
        this.date = date;
        this.title = title;
        this.snippet = snippet;
        this.full = full;
        this.link = link;
        this.important = important;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getProviderId() {
        return providerId;
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

    public String getLink() {
        return link;
    }

    public boolean isImportant() {
        return important;
    }

    public boolean snippetRequireJavascript() {
        return HtmlUtils.requireJavascript(this.getSnippet());
    }

    public boolean fullRequireJavascript() {
        return HtmlUtils.requireJavascript(this.getFull());
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
        dest.writeString(typeId);
        dest.writeString(providerId);
        dest.writeLong(date.getTime());
        dest.writeString(title);
        dest.writeString(snippet);
        dest.writeString(full);
        dest.writeString(link);
        dest.writeInt(important ? 1 : 0);
    }
}
