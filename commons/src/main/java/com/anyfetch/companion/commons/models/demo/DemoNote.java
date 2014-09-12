package com.anyfetch.companion.commons.models.demo;

import android.media.Image;

import com.anyfetch.companion.commons.models.Document;

import java.net.URL;

/**
 * A sample evernote note !
 */
public class DemoNote implements Document {
    @Override
    public String getTitle() {
        return "Dreamforce '14 Meetings";
    }

    @Override
    public String getSnippet() {
        return "Pre-meeting 12 hours before. After-meeting 3 hours after.";
    }

    @Override
    public Image getIcon() {
        return null;
    }

    @Override
    public URL getMobileURL() {
        return null;
    }
}
