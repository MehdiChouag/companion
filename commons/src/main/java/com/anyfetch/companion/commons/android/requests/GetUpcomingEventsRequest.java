package com.anyfetch.companion.commons.android.requests;

import android.content.Context;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * Gets the upcoming requests
 */
public class GetUpcomingEventsRequest extends SpiceRequest<EventsList> {
    private static final String TAG = "GetUpcomingEventsRequest";
    private final Context mContext;

    public GetUpcomingEventsRequest(Context context) {
        super(EventsList.class);
        Log.i(TAG, "Create Upcoming Events Request");
        mContext = context;
    }

    @Override
    public EventsList loadDataFromNetwork() throws Exception {
        Log.i(TAG, "Load");
        return Event.getUpcomingEvents(mContext);
    }

    public String createCacheKey() {
        return "events";
    }
}

