package com.anyfetch.companion.commons.models;

import android.graphics.Bitmap;

import java.util.List;

public interface LinkedInProfile {
    public String getName();
    public String getJob();
    public Bitmap getFace();
    public List<LinkedInProfile> getConnections();
    public List<String> getLikes();
}
