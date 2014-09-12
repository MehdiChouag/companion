package com.anyfetch.companion.commons.models;

import android.media.Image;

import java.net.URL;

/**
 * Defines a generic document
 */
public interface Document {
    public String getTitle();
    public String getSnippet();
    public Image getIcon();
    public URL getMobileURL();
}
