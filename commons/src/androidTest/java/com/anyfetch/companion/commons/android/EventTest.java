package com.anyfetch.companion.commons.android;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.anyfetch.companion.commons.android.helpers.AndroidServicesMockInjecter;

import java.util.List;

public class EventTest extends InstrumentationTestCase {
    private Context mContext;
    private long mId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        AndroidServicesMockInjecter.injectContact(mContext);
        mId = AndroidServicesMockInjecter.injectEvent(mContext);
    }

    public void test_getUpcomingEvents() throws Exception {
        List<Event> events = Event.getUpcomingEvents(mContext);

        assertNotSame(0, events.size());

        Event event = events.get(0);
        assertEquals("Secret Briefing", event.getTitle());
    }

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
}