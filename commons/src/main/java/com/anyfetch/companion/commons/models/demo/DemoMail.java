package com.anyfetch.companion.commons.models.demo;

import android.media.Image;

import com.anyfetch.companion.commons.models.Document;

import java.net.URL;

/**
 * A fake mail from Marc Benioff
 */
public class DemoMail implements Document {

    @Override
    public String getTitle() {
        return "Dreamforce '14 demo";
    }

    @Override
    public String getSnippet() {
        return "I Just wanted to know if you guys are ready for the big presentation ?";
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
