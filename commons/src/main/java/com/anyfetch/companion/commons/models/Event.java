package com.anyfetch.companion.commons.models;

import java.util.Date;
import java.util.List;

/**
 * Defines a generic event interface
 */
public interface Event {
    public String getTitle();
    public Date getStart();
    public Date getEnd();
    public List<Attendee> getAttendees();
}
