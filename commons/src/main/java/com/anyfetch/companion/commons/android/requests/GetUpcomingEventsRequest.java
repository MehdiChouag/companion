package com.anyfetch.companion.commons.android.requests;

import android.content.Context;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * Gets the upcoming requests
 */
public class GetUpcomingEventsRequest extends SpiceRequest<EventsList> {

    private final Context mContext;

    public GetUpcomingEventsRequest(Context context) {
        super(EventsList.class);
        mContext = context;
    }

    @Override
    public EventsList loadDataFromNetwork() throws Exception {
        return Event.getUpcomingEvents(mContext);
    }

    public String createCacheKey() {
        return "events";
    }
}
