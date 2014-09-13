package com.anyfetch.companion.commons.models.demo;

import com.anyfetch.companion.commons.models.Attendee;
import com.anyfetch.companion.commons.models.Context;
import com.anyfetch.companion.commons.models.Document;
import com.anyfetch.companion.commons.models.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A sample demo event
 */
public class DemoEvent implements Event, Context {

    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;

    @Override
    public List<Document> getAssociatedDocuments() {
        ArrayList<Document> list = new ArrayList<Document>();
        list.add(new DemoNote());
        return list;
    }

    @Override
    public String getTitle() {
        return "Dreamforce '14 Aftermeeting";
    }

    @Override
    public Date getStart() {
        return new Date(new Date().getTime() + 3 * HOUR);
    }

    @Override
    public Date getEnd() {
        return new Date(new Date().getTime() + 4 * HOUR);
    }

    @Override
    public List<Attendee> getAttendees() {
        List<Attendee> attendees = new ArrayList<Attendee>();
        attendees.add(new DemoBenioff());
        return attendees;
    }
}
