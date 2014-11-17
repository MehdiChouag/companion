package com.anyfetch.companion.commons.android.pojo;

import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.Suppress;
import com.anyfetch.companion.commons.android.testhelpers.AndroidServicesMockInjecter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class EventTest extends InstrumentationTestCase {
    private Context mContext;
    private long mId;
    private Event mEvent;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        AndroidServicesMockInjecter.injectContact(mContext);
        mId = AndroidServicesMockInjecter.injectEvent(mContext);
        mEvent = new Event(1, "a", "b", new Date(0), new Date(0), new ArrayList<Person>(), "c");
    }

    @Suppress
    public void test_getUpcomingEvents() throws Exception {
        List<Event> events = Event.getUpcomingEvents(mContext);

        assertNotSame(0, events.size());

        Event event = events.get(0);
        assertEquals("Secret Briefing", event.getTitle());
    }

    @Suppress
    public void test_getEvent() throws Exception {
        Event event = Event.getEvent(mContext, mId);

        assertEquals("Secret Briefing", event.getTitle());
        assertEquals("Not Disclosed", event.getDescription());
        assertEquals("A Secret Place", event.getLocation());

        List<Person> attendees = event.getAttendees();

        Person att0 = attendees.get(0);
        assertEquals("Sterling Archer", att0.getName());
        assertEquals("Secret Agent", att0.getJob());

        Person att1 = attendees.get(1);
        assertEquals("Malory Archer", att1.getName());
    }

    public void testParcels() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcel", mEvent);
        Event target = bundle.getParcelable("parcel");
        assertEquals(mEvent.getId(), target.getId());
        assertEquals(mEvent.getTitle(), target.getTitle());
        assertEquals(mEvent.getStartDate(), target.getStartDate());
    }

    public void test_getTitle() throws Exception {
        assertEquals(mEvent.getTitle(), "a");
    }

    public void test_getId() throws Exception {
        assertEquals(mEvent.getId(), 1);
    }

    public void test_getInfo() throws Exception {
        assertEquals(mEvent.getInfo(), "c\n" +
                "01:00 - 01:00");
    }

    public void test_getSearchQuery() throws Exception {
        assertEquals(mEvent.getSearchQuery(new HashSet<String>()), "(a)");

    }
}