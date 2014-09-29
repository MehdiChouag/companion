package com.anyfetch.companion.android;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

/**
 * Gets the upcoming requests
 */
public class GetUpcomingEventsRequest extends SpiceRequest<List<Event>> {

    private final Context mContext;

    public GetUpcomingEventsRequest(Class<List<Event>> klass, Context context) {
        super(klass);
        mContext = context;
    }

    @Override
    public List<Event> loadDataFromNetwork() throws Exception {
        return Event.getUpcomingEvents(mContext);
    }
}
