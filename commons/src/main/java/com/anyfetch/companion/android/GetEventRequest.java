package com.anyfetch.companion.android;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * Gets an event
 */
public class GetEventRequest extends SpiceRequest<Event> {

    private final long mId;
    private final Context mContext;

    public GetEventRequest(Context context, long id) {
        super(Event.class);
        mContext = context;
        mId = id;
    }

    @Override
    public Event loadDataFromNetwork() throws Exception {
        return Event.getEvent(mContext, mId);
    }
}

