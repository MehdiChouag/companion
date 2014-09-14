package com.anyfetch.companion.commons.models.demo;

import android.media.Image;

import com.anyfetch.companion.commons.R;
import com.anyfetch.companion.commons.models.Document;

import java.net.URL;

public class DemoText implements Document {
    @Override
    public String getTitle() {
        return "Final Review";
    }

    @Override
    public String getSnippet() {
        return "The anyfetch wearable product could change the way we use salesforce";
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_gdrive;
    }

    @Override
    public URL getMobileURL() {
        return null;
    }
}
