package com.anyfetch.companion.android;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

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
}
