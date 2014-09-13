package com.anyfetch.companion.commons.models.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anyfetch.companion.commons.R;
import com.anyfetch.companion.commons.models.Attendee;
import com.anyfetch.companion.commons.models.Context;
import com.anyfetch.companion.commons.models.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DemoDayon implements Attendee, Context {
    private android.content.Context activity;

    public DemoDayon(android.content.Context activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return "Alex Dayon";
    }

    @Override
    public String getJob() {
        return "Products, Salesforce.com";
    }

    @Override
    public Bitmap getFace() {

        return BitmapFactory.decodeResource(activity.getResources(), R.drawable.bg_dayon);
    }

    @Override
    public Map<String, String> getEmails() {
        return null;
    }

    @Override
    public Map<String, String> getNumbers() {
        return null;
    }

    @Override
    public String getLinkedInId() {
        return null;
    }

    @Override
    public List<Document> getAssociatedDocuments() {
        ArrayList<Document> list = new ArrayList<Document>();
        list.add(new DemoMail());
        list.add(new DemoNote());
        return list;
    }
}