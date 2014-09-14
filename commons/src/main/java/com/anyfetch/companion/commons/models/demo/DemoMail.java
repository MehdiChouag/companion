package com.anyfetch.companion.commons.models.demo;

import android.media.Image;

import com.anyfetch.companion.commons.R;
import com.anyfetch.companion.commons.models.Document;

import java.net.URL;

/**
 * A fake mail from Marc Benioff
 */
public class DemoMail implements Document {

    @Override
    public String getTitle() {
        return "Dreamforce '14";
    }

    @Override
    public String getSnippet() {
        return "Marc B. wrote:\n" +
               "Hi guys, just wanted to give you some feedbacks before DF14…";
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_gmail;
    }

    @Override
    public URL getMobileURL() {
        return null;
    }
}
