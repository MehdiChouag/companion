package com.anyfetch.companion.commons.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.Map;

/**
 * Defines a generic attendee interface
 */
public interface Attendee {
    public String getName();
    public String getJob();
    public Bitmap getFace();
    public Map<String, String> getEmails();
    public Map<String, String> getNumbers();
    public String getLinkedInId();
}
