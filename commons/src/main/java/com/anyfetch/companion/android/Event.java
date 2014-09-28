package com.anyfetch.companion.android;

import java.util.Date;
import java.util.List;

/**
 * Represents a calendar event
 */
public class Event {
    private final String mId;
    private final String mName;
    private final String mDescription;
    private final Date mStartDate;
    private final Date mEndDate;
    private final List<Person> mAttendees;

    /**
     * Creates a new event
     * @param id The unique identifier for the event
     * @param name The name
     * @param description The description
     * @param startDate The beginning
     * @param endDate The end
     * @param attendees The attendees
     */
    public Event(String id, String name, String description, Date startDate, Date endDate, List<Person> attendees) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mAttendees = attendees;
    }

    /**
     * Gets the id
     * @return An unique identifier
     */
    public String getId() {
        return mId;
    }

    /**
     * Gets the name
     * @return A name
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the description
     * @return A description
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Gets the beginning
     * @return A date
     */
    public Date getStartDate() {
        return mStartDate;
    }

    /**
     * Gets the end
     * @return A date
     */
    public Date getEndDate() {
        return mEndDate;
    }

    /**
     * Gets the attendees
     * @return A list of People
     */
    public List<Person> getAttendees() {
        return mAttendees;
    }
}
