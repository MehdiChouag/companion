package com.anyfetch.companion.commons.android.pojo;

import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestCase;
import com.anyfetch.companion.commons.android.testhelpers.AndroidServicesMockInjecter;

import java.util.*;

public class EventTest extends InstrumentationTestCase {
    private Context mContext;
    private Event mEvent;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();

        mEvent = new Event(1, "a", "b", new Date(0), new Date(0), new ArrayList<Person>(), "c");
    }

    public void test_getUpcomingEvents() throws Exception {
        AndroidServicesMockInjecter.injectContact(mContext);
        AndroidServicesMockInjecter.injectEvent(mContext);
        List<Event> events = Event.getUpcomingEvents(mContext);

        assertNotSame(0, events.size());

        Event event = events.get(0);
        assertEquals("Secret Briefing", event.getTitle());
    }

    public void test_getEvent() throws Exception {
        long id = AndroidServicesMockInjecter.injectEvent(mContext);
        Event event = Event.getEvent(mContext, id);

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
        Calendar time = Calendar.getInstance();
        time.setTime(mEvent.getStartDate());
        assertEquals(mEvent.getInfo(), "c\n" +
                String.format("%02d:00 - %02d:00", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.HOUR_OF_DAY)));
    }

    public void test_getSearchQuery() throws Exception {
        assertEquals(mEvent.getSearchQuery(new HashSet<String>()), "(a)");
    }
}