package com.anyfetch.companion.api.android;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.anyfetch.companion.android.Event;
import com.anyfetch.companion.android.Person;
import com.anyfetch.companion.api.helpers.AndroidServicesMockInjecter;

import java.util.List;

public class EventTest  extends InstrumentationTestCase {
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
        assertEquals("Secret Briefing" , event.getTitle());
    }
}