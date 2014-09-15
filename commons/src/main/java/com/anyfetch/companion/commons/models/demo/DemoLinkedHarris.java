package com.anyfetch.companion.commons.models.demo;

import android.graphics.Bitmap;

import com.anyfetch.companion.commons.models.LinkedInProfile;

import java.util.List;

/**
 * Created by rricard on 15/09/14.
 */
public class DemoLinkedHarris implements LinkedInProfile {
    @Override
    public String getName() {
        return "Parker Harris";
    }

    @Override
    public String getJob() {
        return null;
    }

    @Override
    public Bitmap getFace() {
        return null;
    }

    @Override
    public List<LinkedInProfile> getConnections() {
        return null;
    }

    @Override
    public List<String> getLikes() {
        return null;
    }
}
