package com.anyfetch.companion.commons.models.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anyfetch.companion.commons.R;
import com.anyfetch.companion.commons.models.LinkedInProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rricard on 15/09/14.
 */
public class DemoLinkedBenioff implements LinkedInProfile {
    private android.content.Context activity;

    public DemoLinkedBenioff(android.content.Context activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return "Marc Benioff";
    }

    @Override
    public String getJob() {
        return "CEO, Salesforce.com";
    }

    @Override
    public Bitmap getFace() {

        return BitmapFactory.decodeResource(activity.getResources(), R.drawable.bg_benioff);
    }

    @Override
    public List<LinkedInProfile> getConnections() {
        ArrayList<LinkedInProfile> list = new ArrayList<LinkedInProfile>();
        list.add(new DemoLinkedHarris());
        list.add(new DemoLinkedDayon());
        return list;
    }

    @Override
    public List<String> getLikes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Motorbike");
        list.add("Golf");
        return list;
    }
}
